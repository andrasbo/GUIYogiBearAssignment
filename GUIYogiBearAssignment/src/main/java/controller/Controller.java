package controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import model.Game;
import model.HighScore;
import model.HighScoreManager;
import view.Canvas;
import view.MainWindow;
import view.StatsPanel;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class Controller extends JPanel {
    private Game game;
    private MainWindow mainWindow;
    private Canvas canvas;
    private StatsPanel stats;
    private HighScoreManager highScoreManager;
    
    private Timer gameTimer;
    private long startTime;
    private final int DELAY = 16;
    private int tickCounter = 0;
    
    public Controller(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.canvas = mainWindow.getCanvas();
        this.stats = mainWindow.getStatsPanel();
        highScoreManager = new HighScoreManager(10);

        gameTimer = new Timer(DELAY, (ActionEvent e) -> {
            gameLoop();
        });        
        
        newGame();
        initKeyBindings();
        
        this.mainWindow.addNewGameListener(e -> {newGame();});
        this.mainWindow.addHighScoresListener(e -> {showLeaderboard();});
        
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("SPACE"), "pauseGame");
        
        canvas.getActionMap().put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (gameTimer.isRunning()) {
                    gameTimer.stop();
                }
                else {
                    gameTimer.start();
                }
            }
        });        
    }
        
    private void initKeyBindings() {
        addMoveAction("A", -1, 0);
        addMoveAction("D",  1, 0);
        addMoveAction("W",  0, -1);
        addMoveAction("S",  0, 1);       
    }
        
    private void addMoveAction(String key, int dx, int dy) {
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), "move" + key);
        
        canvas.getActionMap().put("move" + key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.moveYogi(dx, dy); 
            }
        });
    }
    
    private void newGame() {
        if (gameTimer.isRunning()) gameTimer.stop();
        game = new Game();
        canvas.setGame(game);   
        startTime = System.currentTimeMillis();
        gameTimer.start();
    }
    
    private void gameLoop() {
        if (game == null) {return;}
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        tickCounter++;
        if (tickCounter >= 10) {
            game.moveARanger();
            tickCounter = 0;
        }
        
        stats.updateStats(game.getLives(), game.getScore(), elapsedTime);
        canvas.refresh();
        
        if (game.isGameOver()) {
            gameTimer.stop();
            handleGameOver();
        }
    }
    private void handleGameOver() {
        String name = mainWindow.showGameOverDialog(game.getScore());
        
        if (name != null && !name.trim().isEmpty()) {
            highScoreManager.putHighScore(name.trim(), game.getScore());
            showLeaderboard();
        }
    }

    private void showLeaderboard() {
        ArrayList<HighScore> top10 = highScoreManager.getHighScores();
        mainWindow.showLeaderboardDialog(top10);
    }

}

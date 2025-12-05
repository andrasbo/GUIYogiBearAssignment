package view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.HighScore;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class MainWindow extends JFrame {
    private Canvas canvas;
    private StatsPanel stats;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameItem;
    private JMenuItem highScoresItem; 

    public MainWindow() {        
        initSelf();
        initComponents();
        setVisible(true);
        showWelcomeMessage();        
    }

    private void initSelf() { 
        initStyle();
        setTitle("Yogi Bear Adventure");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }
    
    private void initStyle() {
        try {
            setIconImage(ImageIO.read(new File("src/main/resources/yogi.jpg")));
        } 
        catch (IOException e) {
            System.err.println("icon not found");
        }               
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex) {}          
    }
    
    private void initComponents() {
        initMenu();    
        canvas = new Canvas();
        stats = new StatsPanel();
        add(stats, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);  
    }
    
    private void initMenu() {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        newGameItem = new JMenuItem("New Game");
        highScoresItem = new JMenuItem("Leaderboard");
        
        gameMenu.add(newGameItem);
        gameMenu.add(highScoresItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    
    public void showWelcomeMessage() {
        String message = "<html><body style='width: 200px'>" +
                "<h2 style='color: rgb(34, 139, 34)'>Welcome to Yogi Bear Adventure!</h2>" +
                "<ul>" +
                "<li>Help Yogi find all the baskets</li>" +
                "<li>Avoid the rangers</li>" +
                "<li>Yogi has 3 hearts, don't lose them</li>" +
                "</ul>" +
                "<br>" +
                "<p>Controls: WASD</p>" +
                "<br>" +
                "<p>Good luck and bon'appetit!</p><br>" +
                "</body></html>";

        JOptionPane.showMessageDialog(this, message, "Welcome!", JOptionPane.INFORMATION_MESSAGE);
    }
    public String showGameOverDialog(int score) {
        String name = JOptionPane.showInputDialog(this, 
                "Yogi was slain by the rangers...\n" +
                "Baskets: " + score + "\n" +
                "Add your name to the leaderboard:", 
                "Game Over!", 
                JOptionPane.PLAIN_MESSAGE);
        
        return name;
    }
    public void showLeaderboardDialog(ArrayList<HighScore> top10) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h2 style='color: orange'>TOP 10</h2><ol>");
        
        for (HighScore h : top10) {
            sb.append("<li><b>").append(h.getName()).append("</b>: ")
              .append(h.getScore()).append(" points</li>");
        }
        sb.append("</ol></html>");
        
        if (top10.isEmpty()) {
            sb = new StringBuilder("No one yet...");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void addNewGameListener(java.awt.event.ActionListener listener) {
        newGameItem.addActionListener(listener);
    }
    public void addHighScoresListener(java.awt.event.ActionListener listener) {
        highScoresItem.addActionListener(listener);
    }

    
    public Canvas getCanvas() {return canvas;}
    public StatsPanel getStatsPanel() {return stats;}
}

package model;
        
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;       
import java.util.Random;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class Game {
    private final int Y_ROWS = 10;
    private final int X_COLS = 10;
    
    private Tile yogi;    
    private final ArrayList<Tile> map;
    private final ArrayList<Tile> rangers;
    private final ArrayList<Tile> baskets;
    
    private int level = 1;
    private int lives = 3;
    private int score = 0;
    
    private int startX;
    private int startY;
    private final Random random = new Random();
    private boolean gameOver = false;    

    public Game() {
        this.map = new ArrayList<>();
        this.rangers = new ArrayList<>();
        this.baskets = new ArrayList<>();
        
        this.level = 1;
        this.lives = 3;
        this.score = 0;        

        try {
            loadLevel(level);
        }
        catch (IOException e) {
            System.out.println("Level could not be loaded!");
        }

    }

    public void loadLevel(int level) throws FileNotFoundException, IOException {
        
        String filename = "/level" + level + ".txt";
        InputStream is = getClass().getResourceAsStream(filename);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        map.clear();
        rangers.clear();
        baskets.clear();
        yogi = null;
        
        String line;
        for (int y = 0; (line = br.readLine()) != null; y++) {
            int x = 0;
            for (char tileType : line.toCharArray()) {
                switch (tileType) {
                    case '#' -> map.add(new Tile(TileType.TREE,     x, y));
                    case '_' -> map.add(new Tile(TileType.GRASS,    x, y));
                    case '+' -> {
                        Tile basket = new Tile(TileType.BASKET, x, y);
                        baskets.add(basket);                         
                        map.add(basket);                      
                    }
                    case 'R' -> {
                        Tile ranger = new Tile(TileType.RANGER, x, y);
                        rangers.add(ranger);
                        map.add(ranger);
                    }
                    case 'Y' -> {
                        yogi = new Tile(TileType.YOGI, x, y);
                        startX = x;
                        startY = y;
                        map.add(yogi);
                    }
                    default -> System.out.println("Invalid tile in file!");
                }
                x++;
            }
        }
    }
    
    public Tile getTileAt(int x, int y) {
        return map.get(y * X_COLS + x);
    }
    
    public boolean isValidTile(int x, int y) {
        if (gameOver) {return false;}
        
        if (x < 0 || x >= X_COLS) {return false;}
        if (y < 0 || y >= Y_ROWS) {return false;}
        
        TileType dstType = getTileAt(x, y).getType();
        
        if (dstType == TileType.TREE) {return false;}
        
        return true;
    }    
    
    public void moveYogi(int dx, int dy) {
        if (yogi == null || gameOver) {return;}

        int xSrc = yogi.getX();
        int ySrc = yogi.getY();
        
        Tile src = getTileAt(xSrc, ySrc);
        
        if (src.getType() != TileType.YOGI) {return;}
        
        int xDst = xSrc + dx;
        int yDst = ySrc + dy;
        
        if (!isValidTile(xDst, yDst)) {return;} 
        
        Tile dst = getTileAt(xDst, yDst);
        TileType dstType = dst.getType();
        
        if (dstType == TileType.RANGER) {return;}
        
        if (dstType == TileType.BASKET) {
            score++;
            baskets.remove(dst);
            if (baskets.isEmpty()) {
                try {
                    level = (level % 10) + 1;
                    loadLevel(level);
                    return;
                }
                catch (IOException e) {
                    System.out.println("Level could not be loaded!");
                }
            }
        }

        if (isRangerInSight(xDst, yDst)) {
            lives--;
            if (lives <= 0) {gameOver = true;}
            dst = getTileAt(startX, startY);
        }
        
        yogi = dst; 
        dst.setType(TileType.YOGI);
        src.setType(TileType.GRASS);
    }
    
    public void moveARanger() {
        if (rangers.isEmpty() || gameOver) {return;}

        int srcIndex = random.nextInt(rangers.size());
        Tile src = rangers.get(srcIndex);
        
        int xSrc = src.getX();
        int ySrc = src.getY();
        int dx;
        int dy;
        
        int dir = random.nextInt(4);

        switch (dir) {
            case 0 -> {dx = 1; dy = 0;}
            case 1 -> {dx = -1; dy = 0;}
            case 2 -> {dx = 0; dy = 1;}
            case 3 -> {dx = 0; dy = -1;}
            default -> {dx = 0; dy = 0;}
        }
        
        int xDst = xSrc + dx;
        int yDst = ySrc + dy;

        if (!isValidTile(xDst, yDst)) {return;}

        Tile dst = getTileAt(xDst, yDst);
        TileType dstType = dst.getType();
        
        if (    dstType == TileType.YOGI ||
                dstType == TileType.BASKET) {return;}
        
        src.setType(TileType.GRASS);
        dst.setType(TileType.RANGER);
        rangers.set(srcIndex, dst);        
        
        if (isRangerInSight(yogi.getX(), yogi.getY())) {
            lives--;
            if (lives <= 0) {gameOver = true;}
            xSrc = yogi.getX();
            ySrc = yogi.getY();   
            yogi.setType(TileType.GRASS);
            yogi = getTileAt(startX, startY);             
            yogi.setType(TileType.YOGI);            
        }
    }
    
    public boolean isRangerInSight(int x, int y) {
        if ((x+1 < X_COLS && getTileAt(x+1, y).getType() == TileType.RANGER)
        ||  (x-1 > 0      && getTileAt(x-1, y).getType() == TileType.RANGER)
        ||  (y+1 < Y_ROWS && getTileAt(x, y+1).getType() == TileType.RANGER)
        ||  (y-1 > 0      && getTileAt(x, y-1).getType() == TileType.RANGER)) {
            return true;
        }
        else {return false;}
    }    
    
    public int getXCOLS() {return X_COLS;}
    public int getYROWS() {return Y_ROWS;}
    public Tile getYogi() {return yogi;}
    public int getScore() {return score;}
    public int getLives() {return lives;}
    public boolean isGameOver() {return gameOver;}
    public ArrayList<Tile> getMap() {return map;}
}

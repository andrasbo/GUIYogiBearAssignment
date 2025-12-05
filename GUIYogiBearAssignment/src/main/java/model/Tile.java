package model;

/**
 * y: row
 * x: col
 * @author Andras Boromissza [hyczbo]
 */
public class Tile {
    private TileType type;
    private final int x;    
    private final int y;
    
    public Tile(TileType type, int x, int y) {
        this.type = type;
        this.x = x;        
        this.y = y;
    }
    
    public TileType getType() {return type;}
    public void setType(TileType type) {
        this.type = type;
    }
    public int getX() {return x;}    
    public int getY() {return y;}
}

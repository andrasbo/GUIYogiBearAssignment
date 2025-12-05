package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.Game;
import model.Tile;
import model.TileType;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class Canvas extends JPanel {
    private Game game;
    private final int TILE_SIZE = 40;
    private Image yogiJPG;
    private Image rangerJPG;
    private Image treeJPG;
    private Image basketJPG;
    private Image grassJPG;

    public Canvas() { 
        setPreferredSize(new Dimension(TILE_SIZE*10, TILE_SIZE*10));
        setBackground(new Color(34, 139, 34));
        
        loadImages();
    }
    
    private void loadImages() {
        try {
            yogiJPG     = ImageIO.read(getClass().getResourceAsStream("/yogi.jpg"));
            rangerJPG   = ImageIO.read(getClass().getResourceAsStream("/ranger.jpg"));
            treeJPG     = ImageIO.read(getClass().getResourceAsStream("/tree.jpg"));
            basketJPG   = ImageIO.read(getClass().getResourceAsStream("/basket.jpg"));
            grassJPG    = ImageIO.read(getClass().getResourceAsStream("/grass.jpg"));        
        }
        catch (IOException e) {
            System.out.println("Images could not be loaded!");
        }
    }    
    
    public void setGame(Game game) {
        this.game = game;

        if (game != null) {
            setPreferredSize(new Dimension(
                    game.getXCOLS() * TILE_SIZE, game.getYROWS() * TILE_SIZE));
            revalidate();
        }
    }
    
    public void refresh() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        if (game == null) return;

        for (Tile t : game.getMap()) {
            drawTile(g, t);
        }
    }

    private void drawTile(Graphics g, Tile t) {
        int x = t.getX() * TILE_SIZE;
        int y = t.getY() * TILE_SIZE;
        
        switch (t.getType()) {
            case TileType.TREE ->
                g.drawImage(treeJPG,    x, y, TILE_SIZE, TILE_SIZE, null);
            case TileType.BASKET ->
                g.drawImage(basketJPG,  x, y, TILE_SIZE, TILE_SIZE, null);
            case TileType.RANGER ->
                g.drawImage(rangerJPG,  x, y, TILE_SIZE, TILE_SIZE, null); 
            case TileType.YOGI ->
                g.drawImage(yogiJPG,    x, y, TILE_SIZE, TILE_SIZE, null);  
            case TileType.GRASS ->
                g.drawImage(grassJPG,   x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }
}

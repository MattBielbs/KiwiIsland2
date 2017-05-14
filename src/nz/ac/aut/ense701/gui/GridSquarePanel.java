package nz.ac.aut.ense701.gui;

import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import nz.ac.aut.ense701.gameModel.Game;
import nz.ac.aut.ense701.gameModel.Terrain;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Toolkit;

/*
 * Panel for representing a single GridSquare of the island on the GUI.
 * 
 * @author AS
 * @version 1.0 - created
 */

public class GridSquarePanel extends javax.swing.JPanel 
{
    /** 
     * Creates new GridSquarePanel.
     * @param game the game to represent
     * @param row the row to represent
     * @param column the column to represent
     */
    private BufferedImage[] images = new BufferedImage[3];
    private BufferedImage background;
    private int numObjects = 0;
    private Color color;
    
    public GridSquarePanel(Game game, int row, int column)
    {
        this.game   = game;
        this.row    = row;
        this.column = column;
        initComponents();
    }

    /**
     * Updates the representation of the grid square panel.
     */
    public void update()
    {
        // get the GridSquare object from the world
        Terrain terrain   = game.getTerrain(row, column);
        boolean squareVisible = game.isVisible(row, column);
        boolean squareExplored = game.isExplored(row, column);

        
        String filename = terrain.getName();
        
        /*
        switch ( terrain )
        {
            case GRASS: 
                filename += "grass";
                color = Color.BLUE;
                break;
            case CLIFF:
                filename += "cliff";
                color = Color.DARK_GRAY;
                break;
            case WATER:
                filename += "water";
                color = Color.CYAN;
                break;
            default:
                filename += "grass";
                color = Color.LIGHT_GRAY;
                break;
        }
        */
        if(terrain.getName() != "grass")
        {
            Terrain left = terrain;
            Terrain right = terrain;
            Terrain bottom = terrain;
            Terrain top = terrain;
            //check left
            if(column + 1 < game.getIsland().getNumColumns())
            {
                left = game.getTerrain(row, column + 1);
            }
            //check right
            if(column - 1 >= 0)
            {
                right = game.getTerrain(row, column - 1);
            }
            //check bottom
            if(row + 1 < game.getIsland().getNumRows())
            {
                bottom = game.getTerrain(row + 1, column);
            }
            //check top
            if(row - 1 >= 0)
            {
                top = game.getTerrain(row - 1, column);
            }

            if(terrain.getStringRepresentation() != left.getStringRepresentation())
            {
                if(terrain.getStringRepresentation() != top.getStringRepresentation())
                {
                    //top left corner
                    filename += top.getName() + "toprh";
                }
                else
                {
                    if(terrain.getStringRepresentation() != bottom.getStringRepresentation())
                    {
                        //Bottom left  corner
                        filename += bottom.getName() + "botrh";
                    }
                    else
                    {
                        //left change
                        filename += left.getName() + "right";
                    }
                }
            }
            else
            {
               //check right
                if(terrain.getStringRepresentation() != right.getStringRepresentation())
                {
                    if(terrain.getStringRepresentation() != top.getStringRepresentation())
                    {
                        //top right corner
                        filename += top.getName() + "toplh";
                    }
                    else
                    {
                        if(terrain.getStringRepresentation() != bottom.getStringRepresentation())
                        {
                            //Bottom right  corner
                            filename += bottom.getName() + "botlh";
                        }
                        else
                        {
                            //right change
                            filename += right.getName() + "left";
                        }
                    }
                }
                else
                {
                    //check top
                    if(terrain.getStringRepresentation() != top.getStringRepresentation())
                    {
                        filename += top.getName() + "top";
                    }
                    else if(terrain.getStringRepresentation() != bottom.getStringRepresentation())
                    {
                        filename += bottom.getName() + "bot";
                    }
                    else
                    {
                        //center (do nothing)
                    }
                }
            }
        }

        filename += ".png";
        
        //Load in the correct image
        try
        {
            background = ImageIO.read(new File("images/" + filename));
        }
        catch(Exception ex)
        {

        }
        
        
        String[] occupants = game.getOccupantStringRepresentation(row,column).split(",");
        numObjects = occupants.length;
        
        if(occupants[0].equals(""))
            numObjects--;
            
        if ( squareExplored || squareVisible )
        {
            for(int i = 0; i < numObjects; i++)
            {
                try
                {
                    images[i] = ImageIO.read(new File("images/" + occupants[i]));
                    images[i] = imageToBufferedImage(makeColorTransparent(images[i], Color.WHITE));
                }
                catch(Exception ex)
                {

                }
            }
            
            // Set the colour. 
            if ( squareVisible && !squareExplored ) 
            {
                // When explored the colour is brighter
                /*
                color = new Color(Math.min(255, color.getRed()   + 128), 
                                  Math.min(255, color.getGreen() + 128), 
                                  Math.min(255, color.getBlue()  + 128));
                */
            }
            // set border colour according to 
            // whether the player is in the grid square or not
            setBorder(game.hasPlayer(row,column) ? activeBorder : normalBorder);
        }
        else
        {
            color = new Color(255,255,255);
            setBorder(normalBorder);
        }
        
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        //Background
        //g.setColor(color);
        //g.fillRect(0, 0, 60, 60);
        
        //Occupants
        if(game.isVisible(row, column))
        {
            g.drawImage(background, 0, 0, this);
        
            for(int i = 0; i < numObjects; i++)
            {
                g.drawImage(images[i], 8, 8 + (10*i), 30, 30, this);
            }
        }
    }
    
    public static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;
    }

    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private Game game;
    private int row, column;
    
    private static final Border normalBorder = new LineBorder(Color.BLACK, 1);
    private static final Border activeBorder = new LineBorder(Color.RED, 3);
}

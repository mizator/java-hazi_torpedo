package asd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Mozgathato hajo a hajok elhelyezesenel
 * @see TeddLeAHajokat
 * @see TorpedoPanel
 */
public class HajoPanel extends JPanel {

	/**
	 * A hajo mozog (le van nyomva az eger a hajon)
	 */
	private boolean isDraging = false;
	
	/**
	 * A hajo relativ x koordinataja
	 */
	private int relx;
	/**
	 * A hajo relativ y koordinataja
	 */
	private int rely;

	/**
	 * A hajo x koordinataja a jatekteren
	 */	
	private int cellx;
	/**
	 * A hajo y koordinataja a jatekteren
	 */	
	private int celly;
	
	/**
	 * A hajo merete a jatekteren
	 */	
	private int cellcount = 4;
	
	/**
	 * A hajo el van-e forgatva.
	 * Ha igaz, a hajo vizszintesen fekszik. 
	 */
	private boolean isRotated = false;
	
	/**
	 * A jatekter, amin a hajo van
	 */
	private TorpedoPanel torpedopanel;

	/**
	 * Beallitja, hogy a hajo el van-e forgatva.
	 * Ha igaz, a hajo vizszintesen fekszik. 
	 */
	public void setRotated(boolean b)
	{
		isRotated = b;
		calcSize();
		repaint();
	}
	
	/**
	 * Visszaadja, hogy a hajo el van-e forgatva.
	 */
	public boolean getRotated()
	{
		return isRotated;
	}
	
	/**
	 * Visszadja a hajo meretet (jatekter-cellakban)
	 */
	public int getCellcount()
	{
		return cellcount;
	}
	
	/**
	 * A hajo altal elfoglalt terulet (jatekter-cellakban)
	 */
	public Rectangle getRectangle()
	{
		if (isRotated) {
			return new Rectangle(cellx, celly, cellcount, 1);
		}
		else
		{
			return new Rectangle(cellx, celly, 1, cellcount);
		}
	}
	
	/**
	 *  A hajo pozicioja (jatekter-cellakban)
	 */
	public Point getCellPos()
	{
		return new Point(cellx, celly);
	}
	
	/**
	 * Kivant meret (pixel) a GUI-hoz
	 */
    public Dimension getPreferredSize() {
        return new Dimension(50,50);
    }
    
    /**
     * Kiszamolja es beallitja, hol es mekkoranak kell lennie a hajonak
     */
    private void calcSize() {
		if (isRotated) {
			setSize(cellcount*torpedopanel.getMeret(), torpedopanel.getMeret());
		}
		else
		{
			setSize(torpedopanel.getMeret(), cellcount*torpedopanel.getMeret());
		}
		setLocation(cellx*torpedopanel.getMeret()+torpedopanel.getOffset(), celly*torpedopanel.getMeret()+torpedopanel.getOffset());
    }

    /**
     * Beallitja a hajo poziciojat (pixelbol) drag&drop-nal
     */
    public void setLoc(int x, int y) {
		cellx = (x-torpedopanel.getOffset()+torpedopanel.getMeret()/2) / torpedopanel.getMeret();
		celly = (y-torpedopanel.getOffset()+torpedopanel.getMeret()/2) / torpedopanel.getMeret();           
		
		int meret = torpedopanel.palyameret*torpedopanel.getMeret()+torpedopanel.getOffset();
		setLocation(cellx*torpedopanel.getMeret()+torpedopanel.getOffset(), celly*torpedopanel.getMeret()+torpedopanel.getOffset());
    	if (getLocation().x > meret - getSize().width) {
    		setLoc(meret - getSize().width, getLocation().y);
    	}
    	if (getLocation().x < 0) {
    		setLoc(0, getLocation().y);
    	}
    	if (getLocation().y > meret - getSize().height) {
    		setLoc(getLocation().x, meret - getSize().height);
    	}
    	if (getLocation().y < 0) {
    		setLoc(getLocation().x, 0);
    	}
    }
    
    /**
     * Letrehozza a Hajopanel-t
     * @param _torpedopanel jatekter amin megjelenik
     * @param _cellcout hajo merete (cellakban)
     * @param moveable mozgathato?
     */
    public HajoPanel(TorpedoPanel _torpedopanel, int _cellcout, boolean moveable) {
    	torpedopanel = _torpedopanel;
    	cellcount = _cellcout;
    	
    	torpedopanel.addComponentListener( new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				calcSize();
			}

			@Override public void componentHidden(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
		});
    	
    	calcSize();
    	
    	if (moveable)
    	{
			addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	            	if (e.getButton() == 1) {
		            	isDraging = true;
		            	relx = e.getX();
		            	rely = e.getY();
	            	}
	            	else {
	            		isRotated = ! isRotated;
	            		calcSize();
	            		repaint();
	            	}
	            }
	            public void mouseReleased(MouseEvent e) {
	            	isDraging = false;
	        		setLoc(getLocation().x, getLocation().y);
	            }
	        });		
			addMouseMotionListener(new MouseAdapter() {
	            public void mouseDragged(MouseEvent e) {
	            	if (isDraging) {
	            		int x = getLocation().x - relx + e.getX();
	            		int y = getLocation().y - rely + e.getY();
	            		setLocation(x, y);         
	            		repaint();
	            	}
	            }
			});
    	}

	}

    /**
     * A hajo kirajzolasa
     */
	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(3, 3, getSize().width-6, getSize().height-6);
	}

}

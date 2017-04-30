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

public class HajoPanel extends JPanel {

	private boolean isDraging = false;
	private int relx;
	private int rely;

	private int cellx;
	private int celly;
	
	private int cellcount = 4;
	private boolean isRotated = false;
	
	private TorpedoPanel torpedopanel;

	public void setRotated(boolean b)
	{
		isRotated = b;
		calcSize();
		repaint();
	}
	
	public boolean getRotated()
	{
		return isRotated;
	}
	
	public int getCellcount()
	{
		return cellcount;
	}
	
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
	
	public Point getCellPos()
	{
		return new Point(cellx, celly);
	}
	
    public Dimension getPreferredSize() {
        return new Dimension(50,50);
    }
    
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

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(3, 3, getSize().width-6, getSize().height-6);
	}

}

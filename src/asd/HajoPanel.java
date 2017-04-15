package asd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class HajoPanel extends JPanel {

	private boolean isDraging = false;
	private int relx;
	private int rely;

	private int cellx;
	private int celly;
	
	private int offset;
	private int cellsize;

	private int cellcount = 4;
	private boolean isRotated = false;

	public void setOffsetSize(int _offset, int _cellsize) {
		if ((offset != _offset) || (cellsize != _cellsize))
		{
			offset = _offset;
			cellsize = _cellsize;
			calcSize();
			repaint();
		}
	}
	
    public void setCellCount(int c) {
    	cellcount = c;
    }


    public Dimension getPreferredSize() {
        return new Dimension(50,50);
    }
    
    private void calcSize() {
		if (isRotated) {
			setSize(cellcount*cellsize, cellsize);
		}
		else
		{
			setSize(cellsize, cellcount*cellsize);
		}
		setLoc(getLocation().x, getLocation().y);
    }

    public void setLoc(int x, int y) {
		cellx = (x-offset+cellsize/2) / cellsize;
		celly = (y-offset+cellsize/2) / cellsize;           
		
		setLocation(cellx*cellsize+offset, celly*cellsize+offset);
    	if (getLocation().x > getParent().getSize().width - getSize().width) {
    		setLoc(getParent().getSize().width - getSize().width, getLocation().y);
    	}
    	if (getLocation().x < 0) {
    		setLoc(0, getLocation().y);
    	}
    	if (getLocation().y > getParent().getSize().height - getSize().height) {
    		setLoc(getLocation().x, getParent().getSize().height - getSize().height);
    	}
    	if (getLocation().y < 0) {
    		setLoc(getLocation().x, 0);
    	}
    }
    
    public HajoPanel() {
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

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(3, 3, getSize().width-6, getSize().height-6);
	}

}

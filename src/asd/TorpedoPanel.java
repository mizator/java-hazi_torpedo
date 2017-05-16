package asd;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.awt.event.MouseMotionAdapter;

//public class 

public class TorpedoPanel extends JPanel {
	private boolean asd = false;
	public static final int palyameret = 10;
	private static final int textToLineOffset = 3;
	private static final String[] xcoordToStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static final String[] ycoordToStr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	private static final Color off_white = new Color(0xF8F8F8);

	private JatekLogika jateklogika;

	private int offset;
	private int meret;
	
	public int getOffset()
	{
		return offset;
	}
	
	public int getMeret()
	{
		return meret;
	}		
		
	private void calcOffsetMeret() {
		offset = Math.max(getFontMetrics(getFont()).getHeight(), getFontMetrics(getFont()).getMaxAdvance()) + textToLineOffset;
		meret = (Math.min(getSize().width, getSize().height) - offset - 1 ) / palyameret;
	}
	
	/**
	 * Create the panel.
	 */
	public TorpedoPanel(JatekLogika jatekLogika) {
		setLayout(null);
		jateklogika = jatekLogika;
		
		addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println(new String("mouse: ").concat(String.valueOf(e)));
                asd = true;
                repaint();
                
                if ((e.getX() > offset) && (e.getY() > offset) &&
                	(e.getX() < offset+meret*palyameret) && (e.getY() < offset+meret*palyameret))
                {                
	                int x = (e.getX()-offset)/meret;
	                int y = (e.getY()-offset)/meret;
	                System.out.println(new String("cella: ").concat(xcoordToStr[x]).concat(ycoordToStr[y]));
	                
	                if (jatekLogika != null)
	                {
	                	jatekLogika.loves(new Point(x, y));
	                }
                }
            }
        });
	}
	
	public void init() {
		calcOffsetMeret();

		
	}
	
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       
		if (asd) {
			//g.drawString("This is my custom Panel!",10,20);
		}
		
		calcOffsetMeret();
		
		g.setColor(off_white);
		g.fillRect(offset, offset, meret*palyameret, meret*palyameret);

		g.setColor(Color.black);
		for(int i = 0; i < palyameret; i ++)
		{
			g.drawLine(offset + i*meret, offset, offset + i*meret, offset + palyameret*meret);
			String s = xcoordToStr[i];
			g.drawString(s, offset + i*meret + meret/2 - g.getFontMetrics().stringWidth(s)/2, offset - textToLineOffset);
		}
		g.drawLine(offset + palyameret*meret, offset, offset + palyameret*meret, offset + palyameret*meret);

		for(int i = 0; i < palyameret; i ++)
		{
			g.drawLine(offset, offset + i*meret, offset + palyameret*meret, offset + i*meret);
			String s = ycoordToStr[i];
			g.drawString(s, offset - textToLineOffset - g.getFontMetrics().stringWidth(s), offset + i*meret + meret/2 + g.getFontMetrics().getHeight()/2);
		}		
		g.drawLine(offset, offset + palyameret*meret, offset + palyameret*meret, offset + palyameret*meret);
		
	}  
	
	
	
}

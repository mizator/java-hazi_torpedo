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


/**
 * jatekter a hajok elhelyezesehez
 * @see TeddLeAHajokat
 */
public class TorpedoPanel extends JPanel {
	/**
	 * A jatekter merete cellakban
	 */
	public static final int palyameret = 10;

	/**
	 * Milyen messze legyen a szoveg a vonaltol
	 */
	private static final int textToLineOffset = 3;

	/**
	 * jatekter feliratok vizszintesen
	 */
	private static final String[] xcoordToStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

	/**
	 * jatekter feliratok fuggolegesen
	 */
	private static final String[] ycoordToStr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	/**
	 * jatekter hatter szine
	 */	
	private static final Color off_white = new Color(0xF8F8F8);

	/**
	 * tavolsag az elso cella kezdeteig (pixelben)
	 */
	private int offset;
	
	/**
	 * A cellak merete (pixelben)
	 */
	private int meret;
	
	/**
	 * Visszaadja a tavolsagot az elso cella kezdeteig (pixelben)
	 */
	public int getOffset()
	{
		return offset;
	}
	
	/**
	 * Visszaadja a cellak meretet (pixelben)
	 */
	public int getMeret()
	{
		return meret;
	}		
		
	/**
	 * kiszamolja az offset-et es a meret-et, a objektum szelessege es magassaga alapjan
	 */
	private void calcOffsetMeret() {
		offset = Math.max(getFontMetrics(getFont()).getHeight(), getFontMetrics(getFont()).getMaxAdvance()) + textToLineOffset;
		meret = (Math.min(getSize().width, getSize().height) - offset - 1 ) / palyameret;
	}
	
	/**
	 * Create the panel.
	 */
	public TorpedoPanel() {
		setLayout(null);
		
		addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println(new String("mouse: ").concat(String.valueOf(e)));
                repaint();
                
                if ((e.getX() > offset) && (e.getY() > offset) &&
                	(e.getX() < offset+meret*palyameret) && (e.getY() < offset+meret*palyameret))
                {                
	                int x = (e.getX()-offset)/meret;
	                int y = (e.getY()-offset)/meret;
	                System.out.println(new String("cella: ").concat(xcoordToStr[x]).concat(ycoordToStr[y]));

                }
            }
        });
	}
	
	/**
	 * kiszamoltatja az offset-et es a meret-et
	 */
	public void init() {
		calcOffsetMeret();		
	}
	
	/**
	 * Kivant meret (pixel) a GUI-hoz
	 */	
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    /**
     * A jatekter kirajzolasa
     */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       

		
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

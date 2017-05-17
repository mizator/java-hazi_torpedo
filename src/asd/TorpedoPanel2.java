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

public class TorpedoPanel2 extends JPanel {
	private boolean asd = false;
	public static final int palyameret = 10;
	private static final int textToLineOffset = 3;
	private static final String[] xcoordToStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static final String[] ycoordToStr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	private static final Color off_white = new Color(0xF8F8F8);
	private static final Color off_white2 = new Color(0xE8E8E8);

	private JatekLogika jateklogika;

	private int offset;
	private int meret;
	
	public enum CellaTipus {Ures, Loves, NemTalalt, Talat, TalaltSullyedt};
	private CellaTipus[][] cellak = new CellaTipus[palyameret][palyameret];

	private ArrayList<Hajo> hajok = new ArrayList<Hajo>();
	
	public ArrayList<Hajo> getHajok()
	{
		return hajok;
	}

	public void clearHajok()
	{
		hajok.clear();
		repaint();
	}
	
	public void addHajo(Hajo hajo)
	{
		hajok.add(hajo);
		repaint();
	}

	
	public void clearCellak()
	{
		for(int i = 0; i < palyameret; i++)
			for(int j = 0; j < palyameret; j++)
				cellak[i][j] = CellaTipus.Ures;
	}
	
	public void setCella(int x, int y, CellaTipus tipus)
	{
		cellak[x][y] = tipus;
		repaint();
	}
	
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
	public TorpedoPanel2(JatekLogika jatekLogika) {
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

		calcOffsetMeret();
		
		if (isEnabled())
		{
			g.setColor(off_white);
		}
		else
		{
			g.setColor(off_white2);
		}
		
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
		
		
		for(Hajo hajo: hajok)
		{
			g.setColor(Color.red);
			int x = offset + hajo.pos.x * meret;
			int y = offset + hajo.pos.y * meret;
			int width;
			int height;

			if (hajo.isRotated)
			{
				width = hajo.cellsize * meret;
				height = meret;
			}
			else
			{
				width = meret;
				height = hajo.cellsize * meret;
			}
			
			g.fillRect(x+3, y+3, width-6, height-6);
		}
		
		
		for(int i = 0; i < palyameret; i++)
		{
			for(int j = 0; j < palyameret; j++)
			{
				int x = offset + i*meret;
				int y = offset + j*meret;
				
				if (cellak[i][j] == CellaTipus.NemTalalt)
				{
					g.setColor(Color.blue);
					g.drawLine(x+3, y+3, x+meret-3, y+meret-3);
					g.drawLine(x+meret-3, y+3, x+3, y+meret-3);					
				}
				else if (cellak[i][j] == CellaTipus.Talat)
				{
					g.setColor(Color.blue);
					g.fillRect(x+5, y+5, meret-10, meret-10);
				}
				else if (cellak[i][j] == CellaTipus.Loves)
				{
					g.setColor(Color.blue);
					g.fillArc(x+3, y+3, meret-6, meret-6, 0, 360);
				}				
			}
		}
	}  
	
	
	
}

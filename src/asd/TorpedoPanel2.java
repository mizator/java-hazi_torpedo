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
 * jatekter a jatekhoz, lovesekhez, stb
 * @see Fokepernyo
 */
public class TorpedoPanel2 extends JPanel {
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
	 * jatekter hatter szine, ha nincs engedelyezve
	 */	
	private static final Color off_white2 = new Color(0xE8E8E8);

	/**
	 * A jatek logikajat megvalosito objektum
	 */
	private JatekLogika jateklogika;

	/**
	 * tavolsag az elso cella kezdeteig (pixelben)
	 */
	private int offset;
	
	/**
	 * A cellak merete (pixelben)
	 */
	private int meret;
	
	/**
	 * A cellak lehetseges allapotai
	 */
	public enum CellaTipus {Ures, Loves, NemTalalt, Talat, TalaltSullyedt};
	/**
	 * A cellak allapotai
	 */
	private CellaTipus[][] cellak = new CellaTipus[palyameret][palyameret];

	/**
	 * Az elhelyezett hajok
	 */
	private ArrayList<Hajo> hajok = new ArrayList<Hajo>();
	
	/**
	 * Visszaadja az elhelyezett hajokat
	 */
	public ArrayList<Hajo> getHajok()
	{
		return hajok;
	}

	/**
	 * Kiuriti az elhelyezett hajok listajat
	 */
	public void clearHajok()
	{
		hajok.clear();
		repaint();
	}
	
	/**
	 * Uj hajot ad a hajok listajahoz
	 */
	public void addHajo(Hajo hajo)
	{
		hajok.add(hajo);
		repaint();
	}

	/**
	 * Minden cella allapotat alaphelyzetbe allitja
	 */
	public void clearCellak()
	{
		for(int i = 0; i < palyameret; i++)
			for(int j = 0; j < palyameret; j++)
				cellak[i][j] = CellaTipus.Ures;
	}
	
	/**
	 * Beallitja egy cella allapotat
	 */
	public void setCella(int x, int y, CellaTipus tipus)
	{
		cellak[x][y] = tipus;
		repaint();
	}
	
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
	public TorpedoPanel2(JatekLogika jatekLogika) {
		setLayout(null);
		jateklogika = jatekLogika;
		
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
	                                
	                if (jatekLogika != null)
	                {
	                	jatekLogika.loves(new Point(x, y));
	                }
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
				else if (cellak[i][j] == CellaTipus.TalaltSullyedt)
				{
					g.setColor(Color.black);
					g.fillRect(x+5, y+5, meret-10, meret-10);
				}
			}
		}
	}  
	
	
	
}

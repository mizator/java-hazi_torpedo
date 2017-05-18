package asd;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *	Hajoval kapcsolatos informaciokat tartalmazo osztaly. 
 *
 */
public class Hajo {

	/**
	 * A hajo pozicioja a jatekter.
	 */
	public Point pos;
	
	/**
	 * A hajo merete: a jatekteren hany cellat foglal el.
	 */
	public int cellsize;
	
	/**
	 * A hajo el van-e forgatva.
	 * Ha igaz, a hajo vizszintesen fekszik. 
	 */
	public boolean isRotated;
	
	/**
	 * Hanyszor talaltak el a hajot.
	 */
	private int talalatszam = 0;
	
	/**
	 * Letrehoz egy Hajo ebjektumot a megadott parameterek alapjan.
	 * @param _pos a hajo pozicioja
	 * @param _cellsize a hajo merete
	 * @param _isRotated a hajo vizszintesen fekszik
	 */
	public Hajo(Point _pos, int _cellsize, boolean _isRotated) {
		pos = _pos;
		cellsize = _cellsize;
		isRotated = _isRotated;
	}

	/**
	 * Visszaadja a hajo altal elfoglalt teruletet a jatekteren.
	 * @return A hajo altal elfoglalt teruletet a jatekteren.
	 */
	public Rectangle getRectangle()
	{
		if (isRotated) {
			return new Rectangle(pos.x, pos.y, cellsize, 1);
		}
		else
		{
			return new Rectangle(pos.x, pos.y, 1, cellsize);
		}
	}		
	
	/**
	 * Visszaadja, hogy a hajo elsullyedt-e.
	 */
	public boolean getSullyedt()
	{
		return talalatszam >= cellsize;
	}
	
	/**
	 * A hajot meglottek.
	 */
	public void meglottek()
	{
		talalatszam++;
	}

	
}

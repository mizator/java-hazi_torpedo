package asd;

import java.awt.Point;
import java.awt.Rectangle;

public class Hajo {
	public Point pos;
	public int cellsize;
	public boolean isRotated;
	private int talalatszam = 0;
	
	public Hajo(Point _pos, int _cellsize, boolean _isRotated) {
		pos = _pos;
		cellsize = _cellsize;
		isRotated = _isRotated;
	}
	
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
	
	public boolean getSullyedt()
	{
		return talalatszam >= cellsize;
	}
	public void meglottek()
	{
		talalatszam++;
	}

	
}

package asd;

import java.awt.Point;
import java.io.Serializable;

/**
 * A ket fel kozti halozati kapcsolaton atkuldendo uzenetet tartalmazo osztaly
 *
 */
public class Message implements Serializable {
	/**
	 * Az uzenetnek ilyen tipusai lehetnek 
	 */
	public enum Tipus {Loves, NemTalalt, Talat, TalaltSullyedt, Nyertel};
	/**
	 * Az uzenet tipusa
	 */
	Tipus tipus;
	
	/**
	 * Melyik cellara vonatkozik az uzenet
	 */
	public Point loc;
	
	/**
	 * Letrehoz egy Message objektumot
	 * @param t tipussal
	 * @param l cella pozicioval
	 */
	public Message(Tipus t, Point l)
	{
		tipus = t;
		loc = l;
	}
}

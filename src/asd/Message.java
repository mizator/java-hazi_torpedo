package asd;

import java.awt.Point;
import java.io.Serializable;

public class Message implements Serializable {
	public enum Tipus {Loves, NemTallalt, Talat, TalaltSullyedt};
	Tipus tipus;
	public Point loc;
}

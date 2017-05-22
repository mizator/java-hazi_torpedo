package asd;

/**
 * Halozati kapcsolat absztrakt osztalya.
 */
public abstract class Network {
	/**
	 * Uzenet kuldese az ellenfelnek.
	 */
	abstract void send(Message m);
	
	/**
	 * Kapcsolat lezarasa.
	 */
	abstract void disconnect();
}

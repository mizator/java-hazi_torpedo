package asd;


import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * TCP kliens halozati kapcsolatot megvalosito osztaly. 
 */
public class TcpClient extends Network {
	
	/**
	 * Halozati kapcsolathoz socket.
	 */
	private Socket socket = null;
	
	/**
	 * Kiemneti stream.
	 */
	private ObjectOutputStream out = null;
	
	/**
	 * Bemeneti stream.
	 */
	private ObjectInputStream in = null;

	/**
	 * A jatekLogika objektum, aminek az erkezo uzeneteket at kell adni.
	 */
	private JatekLogika jateklogika;
	
	/**
	 * TCP kliens letrehozasa
	 * @param jatekLogika aminek az erkezo uzeneteket at kell adni.
	 */
	public TcpClient(JatekLogika jatekLogika)
	{
		jateklogika = jatekLogika;
	}
	
	/**
	 * Fogado szal, blokkolodik, ameddig nem jon uzenet.
	 * Ha jon uzenet, ertesite a jateklogikat. 
	 */
	private class ReceiverThread implements Runnable {

		public void run() {
			System.out.println("Waiting for points...");
			try {
				// vegtelen ciklus, amiben varunk uj bejovo uzenetre, ha van, akkor atadjuk a jateklogikanak
				while (true) {
					Message received = (Message) in.readObject();
					jateklogika.msgFromNetwork(received);
				}
			} catch (Exception ex) {
				// ha baj van konzolra kiirjuk TODO: usert ertesiteni
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
				// falhasznalo ertesitese
				jateklogika.netError("Server disconnected!");
			} finally {
				// ha baj van vegul lekapcsolodunk
				disconnect();
			}
		}
	}

	/**
	 * Kapcsolodas egy szerverhez.
	 * @param ip Ehhez probalunk kapcsolodni.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	void connect(String ip) throws UnknownHostException, IOException {
		//ha esetleg kapcsoldva voltunk, lekapcsolodunk 
		disconnect();
		
			// letrehozzuk a halozati socket-et es a be-kimeneti stream-ot 
			socket = new Socket(ip, 10007); 

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			// letrehozzuk a bejovo adatokra varakozo szalat, es elunditjuk
			Thread rec = new Thread(new ReceiverThread());
			rec.start();
	}

	/**
	 * Uzenet kuldese a tavoli felnek.
	 */
	@Override
	void send(Message p) {
		// ha nincs kimeneti stream, nem csinalunk mast, csak visszaterunk
		if (out == null)
			return;
		System.out.println("Sending point: " + p + " to Server");
		try {
			out.writeObject(p); // elkuldjuk az uzenetet
			out.flush(); // kenyzeritjuk az elkuldest, ne alljon esetleges pufferekbe
		} catch (IOException ex) {
			System.err.println("Send error."); // ha hiba van, konzolra kiirjuk, TODO: user ertesitese
			// falhasznalo ertesitese
			jateklogika.netError("Send error.");

		}
	}

	/**
	 * Kapcsolat bontasa.
	 */
	@Override
	void disconnect() {
		// lezerjuk a be- es kimeneti stream-eket, es a halozati socket-et 
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}
}

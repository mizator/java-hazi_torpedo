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
				while (true) {
					Message received = (Message) in.readObject();
					jateklogika.msgFromNetwork(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally {
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
		disconnect();
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
	}

	/**
	 * Uzenet kuldese a tavoli felnek.
	 */
	@Override
	void send(Message p) {
		if (out == null)
			return;
		System.out.println("Sending point: " + p + " to Server");
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
		}
	}

	/**
	 * Kapcsolat bontasa.
	 */
	@Override
	void disconnect() {
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

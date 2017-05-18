package asd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP szerver halozati kapcsolatot megvalosito osztaly. 
 */
public class TcpServer extends Network {

	/**
	 * Halozati kapcsolathoz szerver socket.
	 */
	private ServerSocket serverSocket = null;
	
	/**
	 * A hozzank kapcsolodott klienshez socket.
	 */
	private Socket clientSocket = null;
	
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
	 * TCP szerver letrehozasa
	 * @param jatekLogika aminek az erkezo uzeneteket at kell adni.
	 */
	public TcpServer(JatekLogika jatekLogika)
	{
		jateklogika = jatekLogika;
	}
	
	/**
	 * Fogado szal, blokkolodik, ameddig nem jon uzenet.
	 * Ha jon uzenet, ertesite a jateklogikat. 
	 */
	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e) {
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				while (true) {
					Message received = (Message) in.readObject();
					jateklogika.msgFromNetwork(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	/**
	 * Szerver kapcsolat eliditasa. Figyeles egy adott porton.
	 * @throws IOException
	 */
	void listen() throws IOException {
		disconnect();
			serverSocket = new ServerSocket(10007);

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
		System.out.println("Sending point: " + p + " to Client");
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
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}
}

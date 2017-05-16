package asd;


import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class TcpClient extends Network {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	
	private JatekLogika jateklogika;
	
	public TcpClient(JatekLogika jatekLogika)
	{
		jateklogika = jatekLogika;
	}
	
/*	SerialClient(Control c) {
		super(c);
	}*/

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

	void connect(String ip) throws UnknownHostException, IOException {
		disconnect();
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
	}

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

	void disconnect() {}
	
/*	@Override
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
	}*/
}

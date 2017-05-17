package asd;

import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import asd.Message.Tipus;

public class JatekLogika {
	
	private Network network;
	private boolean enJovok;
	
	public void ujJatek()
	{
		ConnectTypeDialog connecTypeDialog = new ConnectTypeDialog();
		try
		{
			connecTypeDialog.setServerAddrHint(InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e1) {} // nem baj ha nincs segitseg
		
		boolean b = connecTypeDialog.exec(); 
		
		if (b)
		{
			if (connecTypeDialog.getServerIsSelected())
			{
				try
				{
					TcpServer server = new TcpServer(this);
					server.listen();
					network = server;
					enJovok = true;
				}
			    catch (IOException e)
				{
			    	b = false;
					JOptionPane.showMessageDialog(null, e.getMessage(), "Kapcsolodas sikertelen", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				try
				{
					TcpClient client = new TcpClient(this);
					client.connect(connecTypeDialog.getConnectAddr());
					network = client;
					enJovok = false;
				}
			    catch (IOException e)
				{
			    	b = false;
					JOptionPane.showMessageDialog(null, e.getMessage(), "Kapcsolodas sikertelen", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			if (b)
			{
				TeddLeAHajokat teddleahajokat = new TeddLeAHajokat();
				if (teddleahajokat.exec())
				{
					/*delHajo(torpedopanel, hajok);
					for (HajoPanel h : a.getHajok())
					{
						System.out.println(h.getCellPos().toString());
						addHajo(torpedopanel, h.getCellcount(), h.getCellPos().x, h.getCellPos().y, h.getRotated(), false);
					}
					torpedopanel.repaint();*/
				}
			}
		}
	}
	
	public void msgFromNetwork(Message msg)
	{
		if (!enJovok)
		{
			
		}
		else
		{
			System.out.println("valaki csalni akar...");
		}
	}
	
	public void loves(Point loc)
	{
		Message msg = new Message();
		msg.tipus = Tipus.Loves;
		msg.loc = loc;
		network.send(msg);
	}
	

}

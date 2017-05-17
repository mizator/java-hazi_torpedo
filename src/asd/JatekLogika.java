package asd;

import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import asd.Message.Tipus;
import asd.TorpedoPanel2.CellaTipus;

public class JatekLogika {
	
	private Network network = null;
	private boolean enJovok;
	
	private TorpedoPanel2 sajatTabla;
	private TorpedoPanel2 ellenfelTabla;
	
	private void toggleEnJovok()
	{
		enJovok = ! enJovok;
		ellenfelTabla.setEnabled(enJovok);
	}

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
					sajatTabla.setEnabled(false);
					ellenfelTabla.setEnabled(enJovok);

					sajatTabla.clearHajok();
					sajatTabla.clearCellak();
					ellenfelTabla.clearCellak();
					ellenfelTabla.clearHajok();
					
					for (HajoPanel h : teddleahajokat.getHajok())
					{
						sajatTabla.addHajo(new Hajo(h.getCellPos(), h.getCellcount(), h.getRotated()));
					}
				}
				else
				{
					network.disconnect();
					b = false;
				}
			}
		}
	}
	
	public void setTablak(TorpedoPanel2 sajat, TorpedoPanel2 ellenfel)
	{
		sajatTabla = sajat;
		ellenfelTabla = ellenfel;
	}
	
	public void msgFromNetwork(Message msg)
	{
		if (!enJovok)
		{
			if (msg.tipus == Tipus.Loves)
			{
				toggleEnJovok();
				
				boolean talalt = false;
				for(Hajo hajo: sajatTabla.getHajok())
				{
					if (hajo.getRectangle().contains(msg.loc))
					{
						talalt = true;
						hajo.meglottek();
						
						if (hajo.getSullyedt())
						{
							//todo valasz sullyedt
						}
						else
						{
						}
					}
				}
				
				if (talalt)
				{
					sajatTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.Talat);
					network.send(new Message(Tipus.Talat, msg.loc));
				}
				else
				{
					sajatTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.NemTalalt);
					network.send(new Message(Tipus.NemTalalt, msg.loc));
				}
			}
			else if (msg.tipus == Tipus.Talat)
			{
				ellenfelTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.Talat);				
			}
			else if (msg.tipus == Tipus.NemTalalt)
			{
				ellenfelTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.NemTalalt);				
			}
		}
		else
		{
			System.out.println("valaki csalni akar...");
		}
	}
	
	public void loves(Point loc)
	{
		if (network != null)
		{
			if (enJovok)
			{
				toggleEnJovok();
				ellenfelTabla.setCella(loc.x, loc.y, CellaTipus.Loves);
				network.send(new Message(Tipus.Loves, loc));
			}
			else
			{
				System.out.println("valaki csalni akar 2...");
			}
		}
	}
	

}

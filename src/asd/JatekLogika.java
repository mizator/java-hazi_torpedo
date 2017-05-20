package asd;

import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import asd.Message.Tipus;
import asd.TorpedoPanel2.CellaTipus;

/**
 * A jatek logikajat megvalosito, a jatek szabalyait ellenorzo osztaly.
 *
 */
public class JatekLogika {
	
	/**
	 * A halozati kapcsolatot kezelo inteface.
	 * @see TcpClient
	 * @see TcpServer
	 */
	private Network network = null;
	
	/**
	 * Az itteni jatekose-e a kovetkezo lepes. A szerver kezd.
	 */
	private boolean enJovok;
	
	/**
	 * Az itteni jatekos hajoit abrazolo jatekter.
	 */
	private TorpedoPanel2 sajatTabla;
	/**
	 * Az ellenfel ellen iranyulo loveskeet / talalatokat abrazolo jatekter.
	 */
	private TorpedoPanel2 ellenfelTabla;
	
	/**
	 * Megvaltoztatja, hogy kie a kovetkezo lepes.
	 */
	private void toggleEnJovok()
	{
		enJovok = ! enJovok;
		ellenfelTabla.setEnabled(enJovok);
	}

	/**
	 * Ujrainicializalja a jatekot, uj halozati kapcsolatot hoz letre,
	 * elhelyezteti a jatekossal a hajoit.
	 */
	public void ujJatek()
	{	
		// user megkerdezese szerver vagy kliens akar lenni
		ConnectTypeDialog connecTypeDialog = new ConnectTypeDialog();
		try
		{
			connecTypeDialog.setServerAddrHint(InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e1) {} // nem baj ha nincs segitseg
		
		boolean b = connecTypeDialog.exec(); 
		
		if (b) //ha OK-ra nyomott
		{
			if (connecTypeDialog.getServerIsSelected())
			{
				try
				{
					// ha szervert valasztott megprobalunk egy TCP szerver-t inditani, beallitani, hogy mi jovunk
					TcpServer server = new TcpServer(this);
					server.listen();
					network = server;
					enJovok = true;
				}
			    catch (IOException e)
				{
					// ha nem sikerult, akkor felugro ablakkal jelezzuk az usernek
			    	b = false;
					JOptionPane.showMessageDialog(null, e.getMessage(), "Kapcsolodas sikertelen", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				try
				{
					// ha klienst valasztott megprobalunk egy TCP klienst-t inditani es csatlakozni a szerverhez, beallitani, hogy mi jovunk
					TcpClient client = new TcpClient(this);
					client.connect(connecTypeDialog.getConnectAddr());
					network = client;
					enJovok = false;
				}
			    catch (IOException e)
				{
					// ha nem sikerult, akkor felugro ablakkal jelezzuk az usernek
			    	b = false;
					JOptionPane.showMessageDialog(null, e.getMessage(), "Kapcsolodas sikertelen", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			// ha sikerult a halozati kapcsolat
			if (b)
			{
				// feldobjuk a hejokat elhejezo ablakot
				TeddLeAHajokat teddleahajokat = new TeddLeAHajokat();
				if (teddleahajokat.exec())
				{
					// ha OK-ot nyomott
					sajatTabla.setEnabled(false); // sajat tablat ne piszkalja
					ellenfelTabla.setEnabled(enJovok); // ellenfel tablajat engedelyezzunk ha mi jovunk

					// tablan (elozo jatekbol) levo hajokat es cellak tartalmat toroljuk
					sajatTabla.clearHajok();
					sajatTabla.clearCellak();
					ellenfelTabla.clearCellak();
					ellenfelTabla.clearHajok();
					
					// a felugro ablakon elhelyezett hajoknak megfelelo hajokat hozzaadjuk a sajat jatekterhez
					for (HajoPanel h : teddleahajokat.getHajok())
					{
						sajatTabla.addHajo(new Hajo(h.getCellPos(), h.getCellcount(), h.getRotated()));
					}
				}
				else
				{
					// ha nem OK-ot nyomott, lezarjuk a halozati kapcsolatot
					network.disconnect();
					b = false;
				}
			}
		}
	}
	
	/**
	 * Beallitja a grafikus feluleten megjeleno jatektereket.
	 * @param sajat Az itteni jatekos hajoit abrazolo jatekter.
	 * @param ellenfel Az ellenfel ellen iranyulo loveskeet / talalatokat abrazolo jatekter.
	 */
	public void setTablak(TorpedoPanel2 sajat, TorpedoPanel2 ellenfel)
	{
		sajatTabla = sajat;
		ellenfelTabla = ellenfel;
	}
	
	/**
	 * A halozat felol erkezo uzeneteket kezelo fuggveny.
	 * @param msg az erkezo uzenet
	 */
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
	
	/**
	 * Az itteni jatekostol erkezo loveseket kezelo fuggveny.
	 * @param loc A loves pozicioja.
	 */
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

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
					sajatTabla.setEnabled(false); // sajat tablat ne piszkalja
					ellenfelTabla.setEnabled(false); // ellenfel tablajat tiltjuk, majd ha akliens csatlakozott engedelyezzuk

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
					sajatTabla.setEnabled(false); // sajat tablat ne piszkalja
					ellenfelTabla.setEnabled(enJovok); // ellenfel tablajat engedelyezzunk ha mi jovunk
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
				if (teddleahajokat.exec()) // ha OK-ot nyomott
				{
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
		// csak akkor varunk uzenetet a masik feltol, ha o jon
		if (!enJovok)
		{
			if (msg.tipus == Tipus.Loves) // ha lott rank
			{
				// mar mi jovunk
				toggleEnJovok();
				
				// eltalalta-e barmelyik hajot
				boolean talalt = false;
				for(Hajo hajo: sajatTabla.getHajok())
				{
					if (hajo.getRectangle().contains(msg.loc))
					{
						talalt = true;
						hajo.meglottek();
						
						if (hajo.getSullyedt())
						{
							sajatTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.Talat); // sajat cellan jeloljuk, hogy talalt
							for(int i = 0; i < hajo.cellsize; i++)
							{
								if (hajo.isRotated)
								{
									// elkuldjuk a hajo osszes koordinatajara, hogy TalaltSullyedt, vizszintes a hajo -> .x+i
									network.send(new Message(Tipus.TalaltSullyedt, new Point(hajo.pos.x + i, hajo.pos.y)));
								}
								else
								{
									// elkuldjuk a hajo osszes koordinatajara, hogy TalaltSullyedt, fuggoleges a hajo -> .y+i
									network.send(new Message(Tipus.TalaltSullyedt, new Point(hajo.pos.x, hajo.pos.y + i)));
								}
							}
						}
						else
						{
							sajatTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.Talat); // sajat cellan jeloljuk, hogy talalt
							network.send(new Message(Tipus.Talat, msg.loc)); // elkuljuk, hogy talalt
						}
					}
				}
				
				// megvaltoztatjuk a cellat es valaszolunk
				if (!talalt)
				{
					sajatTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.NemTalalt);
					network.send(new Message(Tipus.NemTalalt, msg.loc));
				}
				
				// minden hajo sullyedt?
				boolean nyertel = true;
				for(Hajo hajo: sajatTabla.getHajok())
				{
					nyertel = nyertel && hajo.getSullyedt();
				}
				if (nyertel) // ha osszes hajo elsullyedt
				{
					network.send(new Message(Tipus.Nyertel, new Point(0, 0))); // elkuldjuk a masiknak, hogy nyert
					JOptionPane.showMessageDialog(null, "Vesztettel", "Vesztettel", JOptionPane.INFORMATION_MESSAGE); // ertesitjuk a felhasznalot, hogy vesztett
					jatekvege();
				}

			}
			else if (msg.tipus == Tipus.Talat) // ha a mi lovesunk talalt, megvaltoztatjuk a cellat
			{
				ellenfelTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.Talat);				
			}
			else if (msg.tipus == Tipus.NemTalalt) // ha a mi lovesunk nem talalt, megvaltoztatjuk a cellat
			{
				ellenfelTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.NemTalalt);				
			}
			else if (msg.tipus == Tipus.TalaltSullyedt) // ha talalt es sullyedt, megvaltoztatjuk a cellat
			{
				ellenfelTabla.setCella(msg.loc.x, msg.loc.y, CellaTipus.TalaltSullyedt);				
			}
			else if (msg.tipus == Tipus.Nyertel) // ha nyertunk
			{
				JOptionPane.showMessageDialog(null, "Nyertel", "Nyertel", JOptionPane.INFORMATION_MESSAGE); // ertesitjuk a felhasznalot, hogy nyert
				jatekvege();
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
		if (network != null) // ha kapcsoldtunk mar valahova
		{
			if (enJovok) // ha mi jovunk
			{
				toggleEnJovok(); // mar nem mi jovunk
				ellenfelTabla.setCella(loc.x, loc.y, CellaTipus.Loves); // megvaltoztatjuk a cellat
				network.send(new Message(Tipus.Loves, loc)); // elkuldjuk a lovest
			}
			else
			{
				System.out.println("valaki csalni akar 2...");
			}
		}
	}
	
	/**
	 * Kapcsolat hiba eseten, felugro ablakot mutat a felhasznalonak
	 * @param msg megjelenitendo uzenet
	 */
	public void netError(String msg)
	{
		// hibauznet megjelenitese
		JOptionPane.showMessageDialog(null, msg, "Kapcsolodas sikertelen", JOptionPane.ERROR_MESSAGE);
		jatekvege();
	}
	
	/**
	 * TCP szerver hivja meg, ha kapcsolodott a kliens
	 */
	public void clientConnected()
	{
		ellenfelTabla.setEnabled(enJovok); // ellenfel tablajat engedelyezzunk	
	}
	
	/**
	 *  Lezarja a jatekot, jatekmezoket tiltja, halozatrol lakapcsolodik
	 */
	private void jatekvege()
	{
		// jatekmezok tiltasa
		sajatTabla.setEnabled(false);
		ellenfelTabla.setEnabled(false);
		
		// kapcsolat bontasa
		if (network != null)
			network.disconnect();
		network = null;
	}

}

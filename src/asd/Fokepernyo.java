package asd;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.Console;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;

public class Fokepernyo {

	private JFrame frmTorped;
	
	private JatekLogika jateklogika = new JatekLogika();

	private ArrayList<HajoPanel> hajok = new ArrayList<HajoPanel>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fokepernyo window = new Fokepernyo();
					window.frmTorped.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fokepernyo() {
		initialize();
	}
	
	private void addHajo(TorpedoPanel torpedopanel, int cellcount, int locx, int locy, boolean rot, boolean moveable) {
		HajoPanel hajo = new HajoPanel(torpedopanel, cellcount, moveable);
		hajo.setRotated(rot);
		hajo.setLoc(locx*torpedopanel.getMeret()+torpedopanel.getOffset(), locy*torpedopanel.getMeret()+torpedopanel.getOffset());
		hajok.add(hajo);
		torpedopanel.add(hajo);
		hajo.setVisible(true);
	}
	
	private void delHajo(TorpedoPanel torpedopanel, ArrayList<HajoPanel> hajok)
	{
		for (HajoPanel hajo : hajok)
		{
			torpedopanel.remove(hajo);
		}
		hajok.clear();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTorped = new JFrame();
		frmTorped.setTitle("Torpedó");
		frmTorped.setBounds(100, 100, 1041, 571);
		frmTorped.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								frmTorped.getContentPane().setLayout(new MigLayout("", "[200px,grow,fill][200px,grow,fill]", "[35px][200px,grow,fill]"));
								
								JPanel panel_1 = new JPanel();
								frmTorped.getContentPane().add(panel_1, "cell 0 0,growx,aligny top");
								
								JButton ujJatek = new JButton("Új játék");
								panel_1.add(ujJatek);
								ujJatek.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										jateklogika.ujJatek();
									}
								});
								
								TorpedoPanel2 sajatTabla = new TorpedoPanel2(null);
								frmTorped.getContentPane().add(sajatTabla, "cell 0 1,grow");
								sajatTabla.init();
								
								TorpedoPanel2 ellenfelTabla = new TorpedoPanel2(jateklogika);
								frmTorped.getContentPane().add(ellenfelTabla, "cell 1 1,grow");

								jateklogika.setTablak(sajatTabla, ellenfelTabla);
								sajatTabla.setLayout(null);
								
	}
}

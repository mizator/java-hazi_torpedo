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

/**
 * A jatek fokepernyoja, ezen van a ket jatekter
 */
public class Fokepernyo {

	private JFrame frmTorped;
	
	/**
	 * A jetek logikajat megvalosito objektum
	 */
	private JatekLogika jateklogika = new JatekLogika();

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
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTorped = new JFrame();
		frmTorped.setTitle("Torpedó");
		frmTorped.setBounds(100, 100, 1041, 571);
		frmTorped.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								frmTorped.getContentPane().setLayout(new MigLayout("", "[200px,grow,fill][200px,grow,fill]", "[35px][200px,grow,fill]"));
								frmTorped.getContentPane().setLayout(new MigLayout("", "[250px,grow,fill][250px,grow,fill]", "[][200px,grow,fill]"));
								
								JButton ujJatek = new JButton("Új játék");
								frmTorped.getContentPane().add(ujJatek, "cell 0 0");
								ujJatek.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										jateklogika.ujJatek();
									}
								});
								
								TorpedoPanel2 sajatTabla = new TorpedoPanel2(null);
								frmTorped.getContentPane().add(sajatTabla, "cell 0 1,alignx left,aligny top");
								sajatTabla.init();
								
								TorpedoPanel2 ellenfelTabla = new TorpedoPanel2(jateklogika);
								frmTorped.getContentPane().add(ellenfelTabla, "cell 1 1,alignx left,aligny top");

								jateklogika.setTablak(sajatTabla, ellenfelTabla);
								sajatTabla.setLayout(null);
								
	}
}

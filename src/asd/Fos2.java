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

public class Fos2 {

	private JFrame frame;
	
	private JatekLogika jateklogika = new JatekLogika();

	private ArrayList<HajoPanel> hajok = new ArrayList<HajoPanel>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fos2 window = new Fos2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fos2() {
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
		frame = new JFrame();
		frame.setBounds(100, 100, 678, 626);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 0, 205, 25);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(336, 0, 223, 25);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectTypeDialog fos4 = new ConnectTypeDialog();
				try {
					fos4.setServerAddrHint(InetAddress.getLocalHost().getHostAddress().toString());
				} catch (UnknownHostException e1) {}
				
				boolean asd = fos4.exec();

				System.out.println(new String("ret: ").concat(String.valueOf(asd)));
				System.out.println(new String("server: ").concat(String.valueOf(fos4.getServerIsSelected())));
				System.out.println(new String("addr: ").concat(fos4.getConnectAddr()));
				
				
			}
		});
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(223, 0, 104, 25);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("Fossss2");
				for (HajoPanel hajoPanel : hajok) {
					System.out.println(hajoPanel.getCellPos().toString());
				}
				Message msg = new Message();
				msg.loc = new Point(3, 4);
				//TcpClient net = new TcpClient();
				//net.connect("127.0.0.1");
				//net.send(msg);
			}
		});
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnNewButton_1);
		
		TorpedoPanel2 torpedopanel = new TorpedoPanel2(jateklogika);
		torpedopanel.setBounds(223, 46, 432, 429);
		frame.getContentPane().add(torpedopanel);
		torpedopanel.init();
		
		
		/*
		addHajo(torpedopanel, 4, 0, 0, true); // 1x4/1
		addHajo(torpedopanel, 3, 1, 0, true); // 2x3/1
		addHajo(torpedopanel, 3, 1, 3, true); // 2x3/2
		addHajo(torpedopanel, 2, 2, 0, true); // 3x2/1
		addHajo(torpedopanel, 2, 2, 2, true); // 3x2/2
		addHajo(torpedopanel, 2, 2, 4, true); // 3x2/3
		addHajo(torpedopanel, 1, 3, 0, true); // 4x1/1
		addHajo(torpedopanel, 1, 3, 1, true); // 4x1/2
		addHajo(torpedopanel, 1, 3, 2, true); // 4x1/3
		addHajo(torpedopanel, 1, 3, 3, true); // 4x1/4		
		*/
		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TeddLeAHajokat a = new TeddLeAHajokat();
				if (a.exec())
				{
					/*
					delHajo(torpedopanel, hajok);
					for (HajoPanel h : a.getHajok())
					{
						System.out.println(h.getCellPos().toString());
						addHajo(torpedopanel, h.getCellcount(), h.getCellPos().x, h.getCellPos().y, h.getRotated(), false);
					}*/
					torpedopanel.repaint();
				}
			}
		});
		btnNewButton_2.setBounds(66, 46, 117, 25);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("New button");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jateklogika.ujJatek();
			}
		});
		btnNewButton_3.setBounds(27, 202, 156, 52);
		frame.getContentPane().add(btnNewButton_3);

	}
}

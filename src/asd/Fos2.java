package asd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.Console;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;

public class Fos2 {

	private JFrame frame;

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
				fos4.setServerAddrHint("fossssssss");
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
			}
		});
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnNewButton_1);
		
		TorpedoPanel torpedopanel = new TorpedoPanel();
		torpedopanel.setBounds(66, 96, 432, 429);
		torpedopanel.init();
		
		frame.getContentPane().add(torpedopanel);

	}
}

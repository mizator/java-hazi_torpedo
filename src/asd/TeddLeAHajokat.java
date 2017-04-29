package asd;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TeddLeAHajokat extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private TorpedoPanel torpedopanel;
	private ArrayList<HajoPanel> hajok = new ArrayList<HajoPanel>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TeddLeAHajokat dialog = new TeddLeAHajokat();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addHajo(TorpedoPanel torpedopanel, int cellcount, int locx, int locy, boolean moveable) {
		HajoPanel hajo = new HajoPanel(torpedopanel, cellcount, moveable);
		hajo.setLoc(locx*torpedopanel.getMeret()+torpedopanel.getOffset(), locy*torpedopanel.getMeret()+torpedopanel.getOffset());
		hajok.add(hajo);
		torpedopanel.add(hajo);
		hajo.setVisible(true);
	}
	/**
	 * Create the dialog.
	 */
	public TeddLeAHajokat() {
		setTitle("Helyezd el a hajóidat!");
		setBounds(100, 100, 474, 530);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		torpedopanel = new TorpedoPanel();
		contentPanel.add(torpedopanel);
		{
			JLabel lblHelyezdElA = new JLabel("Helyezd el a hajóidat!");
			lblHelyezdElA.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblHelyezdElA, BorderLayout.NORTH);
		}
		torpedopanel.setBounds(66, 96, 432, 429);
		torpedopanel.setVisible(true);
		torpedopanel.init();
		
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
		}
}

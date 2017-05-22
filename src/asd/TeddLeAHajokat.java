package asd;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalExclusionType;

/**
 * A hajok jatek elotti elhelyezeset vegzo ablak/dialog. 
 */
public class TeddLeAHajokat extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Jatekter, ezen lehet a hajokat helyezgetni.
	 */
	private TorpedoPanel torpedopanel;
	
	/**
	 * A hajokat megjelenito objektumokat tartalmazo lista.
	 */
	private ArrayList<HajoPanel> hajok = new ArrayList<HajoPanel>();
	
	/**
	 * OK-ra kattintottak.
	 */
	private boolean returnValue = false;

	/**
	 * Launch the application.
	 * Tesztelesi celokra.
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

	/**
	 * Hajo megjelenitese.
	 * @param torpedopanel Ezen a jatekteren.
	 * @param cellcount Ezzel a merettel
	 * @param locx Ezen az X pozicion.
	 * @param locy Ezen az Y pozicion.
	 * @param moveable A hajo mozgathato?
	 */
	private void addHajo(TorpedoPanel torpedopanel, int cellcount, int locx, int locy, boolean moveable) {
		HajoPanel hajo = new HajoPanel(torpedopanel, cellcount, moveable);
		hajo.setLoc(locx*torpedopanel.getMeret()+torpedopanel.getOffset(), locy*torpedopanel.getMeret()+torpedopanel.getOffset());
		hajok.add(hajo);
		torpedopanel.add(hajo);
		hajo.setVisible(true);
	}
	
	/**
	 * Ellenorzi, a hajok elhelyezkedeset. 
	 * @return Van olyan ket hajo, amik kozt nincs legalabb 1 cellanyi tavolsag.
	 */
	private boolean hajokUtkoznek()
	{
		boolean ret = false;
		
		for (HajoPanel hajo1 : hajok) 
		{			
			for (HajoPanel hajo2 : hajok)
			{
				if (hajo1 != hajo2)
				{
					Rectangle a = hajo1.getRectangle();
					a.grow(1, 1);
					Rectangle b = hajo2.getRectangle();
					ret |= a.intersects(b);
				}
			}
		}
		return false;//ret;
	}
	
	/**
	 * Megjelenitett hajokat adja vissza.
	 * @return A megjelenitett hajok listaja.
	 */
	public ArrayList<HajoPanel> getHajok()
	{
		return hajok;
	}

	/**
	 * A dialog elinditasa, az ablak megjelenitese.
	 * @return A felhasznalo az OK gombra kattintott es nincs utkozo hajo.
	 */
	public boolean exec() {
		setVisible(true);
		return returnValue;
	}
	
	/**
	 * Create the dialog.
	 */
	public TeddLeAHajokat() {
		setModal(true);
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
				okButton.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent arg0) {
						if (hajokUtkoznek())
						{
							JOptionPane.showMessageDialog(null, "A hajók nem érhetnek egymáshoz!", "Hiba", JOptionPane.ERROR_MESSAGE);
							returnValue = false;
						}
						else
						{
							returnValue = true;
							setVisible(false);
						}
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						returnValue = false;
						setVisible(false);
					}
				});
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

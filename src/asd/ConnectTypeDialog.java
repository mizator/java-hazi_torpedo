package asd;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;

public class ConnectTypeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField connectAddr;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnSzerver;
	private JRadioButton rdbtnKliens;
	private JLabel serverAddr;
	private boolean returnValue = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConnectTypeDialog dialog = new ConnectTypeDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConnectTypeDialog() {
		setTitle("Torpedó - Kapcsolódás módja");
		setModal(true);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setBounds(100, 100, 452, 155);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{79, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 23, 23, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Kapcsolódás");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.gridwidth = 2;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			rdbtnSzerver = new JRadioButton("Szerver");
			rdbtnSzerver.setSelected(true);
			buttonGroup.add(rdbtnSzerver);
			GridBagConstraints gbc_rdbtnSzerver = new GridBagConstraints();
			gbc_rdbtnSzerver.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnSzerver.gridx = 0;
			gbc_rdbtnSzerver.gridy = 1;
			contentPanel.add(rdbtnSzerver, gbc_rdbtnSzerver);
		}
		{
			rdbtnKliens = new JRadioButton("Kliens");
			rdbtnKliens.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					connectAddr.setEnabled(rdbtnKliens.isSelected());
				}
			});
			{
				serverAddr = new JLabel("<ip>");
				GridBagConstraints gbc_serverAddr = new GridBagConstraints();
				gbc_serverAddr.anchor = GridBagConstraints.WEST;
				gbc_serverAddr.insets = new Insets(0, 0, 5, 0);
				gbc_serverAddr.gridx = 1;
				gbc_serverAddr.gridy = 1;
				contentPanel.add(serverAddr, gbc_serverAddr);
			}
			buttonGroup.add(rdbtnKliens);
			GridBagConstraints gbc_rdbtnKliens = new GridBagConstraints();
			gbc_rdbtnKliens.insets = new Insets(0, 0, 0, 5);
			gbc_rdbtnKliens.anchor = GridBagConstraints.WEST;
			gbc_rdbtnKliens.gridx = 0;
			gbc_rdbtnKliens.gridy = 2;
			contentPanel.add(rdbtnKliens, gbc_rdbtnKliens);
		}
		{
			connectAddr = new JTextField();
			connectAddr.setEnabled(false);
			GridBagConstraints gbc_connectAddr = new GridBagConstraints();
			gbc_connectAddr.fill = GridBagConstraints.HORIZONTAL;
			gbc_connectAddr.gridx = 1;
			gbc_connectAddr.gridy = 2;
			contentPanel.add(connectAddr, gbc_connectAddr);
			connectAddr.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnValue = true;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnValue = false;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public boolean exec() {
		setVisible(true);
		return returnValue;
	}

	public boolean getServerIsSelected() {
		return rdbtnSzerver.isSelected();
	}

	public String getConnectAddr() {
		return connectAddr.getText();
	}

	public void setServerAddrHint(String s) {
		serverAddr.setText(s);
	}
}

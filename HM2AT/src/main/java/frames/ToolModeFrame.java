package frames;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ToolModeFrame extends JFrame {
	private HelpDialog helpPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToolModeFrame frame = new ToolModeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ToolModeFrame() {
		String title="HM"+"\u00B2"+"AT - Home";
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IconHM2AT.png")));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 573, 477);
		setLocationRelativeTo(null);
		String msg="<p align=\"justify\">The Holistic Meta-Model for Adaptivity presents a flexible and comprehensive conceptualization of the components and mechanisms participating in systems with adaptive behavior. The holistic approach is based on simple and clear reasoning that allows the adaptivity designer to follow each step of the process, while allowing inclusive work with legacy systems in the process of building adaptive systems.</p><br>"
				+ "<p align=\"justify\"> As it is a Meta-Model it needs to be instantiated for its use. An instance of the Meta-Model is a model (a particular approach, a view) of adaptivity. This model is generic, in the sense that it represents a type, a category, a family of systems under its particular vision. It has yet to be implemented for its use in a specific context.</p><br>"
				+ "<p align=\"justify\">This tool provides the following two working modes: </p>";

		msg="<html>"+msg+"</html>";
		JLabel lblNewLabel = new JLabel(msg);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JRadioButton rdbtnGeneric = new JRadioButton();
		rdbtnGeneric.setFont(new Font("Tahoma", Font.PLAIN, 10));
		rdbtnGeneric.setSelected(true);
		rdbtnGeneric.setText("<html>Access a <b>generic</b> instance of the Holistic Meta-model.</html>");
		JRadioButton rdbtnSpecific = new JRadioButton("<html>Configure a more <b>specific</b> instance of the Meta-model for a particular system.</html>");
		rdbtnSpecific.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JButton btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnGeneric.isSelected()) {
				    GenericModeFrame genericFrame=new GenericModeFrame();
				    genericFrame.setVisible(true);
				    genericFrame.setLocationRelativeTo(null);
				} 
				else {
				    SpecificModeFrame specificFrame=new SpecificModeFrame();
				    specificFrame.setVisible(true);
				    specificFrame.setLocationRelativeTo(null);		
				}
				
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		ButtonGroup group=new ButtonGroup();
		group.add(rdbtnGeneric);
		group.add(rdbtnSpecific);

		
		JButton btnHelpSpecific = new JButton(" ");
		btnHelpSpecific.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpPanel=new HelpDialog();
				helpPanel.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				helpPanel.displayMessage("specific");
				helpPanel.setVisible(true);
			}
		});
		
		btnHelpSpecific.putClientProperty("JButton.buttonType", "help");
		
		JButton btnHelpGeneric = new JButton(" ");
		btnHelpGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpPanel=new HelpDialog();
				helpPanel.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				helpPanel.displayMessage("generic");
				helpPanel.setVisible(true);
			}
		});
		btnHelpGeneric.putClientProperty("JButton.buttonType", "help");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 502, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 335, Short.MAX_VALUE)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnContinue)
							.addGap(33))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(54)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnGeneric, GroupLayout.PREFERRED_SIZE, 357, GroupLayout.PREFERRED_SIZE)
						.addComponent(rdbtnSpecific))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnHelpGeneric)
						.addComponent(btnHelpSpecific))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel)
					.addGap(41)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnGeneric)
						.addComponent(btnHelpGeneric))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnSpecific)
						.addComponent(btnHelpSpecific))
					.addPreferredGap(ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnContinue))
					.addGap(22))
		);
		getContentPane().setLayout(groupLayout);
	}
}

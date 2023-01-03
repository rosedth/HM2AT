package frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class HelpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String msg;
	private JLabel lblHelpMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HelpDialog dialog = new HelpDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HelpDialog() {
		String title = "HM" + "\u00B2" + "AT " + " - Help";
		setTitle(title);
		setModal(true);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IconHM2AT.png")));
		setBounds(100, 100, 450, 216);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		ImageIcon imgHelp = new ImageIcon(this.getClass().getResource("/question1.png"));
		JLabel lblHelpIcon = new JLabel("");
		lblHelpIcon.setIcon(imgHelp);
		lblHelpMessage = new JLabel(" ");
		lblHelpMessage.setFont(new Font("Tahoma", Font.PLAIN, 9));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblHelpIcon)
					.addGap(18)
					.addComponent(lblHelpMessage, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblHelpMessage, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHelpIcon))
					.addContainerGap(112, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	public void displayMessage(String type) {
		switch (type.toLowerCase()) {
		case "generic":
			msg = "<html>\r\n"
					+ "<p align=\"justify\"><b>Generic Mode on HM<sup>2</sup>AT</b></p><br>\r\n"
					+ "<p align=\"justify\">Through this mode, the user can access resources related to generic (abstract) instances of the holistic meta-model of adaptivity, such as model specifications, examples, and libraries.</p>\r\n"
					+ "</html>";
			break;
		case "specific":
			msg = "<html>\r\n" + "<p align=\"justify\"><b>Specific Mode on HM<sup>2</sup>AT</b></p><br>\r\n"
					+ "<p align=\"justify\">Through this mode, it is possible to specialize an instance of the holistic model to design an adaptive mechanism suited for a concrete underlying adaptable device submitted by the user.</p>\r\n"
					+ "</html>";
			break;
		}
		lblHelpMessage.setText(msg);
	}
}

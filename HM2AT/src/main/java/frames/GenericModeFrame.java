package frames;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import utils.FileManager;

import javax.swing.border.EtchedBorder;

public class GenericModeFrame extends JFrame {
	final static String EXAMPLEPANEL = "Examples";
	final static String DEPENDENCYPANEL = "Dependency";
	final static String CODEPANEL = "Code";

	// booleans to control visibility on combo boxes
	private boolean selectedModel=true;
	private boolean selectedLangModel;
	private boolean paradigm;
	private boolean language;
	private boolean type;
	
	private JPanel panelCoding;
	private JEditorPane txtAreaDependency;
	private String dependencyData=""; 

	private boolean exampleName;
	private boolean exampleLang;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenericModeFrame frame = new GenericModeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public GenericModeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 579, 615);
		String title = "HM" + "\u00B2" + "AT " + " - Generic Mode";
		setTitle(title);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IconHM2AT.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		/**
		 * GENERAL BUTTONS
		 */
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GenericModeFrame.this.dispose();
			}
		});

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		
		/**
		 * MODELING PANEL
		 */

		JPanel panelModeling = new JPanel();
		panelModeling.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), " Model ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JButton btnModelingDownload = new JButton("Download");
		btnModelingDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveModelDialog();
			}
		});
		btnModelingDownload.setEnabled(false);
		btnModelingDownload.setFont(new Font("Arial", Font.PLAIN, 10));

		JLabel lblModelLanguage = new JLabel("Language");
		JComboBox cbModelLanguage = new JComboBox(populateModelLanguage());
		cbModelLanguage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbModelLanguage) {
					if (cbModelLanguage.getSelectedIndex() != 0) {
						selectedLangModel=true;
					} else {
						selectedLangModel=false;
					}
					btnModelingDownload.setEnabled(selectedModel&&selectedLangModel);
					//setEnabledAtPanel(panelCoding, selectedModel&&selectedLangModel);

				}
			}
		});

		JLabel lblModelInstance = new JLabel("Name");
		JComboBox cbModelInstance = new JComboBox(populateModelInstance());
		cbModelInstance.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbModelInstance.getSelectedIndex()!=-1) {
					selectedModel=true;
				}else {
					selectedModel=false;
				}
				btnModelingDownload.setEnabled(selectedModel&&selectedLangModel);
				//setEnabledAtPanel(panelCoding, selectedModel&&selectedLangModel);
			}
		});
		
		JLabel lblModelApproach = new JLabel("Approach");
		JComboBox cbModelApproach = new JComboBox(populateModelApproach());

		GroupLayout gl_panelModeling = new GroupLayout(panelModeling);
		gl_panelModeling.setHorizontalGroup(
			gl_panelModeling.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelModeling.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panelModeling.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelModeling.createSequentialGroup()
							.addGroup(gl_panelModeling.createParallelGroup(Alignment.LEADING)
								.addComponent(lblModelInstance, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelModeling.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblModelApproach)
									.addComponent(lblModelLanguage)))
							.addGap(27)
							.addGroup(gl_panelModeling.createParallelGroup(Alignment.TRAILING)
								.addComponent(cbModelInstance, 0, 155, Short.MAX_VALUE)
								.addComponent(cbModelLanguage, Alignment.LEADING, 0, 155, Short.MAX_VALUE)
								.addComponent(cbModelApproach, 0, 155, Short.MAX_VALUE))
							.addGap(266))
						.addGroup(gl_panelModeling.createSequentialGroup()
							.addComponent(btnModelingDownload, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		gl_panelModeling.setVerticalGroup(
			gl_panelModeling.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelModeling.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_panelModeling.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblModelInstance)
						.addComponent(cbModelInstance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelModeling.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblModelLanguage)
						.addComponent(cbModelLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelModeling.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelModeling.createSequentialGroup()
							.addGap(47)
							.addComponent(btnModelingDownload))
						.addGroup(gl_panelModeling.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblModelApproach)
							.addComponent(cbModelApproach, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		panelModeling.setLayout(gl_panelModeling);

		
		
		/**
		 * CODING PANEL
		 */

		panelCoding = new JPanel();
		panelCoding.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), " Implementation ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		GroupLayout gl_panelCoding = new GroupLayout(contentPane);
		gl_panelCoding.setHorizontalGroup(
			gl_panelCoding.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCoding.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelCoding.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelCoding.createSequentialGroup()
							.addComponent(panelModeling, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelCoding.createSequentialGroup()
							.addComponent(panelCoding, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelCoding.createSequentialGroup()
							.addComponent(btnBack)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExit)
							.addGap(17))))
		);
		gl_panelCoding.setVerticalGroup(
			gl_panelCoding.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelCoding.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelModeling, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(panelCoding, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addGroup(gl_panelCoding.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBack)
						.addComponent(btnExit))
					.addContainerGap())
		);

		

		panelCoding.setLayout(new CardLayout(0, 0));
		JTabbedPane tabbedCodingPane = new JTabbedPane(JTabbedPane.TOP);
		panelCoding.add(tabbedCodingPane, "tabbedCodingPane");
		
		
		/**
		 * "CODE" TAB
		 */
		
		JPanel tabCodePanel = new JPanel();
		tabbedCodingPane.addTab(CODEPANEL, null, tabCodePanel, null);

		JButton btnCodeDownload = new JButton("Download");
		btnCodeDownload.setFont(new Font("Arial", Font.PLAIN, 10));
		btnCodeDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveCodeDialog();
			}
		});
		btnCodeDownload.setEnabled(false);

		JLabel lblCodeLanguage = new JLabel("Language");
		JComboBox cbCodeLanguage = new JComboBox(populateCodeLanguage());
		cbCodeLanguage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbCodeLanguage) {
					if (cbCodeLanguage.getSelectedIndex() != 0) {
						language = true;
					} else {
						language = false;
					}

				}
				btnCodeDownload.setEnabled(checkCompleteOptions(paradigm, language, type));
			}

		});

		JLabel lblCodeParadigm = new JLabel("Paradigm");
		JComboBox cbCodeParadigm = new JComboBox(populateCodeParadigm());
		cbCodeParadigm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbCodeParadigm) {
					if (cbCodeParadigm.getSelectedIndex() != 0) {
						paradigm = true;
					} else {
						paradigm = false;
					}

				}
				btnCodeDownload.setEnabled(checkCompleteOptions(paradigm, language, type));
			}
		});

		JLabel lblCodeTypeFile = new JLabel("Type of file");
		JComboBox cbCodeTypeFile = new JComboBox(populateCodeTypeFile());
		cbCodeTypeFile.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbCodeTypeFile) {
					if (cbCodeTypeFile.getSelectedIndex() != 0) {
						type = true;
					} else {
						type = false;
					}

				}
				btnCodeDownload.setEnabled(checkCompleteOptions(paradigm, language, type));
			}

		});

		GroupLayout gl_tabCodePanel = new GroupLayout(tabCodePanel);
		gl_tabCodePanel.setHorizontalGroup(
			gl_tabCodePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabCodePanel.createSequentialGroup()
					.addContainerGap(427, Short.MAX_VALUE)
					.addComponent(btnCodeDownload)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_tabCodePanel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_tabCodePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCodeParadigm, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCodeTypeFile)
						.addComponent(lblCodeLanguage, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_tabCodePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(cbCodeLanguage, 0, 156, Short.MAX_VALUE)
						.addComponent(cbCodeParadigm, Alignment.TRAILING, 0, 156, Short.MAX_VALUE)
						.addComponent(cbCodeTypeFile, Alignment.TRAILING, 0, 156, Short.MAX_VALUE))
					.addGap(247))
		);
		gl_tabCodePanel.setVerticalGroup(
			gl_tabCodePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabCodePanel.createSequentialGroup()
					.addContainerGap(50, Short.MAX_VALUE)
					.addGroup(gl_tabCodePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCodeParadigm)
						.addComponent(cbCodeParadigm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_tabCodePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCodeLanguage)
						.addComponent(cbCodeLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_tabCodePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCodeTypeFile)
						.addComponent(cbCodeTypeFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(48)
					.addComponent(btnCodeDownload)
					.addContainerGap())
		);
		tabCodePanel.setLayout(gl_tabCodePanel);
		//setEnabledAtPanel(tabCodePanel, false);
		
		/**
		 *  "DEPENDENCY" TAB
		 */
		JPanel tabDependencyPanel = new JPanel();
		tabbedCodingPane.addTab(DEPENDENCYPANEL, null, tabDependencyPanel, null);

		
		JLabel lblDependencyManager = new JLabel("Dependency Manager");
		JComboBox cbDependencyManager = new JComboBox(populateDependencyManager());
		cbDependencyManager.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch(cbDependencyManager.getSelectedIndex()) {
				case 1: 
					InputStream inputStream = this.getClass().getResourceAsStream("/dependency-POM.xml");
					try {
						dependencyData = readFromPOM(inputStream);
						txtAreaDependency.setText(dependencyData);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case 2:
					break;
					
				default: dependencyData="Dependency not available";
				}
			}
		});
		
		JButton btnDependencyDownload = new JButton("Download");
		btnDependencyDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDependencyDialog();
			}
		});

		JScrollPane dependencyScroller = new JScrollPane();
		dependencyScroller.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

		JButton btnDependencyCopy = new JButton("");
		btnDependencyCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToClipboad(txtAreaDependency.getText());
				JOptionPane.showMessageDialog(null, "Dependency has been copied to clipboard!");
			}
		});
		ImageIcon imgCopy = new ImageIcon(this.getClass().getResource("/copy.png"));
		btnDependencyCopy.setIcon(imgCopy);


		GroupLayout gl_tabDependencyPanel = new GroupLayout(tabDependencyPanel);
		gl_tabDependencyPanel.setHorizontalGroup(
			gl_tabDependencyPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabDependencyPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabDependencyPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(dependencyScroller, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
						.addGroup(gl_tabDependencyPanel.createSequentialGroup()
							.addComponent(lblDependencyManager)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbDependencyManager, 0, 381, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_tabDependencyPanel.createSequentialGroup()
							.addComponent(btnDependencyCopy, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDependencyDownload)))
					.addContainerGap())
		);
		gl_tabDependencyPanel.setVerticalGroup(
			gl_tabDependencyPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabDependencyPanel.createSequentialGroup()
					.addContainerGap(29, Short.MAX_VALUE)
					.addGroup(gl_tabDependencyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDependencyManager)
						.addComponent(cbDependencyManager, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(dependencyScroller, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_tabDependencyPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnDependencyCopy, 0, 0, Short.MAX_VALUE)
						.addComponent(btnDependencyDownload))
					.addContainerGap())
		);
		
		txtAreaDependency = new JEditorPane();
		dependencyScroller.setViewportView(txtAreaDependency);
		txtAreaDependency.setContentType("text/xml");
		txtAreaDependency.setEditable(false);
		txtAreaDependency.setText(dependencyData);
		btnDependencyDownload.setFont(new Font("Arial", Font.PLAIN, 10));
		tabDependencyPanel.setLayout(gl_tabDependencyPanel);

		/**
		 * "EXAMPLE" TAB
		 */
		
		JPanel tabExamplePanel = new JPanel();
		tabbedCodingPane.addTab(EXAMPLEPANEL, null, tabExamplePanel, null);

		JTextArea txtPaneExampleDesc = new JTextArea();
		txtPaneExampleDesc.setEditable(false);
		txtPaneExampleDesc.setEnabled(false);
		txtPaneExampleDesc.setVisible(false);
		txtPaneExampleDesc.setFont(new Font("Tahoma", Font.PLAIN, 9));
		//txtPaneExampleDesc.setDisabledTextColor(new Color(43, 156, 184));
		//txtPaneExampleDesc.setDisabledTextColor(new Color(26, 94, 11));
		txtPaneExampleDesc.setDisabledTextColor(new Color(26, 94, 110));
		txtPaneExampleDesc.setBackground(UIManager.getColor("TextArea.disabledBackground"));
		txtPaneExampleDesc.setWrapStyleWord(true);
		txtPaneExampleDesc.setLineWrap(true);

		String exampleDesc;
		exampleDesc = "Example Description" + "\n"
				+ "The \"Adaptive Automata\" example implements a basic automata that enhances its operation by incorporating an adaptive layer to manage particular events.";
		txtPaneExampleDesc.setText("\r\nThe \"Adaptive Automata\" example implements a basic automata that enhances its operation by incorporating an adaptive layer to manage particular events.");

		JButton btnExampleDownload = new JButton("Download");
		btnExampleDownload.setEnabled(false);
		btnExampleDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveExampleDialog();
			}
		});
		
		btnExampleDownload.setFont(new Font("Arial", Font.PLAIN, 10));

		JLabel lblExampleName = new JLabel("Name");
		JComboBox cbExampleName = new JComboBox(populateExamples());
		cbExampleName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbExampleName) {
					if (cbExampleName.getSelectedIndex() != 0) {
						txtPaneExampleDesc.setVisible(true);
						exampleName = true;
					} else {
						txtPaneExampleDesc.setVisible(false);
						exampleName = false;
					}

				}
				btnExampleDownload.setEnabled(checkCompleteOptions(exampleName, exampleLang, true));
			}
		});

		JLabel lblExampleLanguage = new JLabel("Language");
		JComboBox cbExampleLanguage = new JComboBox(populateCodeLanguage());
		cbExampleLanguage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == cbExampleLanguage) {
					if (cbExampleLanguage.getSelectedIndex() != 0) {
						exampleLang = true;
					} else {
						exampleLang = false;
					}

				}
				btnExampleDownload.setEnabled(checkCompleteOptions(exampleName, exampleLang, true));
			}
		});

		JLabel lblExampleMsgGit = new JLabel();
		lblExampleMsgGit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					Desktop.getDesktop().browse(new URI("https://github.com/rosedth/Meta-Adaptive-FSM.git"));

				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblExampleMsgGit.setText(
				"<html>You can also access all the examples at the <a href=\"https://github.com/rosedth/Meta-Adaptive-FSM.git\">Git Repositories</a></html>");
		lblExampleMsgGit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JLabel lblExampleDescription = new JLabel("Description");
		lblExampleDescription.setVisible(false);

		GroupLayout gl_tabExamplePanel = new GroupLayout(tabExamplePanel);
		gl_tabExamplePanel.setHorizontalGroup(
			gl_tabExamplePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabExamplePanel.createSequentialGroup()
					.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_tabExamplePanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblExampleDescription, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblExampleName)
								.addComponent(lblExampleLanguage, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtPaneExampleDesc, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(cbExampleName, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(cbExampleLanguage, Alignment.LEADING, 0, 387, Short.MAX_VALUE))))
						.addGroup(gl_tabExamplePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblExampleMsgGit, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnExampleDownload)))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		gl_tabExamplePanel.setVerticalGroup(
			gl_tabExamplePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabExamplePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExampleName)
						.addComponent(cbExampleName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbExampleLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblExampleLanguage))
					.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_tabExamplePanel.createSequentialGroup()
							.addGap(28)
							.addComponent(lblExampleDescription))
						.addGroup(gl_tabExamplePanel.createSequentialGroup()
							.addGap(18)
							.addComponent(txtPaneExampleDesc, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)))
					.addGap(35)
					.addGroup(gl_tabExamplePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExampleMsgGit)
						.addComponent(btnExampleDownload))
					.addGap(100))
		);
		tabExamplePanel.setLayout(gl_tabExamplePanel);
		
		
		contentPane.setLayout(gl_panelCoding);
	}

	/**
	 * METHODS TO POPULATE COMBOXES
	 */

	private String[] populateModelInstance() {
		// TODO Auto-generated method stub
		String[] options = { "[Holistic] Basic", "[O.C.] Basic", "[A.C.] Simple", "[Holistic] 3Comp." };
		return options;
	}

	private String[] populateModelLanguage() {
		// TODO Auto-generated method stub
		String[] options = { "---", "UML", "Other1", "Other2" };
		return options;
	}

	private String[] populateModelApproach() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Holistic","Organic Computing", "Autonomic Computing", "Control Theory" };
		return options;
	}
	
	private String[] populateCodeLanguage() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Java", "C#", "C++", "C" };
		return options;
	}

	private String[] populateCodeParadigm() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Object-oriented", "Service-oriented", "Aspect-oriented", "Declarative", "Other" };
		return options;
	}

	private String[] populateCodeTypeFile() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Source", "Library" };
		return options;
	}

	private String[] populateDependencyManager() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Maven Central (Java)","NuGet (.NET)", "Packagist (PHP)", "PyPI (Python)", "RubyGems (Ruby)", "Other" };
		return options;
	}
	
	private String[] populateExamples() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Adaptive Automata", "Decision Table", "Android Example" };
		return options;
	}

	/**
	 * METHODS TO SHOW DIALOGS
	 */

	private void showSaveModelDialog() {
		JFileChooser fileChooser = new JFileChooser();
		// The extension of the file should be choose according to the modeling language
		fileChooser.setSelectedFile(new File("model.uml"));
		fileChooser.setDialogTitle("Save model as ");
		int response = fileChooser.showSaveDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				FileManager.copyFileNIO("C:\\Users\\rosed\\Downloads\\newBasicModel.uml", selectedFile.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Save as file: " + selectedFile.getAbsolutePath());
		}
	}

	private void showSaveCodeDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Code as ");
		int response = fileChooser.showSaveDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Save as file: " + selectedFile.getAbsolutePath());
		}
	}
	
	

	private void showSaveDependencyDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Dependency as ");
		fileChooser.setSelectedFile(new File("dependency.xml"));
		int response = fileChooser.showSaveDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			//Document doc = convertXMLFileToXMLDocument( xmlFilePath );
			System.out.println("Save as file: " + selectedFile.getAbsolutePath());
		}
	}

	private void showSaveExampleDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Example as ");
		int response = fileChooser.showSaveDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Save as file: " + selectedFile.getAbsolutePath());
		}
	}

	
	/**
	 * UTILS
	 */
	private void copyToClipboad(String str) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(str);
		clipboard.setContents(strSel, null);
	}

	private String readFromPOM(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	private boolean checkCompleteOptions(boolean paradigm, boolean language, boolean type) {
		return paradigm && language && type;
	}
	
	private void setEnabledAtPanel(JPanel panel, boolean visible) {
		for (Component component : panel.getComponents())
		  component.setEnabled(visible);
	}
}

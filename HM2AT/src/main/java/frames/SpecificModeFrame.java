package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import logic.AdaptivityModel;
import logic.AdaptivityModelImplementation;
import logic.UnderlyingDevice;
import utils.ComboPopulator;
import utils.Scriptor;

public class SpecificModeFrame extends JFrame {

	private JPanel contentPane;


	private UnderlyingDevice device;
	private boolean adaptable;

	// Device Panel controls
	private JLabel lblDeviceStatus;
	private JComboBox cbDeviceLang;
	private JComboBox cbDeviceType;
	private JTextField txtDeviceLocation;
	private boolean fileChoosed;
	private boolean typeFileChoosed;
	private JButton btnDeviceVerify;
	
	
	// Adaptive Logic controls
	private JTabbedPane tabbedPaneAdaptiveLayer;
	JComboBox<AdaptivityModel> cbDeviceModel;
	JComboBox cbModelProgLang;
	boolean modelChoosed;
	boolean langChoosed;
	boolean implementationChoosed;
	boolean activateTabbedPanel;
	private JTextField txtEntityName;
	JButton btnLoadModel;
	RSyntaxTextArea txtEditor;
	JButton btnDownload;

	AdaptivityModel selectedModel;
	AdaptivityModelImplementation selectedImplementation;
	private JComboBox<AdaptivityModelImplementation> cbImplementationName;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpecificModeFrame frame = new SpecificModeFrame();
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
	public SpecificModeFrame() {
		device = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 800);
		String title = "HM" + "\u00B2" + "AT " + " - Specific Mode";
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IconHM2AT.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		/**
		 * BUTTONS
		 */
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecificModeFrame.this.dispose();
			}
		});

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		setContentPane(contentPane);

		/**
		 * DEVICE PANEL
		 */

		JPanel panelDevice = new JPanel();
		panelDevice.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Underlying Device", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JLabel lblDeviceLocation = new JLabel("Location");
		txtDeviceLocation = new JTextField();
		txtDeviceLocation.setColumns(10);
		txtDeviceLocation.setEditable(false);

		btnDeviceVerify = new JButton();
		btnDeviceVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VerifyDeviceFrame verifyFrame = new VerifyDeviceFrame(txtDeviceLocation.getText(), cbDeviceLang.getSelectedItem().toString(), cbDeviceType.getSelectedItem().toString(),
						SpecificModeFrame.this);
				verifyFrame.setVisible(true);
				verifyFrame.setLocationRelativeTo(null);
			}
		});
		ImageIcon imgLoad = new ImageIcon(this.getClass().getResource("/load.png"));
		btnDeviceVerify.setIcon(imgLoad);
		btnDeviceVerify.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDeviceVerify.setEnabled(false);
		btnDeviceVerify.setToolTipText("Load device");

		JButton btnDeviceSearch = new JButton("");
		ImageIcon imgSearch = new ImageIcon(this.getClass().getResource("/search.png"));
		btnDeviceSearch.setIcon(imgSearch);
		btnDeviceSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSearchDeviceDialog();
			}
		});

		lblDeviceStatus = new JLabel("   ");
		lblDeviceStatus.setEnabled(false);

		JLabel lblDeviceLang = new JLabel("Language");

		cbDeviceLang = new JComboBox();
		cbDeviceLang.setEnabled(false);
		cbDeviceLang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((cbDeviceLang.getSelectedIndex() != 0) && (cbDeviceType.getSelectedIndex() != 0)) {
					btnDeviceVerify.setEnabled(true);
				} else {
					btnDeviceVerify.setEnabled(false);
				}
			}
		});
		ComboPopulator.populateProgrammingLanguage(cbDeviceLang);

		JLabel lblDeviceType = new JLabel("Type");

		cbDeviceType = new JComboBox();
		cbDeviceType.setEnabled(false);
		ComboPopulator.populateDeviceTypeFile(cbDeviceType);
		cbDeviceType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((cbDeviceLang.getSelectedIndex() != 0) && (cbDeviceType.getSelectedIndex() != 0)) {
					btnDeviceVerify.setEnabled(true);
					
				} else {
					btnDeviceVerify.setEnabled(false);
				}
			}
		});
		GroupLayout gl_panelDevice = new GroupLayout(panelDevice);
		gl_panelDevice.setHorizontalGroup(
			gl_panelDevice.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelDevice.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelDevice.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDeviceLocation, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDeviceLang))
					.addGap(28)
					.addGroup(gl_panelDevice.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelDevice.createSequentialGroup()
							.addGroup(gl_panelDevice.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelDevice.createSequentialGroup()
									.addComponent(lblDeviceStatus, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
									.addGap(32))
								.addComponent(cbDeviceLang, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblDeviceType)
							.addGap(22)
							.addComponent(cbDeviceType, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtDeviceLocation, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelDevice.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDeviceSearch, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDeviceVerify, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panelDevice.setVerticalGroup(
			gl_panelDevice.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDevice.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panelDevice.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelDevice.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblDeviceLocation)
							.addComponent(txtDeviceLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnDeviceSearch))
					.addGap(18)
					.addGroup(gl_panelDevice.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelDevice.createSequentialGroup()
							.addGroup(gl_panelDevice.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDeviceLang)
								.addComponent(cbDeviceLang, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblDeviceStatus, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelDevice.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnDeviceVerify)
							.addGroup(gl_panelDevice.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDeviceType)
								.addComponent(cbDeviceType, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))))
					.addGap(31))
		);
		panelDevice.setLayout(gl_panelDevice);

		/**
		 * ADAPTIVE LAYER PANEL
		 */

		JPanel panelAdaptiveLayer = new JPanel();
		panelAdaptiveLayer.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Adaptive Logic", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		cbDeviceModel = new JComboBox<AdaptivityModel>();
		ComboPopulator.populateModelfromRepository(cbDeviceModel);
		cbDeviceModel.setEnabled(false);
		modelChoosed=true;
		cbDeviceModel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				tabbedPaneAdaptiveLayer.requestFocus();
				modelChoosed=true;
				selectedModel=(AdaptivityModel)cbDeviceModel.getSelectedItem();
				ComboPopulator.populateImplementationfromModel(cbImplementationName,selectedModel.getId());
			}
		});

		// UIManager.put("TabbedPane.selected", Color.red);
		tabbedPaneAdaptiveLayer = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneAdaptiveLayer.setEnabled(adaptable);
		tabbedPaneAdaptiveLayer.putClientProperty("JTabbedPane.tabType", "card");
		tabbedPaneAdaptiveLayer.putClientProperty("JTabbedPane.hasFullBorder", true);

		JPanel tabMonitorPanel = new JPanel();
		tabbedPaneAdaptiveLayer.addTab("Monitor", null, tabMonitorPanel, null);
		txtEditor = new RSyntaxTextArea(17, 70);
		txtEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		txtEditor.setEnabled(false);
		txtEditor.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(txtEditor);
        sp.getTextArea().setFont(new Font("Consolas", Font.PLAIN, 10));
        sp.setEnabled(false);
        sp.getTextArea().setLineWrap(true);
       //tabMonitorPanel.add(sp);
        
		JLabel lblEntityName = new JLabel("Responsible Entity");
		
		txtEntityName = new JTextField();
		txtEntityName.setEnabled(false);
		txtEntityName.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.add(sp);
		GroupLayout gl_tabMonitorPanel = new GroupLayout(tabMonitorPanel);
		gl_tabMonitorPanel.setHorizontalGroup(
			gl_tabMonitorPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabMonitorPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabMonitorPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_tabMonitorPanel.createSequentialGroup()
							.addComponent(lblEntityName)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(txtEntityName, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 495, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		gl_tabMonitorPanel.setVerticalGroup(
			gl_tabMonitorPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabMonitorPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabMonitorPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEntityName)
						.addComponent(txtEntityName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		tabMonitorPanel.setLayout(gl_tabMonitorPanel);

		JPanel tabReasonerPanel = new JPanel();
		tabbedPaneAdaptiveLayer.addTab("Reasoner", null, tabReasonerPanel, null);

		JPanel tabSelectorPanel = new JPanel();
		tabbedPaneAdaptiveLayer.addTab("Selector", null, tabSelectorPanel, null);

		JPanel tabExecutorPanel = new JPanel();
		tabbedPaneAdaptiveLayer.addTab("Executor", null, tabExecutorPanel, null);
		JLabel lblImplementationName = new JLabel("Implementation");

		btnLoadModel = new JButton();
		btnLoadModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadModel(cbDeviceModel.getSelectedItem().toString(),cbModelProgLang.getSelectedItem().toString());
			}
		});
		btnLoadModel.setToolTipText("Generate instance");
		ImageIcon imgInstance = new ImageIcon(this.getClass().getResource("/instance-3.png"));
		btnLoadModel.setIcon(imgInstance);
		btnLoadModel.setEnabled(false);
		
		JLabel lblModelLanguage = new JLabel("Prog. Lang.");

		cbModelProgLang = new JComboBox();
		cbModelProgLang.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(cbModelProgLang.getSelectedIndex()!=0) {
					langChoosed=true;
				}
				btnLoadModel.setEnabled(langChoosed&&modelChoosed);
			}
		});
		ComboPopulator.populateProgrammingLanguage(cbModelProgLang);
		cbModelProgLang.setEnabled(false);
		
		btnDownload = new JButton("Download");
		btnDownload.setEnabled(false);
		
		JLabel lblModelName = new JLabel("Model");
		
		cbImplementationName = new JComboBox<AdaptivityModelImplementation>();
		cbImplementationName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				selectedImplementation=(AdaptivityModelImplementation)cbImplementationName.getSelectedItem();
				implementationChoosed=true;
				cbModelProgLang.setSelectedItem(selectedImplementation.getProgrammingLang());
				btnLoadModel.setEnabled(langChoosed&&modelChoosed&&implementationChoosed);
			}
		});

		// tabbedPaneAdaptiveLayer.setBackgroundAt(0, Color.CYAN);

		GroupLayout gl_panelAdaptiveLayer = new GroupLayout(panelAdaptiveLayer);
		gl_panelAdaptiveLayer.setHorizontalGroup(
			gl_panelAdaptiveLayer.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelAdaptiveLayer.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDownload)
						.addComponent(tabbedPaneAdaptiveLayer, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
						.addGroup(gl_panelAdaptiveLayer.createSequentialGroup()
							.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelAdaptiveLayer.createSequentialGroup()
									.addGap(21)
									.addComponent(lblModelLanguage))
								.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblModelName)
									.addComponent(lblImplementationName)))
							.addGap(18)
							.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.LEADING)
								.addComponent(cbImplementationName, 0, 427, Short.MAX_VALUE)
								.addComponent(cbDeviceModel, 0, 427, Short.MAX_VALUE)
								.addGroup(gl_panelAdaptiveLayer.createSequentialGroup()
									.addComponent(cbModelProgLang, 0, 157, Short.MAX_VALUE)
									.addGap(241)
									.addComponent(btnLoadModel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_panelAdaptiveLayer.setVerticalGroup(
			gl_panelAdaptiveLayer.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelAdaptiveLayer.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbDeviceModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblModelName))
					.addGap(7)
					.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbImplementationName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblImplementationName))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAdaptiveLayer.createParallelGroup(Alignment.BASELINE)
							.addComponent(cbModelProgLang, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblModelLanguage))
						.addComponent(btnLoadModel))
					.addGap(36)
					.addComponent(tabbedPaneAdaptiveLayer, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnDownload)
					.addGap(32))
		);

		panelAdaptiveLayer.setLayout(gl_panelAdaptiveLayer);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelAdaptiveLayer, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelDevice, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelDevice, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panelAdaptiveLayer, GroupLayout.PREFERRED_SIZE, 522, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExit)
						.addComponent(btnBack))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
	}

	protected void loadModel(String string, String string2) {
		// retrieve model from Name
		// retrieve model implementation from <model, language> tuple
		// load the script for specialization of the implementation
		// execute the commands
		
		txtEntityName.setText("HM2AT\\adaptiveLogic\\MonitorFSM.java");
		List<String> code= new ArrayList<String>();
		code.add(Scriptor.createPackagePreamble());
		List<String> parents= new ArrayList<String>();
		parents.add("Monitor");
		parents.add("Controller");

		List<String> inter= new ArrayList<String>();
		inter.add("Data");
		code.addAll(Scriptor.createClass("MonitorFSM",parents,inter));
		
		txtEditor.setEnabled(true);
		txtEditor.setText(Scriptor.printCode(code));
		// set Monitor entity
		// set Reasoner entity
		// set Selector entity
		// set Executor entity
		btnDownload.setEnabled(true);
		
	}

	private void showSearchDeviceDialog() {
		lblDeviceStatus.setText("");
		lblDeviceStatus.setEnabled(false);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setDialogTitle("Choose Root Folder or File for the device");
		int response = fileChooser.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			fileChoosed = true;
			File selectedFile = fileChooser.getSelectedFile();
			txtDeviceLocation.setText(selectedFile.getAbsolutePath());
			txtDeviceLocation.setEnabled(false);
			cbDeviceLang.setEnabled(true);
			cbDeviceType.setEnabled(true);
			System.out.println("Save as file: " + selectedFile.getAbsolutePath());
		} else {
			fileChoosed = false;
			cbDeviceLang.setEnabled(false);
			cbDeviceType.setEnabled(false);
		}
	}

	public void setDevice(UnderlyingDevice device) {
		this.device = device;
	}

	public void setAdaptable(boolean adaptable) {
		this.adaptable = adaptable;
	}

	public JLabel getLblDeviceStatus() {
		return this.lblDeviceStatus;
	}

	public JTabbedPane getTabbedPanel() {
		return this.tabbedPaneAdaptiveLayer;
	}

	public String[] populateDeviceModel() {
		// TODO Auto-generated method stub
		String[] options = { "Holistic-Basic", "Organic Comp.", "Autonomic Comp.", "Holistic-3Comp." };
		return options;
	}

	private String[] populateCodeLanguage() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Java", "C#", "C++", "C" };
		return options;
	}
}

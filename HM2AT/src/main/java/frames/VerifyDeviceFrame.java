package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.fasterxml.jackson.databind.ObjectMapper;

import logic.MethodTableModel;
import logic.UnderlyingDevice;
import utils.ComboPopulator;
import utils.FileManager;
import utils.JARManager;

public class VerifyDeviceFrame extends JFrame {

	private SpecificModeFrame specificModeFrame;
	private JPanel contentPane;
	private JButton btnContinue;
	private JButton btnBack;
	private JTextField txtDeviceName;
	private JTextField txtMainEntityName;

	private String notAdaptableStatus = "<html><font color='red'>Not adaptable</font></html>";
	private String adaptableStatus = "<html><font color='green'> Adaptable </font></html>";
	private boolean status = false;

	private String configExt = "json";

	private UnderlyingDevice device;
	private JComboBox cbDeviceParadigm;
	private JComboBox cbDeviceLanguage;
	private JComboBox cbCommPattern;
	private String deviceType;
	private String deviceLanguage;
	private boolean adaptable;
	private JTextField txtConfigFileName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VerifyDeviceFrame frame = new VerifyDeviceFrame(
							"C:\\Users\\rosed\\Downloads\\Installers\\JabRef-5.7.msi", "Java", "JAR",
							new SpecificModeFrame());
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
	public VerifyDeviceFrame(String filePath, String language, String type, SpecificModeFrame specificModeFrame) {
		// Saving parent Frame
		this.specificModeFrame = specificModeFrame;
		this.deviceType = type;
		this.deviceLanguage = language;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 778, 668);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		String title = "HM" + "\u00B2" + "AT " + " - Adaptability Verification";
		setTitle(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IconHM2AT.png")));
		setContentPane(contentPane);

		/**
		 * Create the panels and labels
		 */

		JPanel panelGeneralInfo = new JPanel();
		panelGeneralInfo.setBorder(
				new TitledBorder(null, "General Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panelAdaptableHooks = new JPanel();
		panelAdaptableHooks.setBorder(
				new TitledBorder(null, "Adaptable Hooks", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Communication", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setVisible(status);

		JLabel lblStatusValue = new JLabel("                           ");
		lblStatusValue.setVisible(status);
		lblStatusValue.setFont(new Font("Tahoma", Font.PLAIN, 11));

		/**
		 * Create the buttons.
		 */

		JButton btnValidate = new JButton("");
		ImageIcon imgSearch = new ImageIcon(this.getClass().getResource("/evaluate3.png"));
		btnValidate.setIcon(imgSearch);
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblStatus.setVisible(true);
				lblStatusValue.setVisible(true);
				String msg;
				adaptable = evaluateAdaptability();
				if (adaptable) {
					msg = adaptableStatus;
					specificModeFrame.getLblDeviceStatus()
							.setText("<html><font color='green'>Adaptable Device</font></html>");
				} else {
					msg = notAdaptableStatus;
					specificModeFrame.getLblDeviceStatus()
							.setText("<html><font color='red'>Not Adaptable Device</font></html>");
				}
				lblStatusValue.setText(msg);
				specificModeFrame.getLblDeviceStatus().setEnabled(true);
				specificModeFrame.getTabbedPanel().setEnabled(adaptable);
//				specificModeFrame.getTabbedPanel().setFocusable(true);
//				specificModeFrame.getTabbedPanel().setSelectedIndex(0);
				btnContinue.setEnabled(adaptable);
				btnBack.setEnabled(!adaptable);

			}
		});

		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				device = null;
				VerifyDeviceFrame.this.dispose();
			}
		});

		btnContinue = new JButton("Continue");
		btnContinue.setEnabled(false);
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				specificModeFrame.setDevice(device);
				specificModeFrame.setAdaptable(adaptable);
				specificModeFrame.cbDeviceModel.setEnabled(true);
				specificModeFrame.cbModelProgLang.setEnabled(true);
				VerifyDeviceFrame.this.dispose();
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panelAdaptableHooks, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 732,
										Short.MAX_VALUE)
								.addComponent(panelGeneralInfo, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 732,
										Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING,
										gl_contentPane.createSequentialGroup().addComponent(lblStatus)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lblStatusValue, GroupLayout.PREFERRED_SIZE, 125,
														GroupLayout.PREFERRED_SIZE)
												.addGap(396).addComponent(btnBack)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnContinue))
								.addComponent(btnValidate, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 732,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(7)
						.addComponent(panelGeneralInfo, GroupLayout.PREFERRED_SIZE, 181, Short.MAX_VALUE).addGap(18)
						.addComponent(panelAdaptableHooks, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnValidate, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(31)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblStatusValue).addComponent(lblStatus))
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnBack)
										.addComponent(btnContinue)))
						.addContainerGap()));

		/**
		 * Create "Communication" Panel
		 */

		JLabel lblCommPattern = new JLabel("Pattern");
		cbCommPattern = new JComboBox(populateCommunicationPattern());

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(lblCommPattern).addGap(18)
						.addComponent(cbCommPattern, 0, 608, Short.MAX_VALUE).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(22)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCommPattern)
								.addComponent(cbCommPattern, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(45, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		JLabel lblDeviceName = new JLabel("Device Name");
		txtDeviceName = new JTextField();
		txtDeviceName.setColumns(10);

		JLabel lblDeviceParadigm = new JLabel("Paradigm");
		cbDeviceParadigm = new JComboBox();
		ComboPopulator.populateProgrammingParadigm(cbDeviceParadigm);

		JLabel lblDeviceLanguage = new JLabel("Language");
		cbDeviceLanguage = new JComboBox();
		cbDeviceLanguage.setEnabled(false);
		ComboPopulator.populateProgrammingLanguage(cbDeviceLanguage);

		JLabel lblMainEntityName = new JLabel("Entity Name");
		txtMainEntityName = new JTextField();
		txtMainEntityName.setColumns(10);

		JLabel lblConfigFile = new JLabel("Config. File");

		txtConfigFileName = new JTextField();
		txtConfigFileName.setEnabled(false);
		txtConfigFileName.setEditable(false);
		txtConfigFileName.setColumns(10);

		GroupLayout gl_panelGeneralInfo = new GroupLayout(panelGeneralInfo);
		gl_panelGeneralInfo.setHorizontalGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGeneralInfo.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDeviceName, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDeviceParadigm).addComponent(lblMainEntityName)
								.addComponent(lblConfigFile))
						.addGap(18)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
								.addComponent(txtDeviceName, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
								.addGroup(gl_panelGeneralInfo.createSequentialGroup()
										.addComponent(cbDeviceParadigm, GroupLayout.PREFERRED_SIZE, 156,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
										.addComponent(lblDeviceLanguage, GroupLayout.PREFERRED_SIZE, 73,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbDeviceLanguage,
												GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
								.addComponent(txtMainEntityName, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 610,
										Short.MAX_VALUE)
								.addComponent(txtConfigFileName, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panelGeneralInfo.setVerticalGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGeneralInfo.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblConfigFile).addComponent(txtConfigFileName, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDeviceName).addComponent(txtDeviceName, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDeviceParadigm).addComponent(lblDeviceLanguage)
								.addComponent(cbDeviceLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(cbDeviceParadigm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMainEntityName).addComponent(txtMainEntityName,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(29, Short.MAX_VALUE)));
		panelGeneralInfo.setLayout(gl_panelGeneralInfo);
		contentPane.setLayout(gl_contentPane);

		/**
		 * DEFAULT: WORKING WITH A .CONFIG FILE
		 */

		// If type is Config

		Path path = Paths.get(filePath);
		String pathConfigFile = searchConfigFile(path);

		if (pathConfigFile.isEmpty()) {
			System.out.println("There is no configuration file!");
			loadingEmptyDevice();
			loadingDevice();
		} else {
			loadConfigFile(pathConfigFile);
		}

		loadingData();
		Object[][] data = { { "1", "Class1", "myMethod1(String, String, String)", new Boolean(false), "---" },
				{ "2", "Class1", "myMethod2(String, String, String)", new Boolean(true), "---" },
				{ "3", "Class1", "myMethod3(String, String, String)", new Boolean(false), "---" },
				{ "4", "Class2", "myMethod4(String, String, String)", new Boolean(true), "---" },
				{ "5", "Class3", "myMethod5(String, String, String)", new Boolean(false), "---" } };

		// Create Customized TableModel
		MethodTableModel model = new MethodTableModel(data);

		JTable table = new JTable(model);
		table.getTableHeader().setBackground(new Color(40, 164, 195));
		table.getTableHeader().setForeground(Color.white);
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setShowGrid(true);
		table.setGridColor(new Color(179, 204, 204));
		table.setPreferredScrollableViewportSize(new Dimension(650, 120));
		table.setFillsViewportHeight(true);

		// set the column width for each column
		setJTableColumnsWidth(table, 650, 5, 30, 35, 10, 20);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Fiddle with the Hobby column's cell editors/renderers.
		setUpHookTypeColumn(table, table.getColumnModel().getColumn(4));

		// Add the scroll pane to this panel.
		panelAdaptableHooks.add(scrollPane);

	}

	private void loadingData() {
		//loadingDataFromJAR();

	}

	private void loadingDataFromJAR() {
		device.getPath();
		try {
			Set <Class> classesFromJAR=JARManager.getClassesFromJarFile(device.getPath().toFile());
			Set<String> names = classesFromJAR.stream().map(Class::getName).collect(Collectors.toSet());
			
			Iterator iterator = names.iterator();
			
			//simple iteration
			while(iterator.hasNext()){
				System.out.println(iterator.next());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String searchConfigFile(Path path) {
		String pathConfigFile = "";
		try {
			List<String> configFiles = FileManager.findFiles(path, configExt);
			if (!configFiles.isEmpty()) {
				System.out.println("Load Config File at: " + configFiles.get(0));
				pathConfigFile = configFiles.get(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathConfigFile;
	}

	private void loadConfigFile(String filePath) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			txtConfigFileName.setText(filePath);

			// JSON file to Java object
			device = mapper.readValue(new File(filePath), UnderlyingDevice.class);
			loadingDevice();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadingEmptyDevice() {
		device = new UnderlyingDevice();
		device.setLanguage(deviceLanguage);

	}

	public void loadingDevice() {
		txtDeviceName.setText(device.getName());
		switch (device.getParadigm()) {
		case "POO":
			cbDeviceParadigm.setSelectedIndex(1);
			break;
		case "Service-Oriented":
			cbDeviceParadigm.setSelectedIndex(2);
			break;
		case "Aspect-Oriented":
			cbDeviceParadigm.setSelectedIndex(3);
			break;
		case "Declarative":
			cbDeviceParadigm.setSelectedIndex(4);
			break;
		case "Other":
			cbDeviceParadigm.setSelectedIndex(5);
			break;
		default:
			cbDeviceParadigm.setSelectedIndex(0);
			break;
		}

		switch (device.getLanguage()) {
		case "Java":
			cbDeviceLanguage.setSelectedIndex(1);
			break;
		case "C#":
			cbDeviceLanguage.setSelectedIndex(2);
			break;
		case "C++":
			cbDeviceLanguage.setSelectedIndex(3);
			break;
		case "C":
			cbDeviceLanguage.setSelectedIndex(4);
			break;
		case "Other":
			cbDeviceLanguage.setSelectedIndex(5);
			break;
		default:
			cbDeviceLanguage.setSelectedIndex(0);
			break;
		}

		txtMainEntityName.setText(device.getMainEntity());

		if (device.getCommunication() != null) {
			switch (device.getCommunication().getType()) {
			case "Signals & Slots":
				cbCommPattern.setSelectedIndex(1);
				break;
			case "Events & Listeners":
				cbCommPattern.setSelectedIndex(2);
				break;
			case "Message-oriented":
				cbCommPattern.setSelectedIndex(3);
				break;
			case "Other":
				cbCommPattern.setSelectedIndex(5);
				break;
			default:
				cbCommPattern.setSelectedIndex(0);
				break;
			}
		}
	}

	public void setUpHookTypeColumn(JTable table, TableColumn hoookTypeColumn) {
		// Set up the editor for the sport cells.
		JComboBox cbHookType = new JComboBox();
		cbHookType.addItem("---");
		cbHookType.addItem("Sensor");
		cbHookType.addItem("Actuator");
		hoookTypeColumn.setCellEditor(new DefaultCellEditor(cbHookType));

		// Set up tool tips for the sport cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for combo box");
		hoookTypeColumn.setCellRenderer(renderer);
	}

	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
		double total = 0;
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			total += percentages[i];
		}

		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
		}
	}

	private String[] populateCommunicationPattern() {
		// TODO Auto-generated method stub
		String[] options = { "---", "Signals & Slots", "Events & Listeners", "Message-oriented", "Other" };
		return options;
	}

	private boolean evaluateAdaptability() {
		return true;
	}
}

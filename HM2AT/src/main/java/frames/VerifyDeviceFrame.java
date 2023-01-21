package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.rossedth.FSM_Adaptable.RecognizerFSM;
import org.rossedth.hm2aTool.MainHM2AT;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import logic.CommunicationMechanism;
import logic.Hook;
import logic.MethodTable;
import logic.Repository;
import logic.UnderlyingDevice;
import utils.ComboPopulator;
import utils.FileManager;
import utils.JARManager;
import utils.MethodTableModel;

public class VerifyDeviceFrame extends JFrame {

	private SpecificModeFrame specificModeFrame;
	private JPanel contentPane;

	private JButton btnContinue;
	private JButton btnBack;

	// Device Panel fields
	private UnderlyingDevice device;
	private JTextField txtLocation;
	private JTextField txtDeviceName;
	private String deviceType;
	private String deviceLanguage;
	private JTextField txtMainEntityName;
	private JTextField txtConfigFileName;

	private JComboBox cbDeviceParadigm;
	private JComboBox cbDeviceLanguage;
	private JButton btnSearchDeviceConfig;

	// Table Fields
	private MethodTableModel model;
	private JTable methodsTable;
	
	// Communication Panel fields
	private JComboBox cbCommPattern;
	private String configExt = "config-device.json";

	// Status fields
	private boolean status = false;
	private String notAdaptableStatus = "<html><font color='red'>Not adaptable</font></html>";
	private String adaptableStatus = "<html><font color='green'> Adaptable </font></html>";
	private boolean adaptable;



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

		JPanel panelCommunication = new JPanel();
		panelCommunication
				.setBorder(new TitledBorder(null, "Communication", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setVisible(status);

		JLabel lblStatusValue = new JLabel("                           ");
		lblStatusValue.setVisible(status);
		lblStatusValue.setFont(new Font("Tahoma", Font.PLAIN, 11));

		/**
		 * Create the buttons.
		 */

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

		JButton btnValidate = new JButton("");
		ImageIcon imgValidate = new ImageIcon(this.getClass().getResource("/evaluate3.png"));
		btnValidate.setIcon(imgValidate);
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

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
						.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblStatus)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblStatusValue, GroupLayout.PREFERRED_SIZE, 125,
												GroupLayout.PREFERRED_SIZE)
										.addGap(369).addComponent(btnBack).addGap(18).addComponent(btnContinue)
										.addGap(15))
								.addComponent(panelCommunication, GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
								.addComponent(panelAdaptableHooks, GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
								.addComponent(panelGeneralInfo, GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE))
						.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addComponent(btnValidate, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addGap(27)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(7)
				.addComponent(panelGeneralInfo, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(panelAdaptableHooks, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
				.addGap(18).addComponent(panelCommunication, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
				.addGap(18).addComponent(btnValidate, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblStatusValue)
								.addComponent(lblStatus))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnBack)
								.addComponent(btnContinue)))
				.addContainerGap()));

		/**
		 * Create "General Information" Panel
		 */

		JLabel lblLocation = new JLabel("Location");
		txtLocation = new JTextField(filePath);
		txtLocation.setEditable(false);
		txtLocation.setEnabled(false);
		txtLocation.setColumns(10);

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

		JButton btnMainEntitySearch = new JButton("");
		btnMainEntitySearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Loading the methods of the selected entity
				List<MethodTable> methods = loadMethodsToTable();
				model = new MethodTableModel(methods);
				methodsTable.setModel(model);
				setUpHookTypeColumn(methodsTable, methodsTable.getColumnModel().getColumn(4));
			}
		});
		ImageIcon imgLoad = new ImageIcon(this.getClass().getResource("/load.png"));
		btnMainEntitySearch.setIcon(imgLoad);

		btnSearchDeviceConfig = new JButton("");
		btnSearchDeviceConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSearchDeviceConfigurationFileDialog(filePath);
			}
		});
		btnSearchDeviceConfig.setEnabled(false);
		ImageIcon imgSearch = new ImageIcon(this.getClass().getResource("/search.png"));
		btnSearchDeviceConfig.setIcon(imgSearch);

		GroupLayout gl_panelGeneralInfo = new GroupLayout(panelGeneralInfo);
		gl_panelGeneralInfo.setHorizontalGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGeneralInfo.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDeviceParadigm)
								.addComponent(lblDeviceName, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMainEntityName).addComponent(lblConfigFile).addComponent(lblLocation))
						.addGap(18)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelGeneralInfo.createSequentialGroup().addGroup(gl_panelGeneralInfo
										.createParallelGroup(Alignment.LEADING)
										.addComponent(txtMainEntityName, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
										.addGroup(gl_panelGeneralInfo.createSequentialGroup()
												.addComponent(cbDeviceParadigm, GroupLayout.PREFERRED_SIZE, 251,
														GroupLayout.PREFERRED_SIZE)
												.addGap(36)
												.addComponent(lblDeviceLanguage, GroupLayout.PREFERRED_SIZE, 73,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(cbDeviceLanguage, 0, 205, Short.MAX_VALUE)))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnMainEntitySearch, GroupLayout.PREFERRED_SIZE, 31,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_panelGeneralInfo.createSequentialGroup().addGroup(gl_panelGeneralInfo
										.createParallelGroup(Alignment.TRAILING)
										.addComponent(txtLocation, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
										.addComponent(txtDeviceName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 575,
												Short.MAX_VALUE)
										.addComponent(txtConfigFileName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												575, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSearchDeviceConfig,
												GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
										.addGap(4)))
						.addGap(4)));
		gl_panelGeneralInfo.setVerticalGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGeneralInfo.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblLocation))
						.addGap(16)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblConfigFile).addComponent(txtConfigFileName,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(btnSearchDeviceConfig))
						.addGap(8)
						.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelGeneralInfo.createSequentialGroup()
										.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblDeviceName).addComponent(txtDeviceName,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblDeviceParadigm).addComponent(lblDeviceLanguage)
														.addComponent(cbDeviceLanguage, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(cbDeviceParadigm, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panelGeneralInfo.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblMainEntityName).addComponent(txtMainEntityName,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)))
								.addComponent(btnMainEntitySearch))
						.addContainerGap(44, Short.MAX_VALUE)));
		panelGeneralInfo.setLayout(gl_panelGeneralInfo);
		contentPane.setLayout(gl_contentPane);
		init(filePath);

		/**
		 * DEFAULT: WORKING WITH A .CONFIG FILE
		 */

		List<MethodTable> methods = new ArrayList<>();

		// Create Customized TableModel
		model = new MethodTableModel(methods);

		methodsTable = new JTable(model);
		methodsTable.getTableHeader().setBackground(new Color(40, 164, 195));
		methodsTable.getTableHeader().setForeground(Color.white);
		methodsTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
		methodsTable.setShowGrid(true);
		methodsTable.setGridColor(new Color(179, 204, 204));
		methodsTable.setPreferredScrollableViewportSize(new Dimension(650, 120));
		methodsTable.setFillsViewportHeight(true);

		// set the column width for each column
		setJTableColumnsWidth(methodsTable, 650, 5, 30, 35, 10, 20);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(methodsTable);

		// Fiddle with the Hobby column's cell editors/renderers.
		setUpHookTypeColumn(methodsTable, methodsTable.getColumnModel().getColumn(4));

		// Add the scroll pane to this panel.
		panelAdaptableHooks.add(scrollPane);

		/**
		 * Create "Communication" Panel
		 */

		JLabel lblCommPattern = new JLabel("Pattern");
		cbCommPattern = new JComboBox(populateCommunicationPattern());

		GroupLayout gl_panel = new GroupLayout(panelCommunication);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(lblCommPattern).addGap(18)
						.addComponent(cbCommPattern, 0, 646, Short.MAX_VALUE).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbCommPattern, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCommPattern))
						.addContainerGap(30, Short.MAX_VALUE)));
		panelCommunication.setLayout(gl_panel);

	}

	private void init(String filePath) {
		// Search device's configuration file on the same folder
		Path path = Paths.get(filePath);
		String pathConfigFile = searchConfigFileOnRoot(path);

		if (pathConfigFile.isEmpty()) {
			System.out.println("There is no configuration file!");
			btnSearchDeviceConfig.setEnabled(true);

			loadingEmptyDevice();
			loadingDevice();
		} else {
			loadConfigFile(pathConfigFile);
		}

	}

	private String searchConfigFileOnRoot(Path path) {
		String pathConfigFile = "";
		try {
			List<Path> configFiles = FileManager.findByFileName(path, configExt);
			if (!configFiles.isEmpty()) {
				System.out.println("Load Config File at: " + configFiles.get(0));
				pathConfigFile = configFiles.get(0).toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pathConfigFile;
	}

	private void showSearchDeviceConfigurationFileDialog(String filePath) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setCurrentDirectory(new File(filePath));
		fileChooser.setDialogTitle("Choose Configuration File for the device");

		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("Files ending in (.json)", "json");
		fileChooser.setFileFilter(jsonFilter);
		int response = fileChooser.showOpenDialog(this);

		if (response == JFileChooser.APPROVE_OPTION) {
			// fileChoosed = true;
			File selectedFile = fileChooser.getSelectedFile();
			loadConfigFile(selectedFile.getAbsolutePath());
			System.out.println("Configuration file at: " + selectedFile.getAbsolutePath());
		} else {
//			fileChoosed = false;
//			cbDeviceLang.setEnabled(false);
//			cbDeviceType.setEnabled(false);
		}

	}

	private List<MethodTable> loadMethodsToTable() {
		List<MethodTable> methods = new ArrayList<MethodTable>();
		
		// Variables to load the mainEntity
		ClassLoader loader=getClass().getClassLoader();
		Class entityClass;
		try {
			String entityName=txtMainEntityName.getText();
			entityClass = loader.loadClass(entityName);
			List<Method>entityMethods=loadEntityMethods(entityClass);
			entityName=entityName.substring(entityName.lastIndexOf('.')+1,entityName.length());
			int seq=1;			
			for (Method method:entityMethods) {
				String id="M"+seq;
				seq+=1;
				
				String simplifiedMethodName=getSimplifiedMethodName(method.toString());
				MethodTable methodTable= new MethodTable(id,entityName, simplifiedMethodName, false, "None of the above");
				methods.add(methodTable);
				System.out.println(id+"  "+entityClass.getSimpleName()+" "+simplifiedMethodName);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return methods;
	}

	private List<Method> loadEntityMethods(Class entityClass) {
		List<Method> methods=null;
		try {
			//entityClass = loader.loadClass("org.rossedth.FSM_Adaptable.RecognizerFSM");
			Object entityObject=entityClass.newInstance();
			methods=getPublicMethods(entityObject);
			System.out.println("The class has "+methods.size()+" public methods");
			methods.forEach(System.out::println);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return methods;
	}

	private List<Method> getPublicMethods(Object o) {
		   List<Method> publicMethods = new ArrayList<>();

		   // getDeclaredMethods only includes methods in the class (good)
		   // but also includes protected and private methods (bad)
		   for (Method method : o.getClass().getDeclaredMethods()) {
		      if (!Modifier.isPublic(method.getModifiers())) continue; //only **public** methods
		      publicMethods.add(method);
		   }
		   return publicMethods;
		}
	
	
	private String getSimplifiedMethodName(String fullMethodName) {
		String simplified="";
		String[] split1=fullMethodName.split(" ");
		String modifier = split1[0];
		String returnType=split1[1];
		returnType=returnType.substring(returnType.lastIndexOf('.')+1,returnType.length());
		String signature=split1[2];
		String methodName=signature.substring(0, signature.indexOf('('));
		methodName=methodName.substring(methodName.lastIndexOf('.')+1,methodName.length());
		String parameters;
		if (signature.indexOf(')')-signature.indexOf('(')==1) {
			parameters="";
		}else {
			parameters=signature.substring(signature.indexOf('(')+1,signature.indexOf(')'));
			parameters=parameters.substring(parameters.lastIndexOf('.')+1,parameters.length());
		}

		simplified=modifier+" "+returnType+" "+methodName+"("+parameters+")";
		return simplified;
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
		updateDeviceFromGUI();
		Path temp=saveTempDevice(device);
		boolean result=runEvaluatePython(temp.toString());
		temp.toFile().delete();
		return result;
	}

	private void updateDeviceFromGUI() {
		device.setName(txtDeviceName.getText());
		device.setParadigm(cbDeviceParadigm.getSelectedItem().toString());
		device.setMainEntity(txtMainEntityName.getText());
		CommunicationMechanism comm = new CommunicationMechanism();
		comm.setType(cbCommPattern.getSelectedItem().toString());

		List<Hook> hooks = new ArrayList<Hook>();

		for (int row = 0; row < model.getRowCount(); row++) {
			boolean isMethodHooK = (boolean) model.getValueAt(row, 3);
			if (isMethodHooK) {
				MethodTable method = new MethodTable();
				method.setId((String) model.getValueAt(row, 0));
				method.setOwnerClass((String) model.getValueAt(row, 1));
				method.setSignature((String) model.getValueAt(row, 2));
				method.setHook((Boolean) model.getValueAt(row, 3));
				method.setHookType((String) model.getValueAt(row, 4));

				Hook hookMethod = new Hook();
				hookMethod.setName(method.getSignature());
				hookMethod.setType(method.getHookType());
				hooks.add(hookMethod);
			}
		}

		device.setHooks(hooks);
		device.setCommunication(comm);
	}

	private Path saveTempDevice(UnderlyingDevice device) {
		Path temp=null;
		try {
            Path path = Paths.get(MainHM2AT.repository+"\\tmp\\");
            Files.createDirectories(path);
            System.out.println("Temp directory created at: " + path.toString());
            
            // Create an temporary file
            temp = Files.createTempFile(path,"underlying-device", ".json");
            System.out.println("Temp file : " + temp);

    		ObjectMapper mapper = new ObjectMapper();
    		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(temp.toFile(), device);
    		
        } catch (IOException e) {
            e.printStackTrace();
        }
		return temp;
	}
	
	public static boolean runEvaluatePython(String source) {
		boolean adaptable=false;
		ProcessBuilder builder = new ProcessBuilder("python",
				MainHM2AT.repository+ "\\scripts\\evaluateAdaptability.py", source);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(process.getInputStream()));
	            StringBuilder buffer = new StringBuilder();     
	            String line = null;
	            while ((line = in.readLine()) != null){           
	                buffer.append(line);
	            }
	            int exitCode = process.waitFor();
	            System.out.println("Value is: "+buffer.toString());  
	            adaptable=buffer.toString().equalsIgnoreCase("true");
	            System.out.println("Process exit value:"+exitCode);        
	            in.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return adaptable;
	}

}

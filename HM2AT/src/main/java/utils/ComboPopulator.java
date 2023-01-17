package utils;

import java.lang.StackWalker.Option;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.rossedth.hm2aTool.MainHM2AT;

import frames.DependencyPanel;
import logic.AdaptivityModel;
import logic.AdaptivityModelImplementation;
import logic.ImplementationDependency;
import logic.ImplementationExample;

public class ComboPopulator {

	public static void populateModelingLanguage(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("UML");
		options.add("Other 1");
		options.add("Other 2");
		
		for (String value : options) {
			combo.addItem(value);
		}
	}
	
	public static void populateModelfromRepository(JComboBox<AdaptivityModel> combo) {
//		for (AdaptivityModel model : MainHM2AT.models) {
//			combo.addItem(model.getName());
//		}
		DefaultComboBoxModel<AdaptivityModel> defaultComboBoxModel= new DefaultComboBoxModel<AdaptivityModel>();
		
		for(AdaptivityModel model: MainHM2AT.models) {
			defaultComboBoxModel.addElement(model);
		}
		
		combo.setModel(defaultComboBoxModel);
		combo.setRenderer(new ModelListCellRenderer());

	}
	
	
	public static void populateProgrammingLanguage(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Java");
		options.add("C");
		options.add("C++");
		options.add("C#");
		options.add("Ruby");
		options.add("PHP");
		options.add("Python");

		for (String value : options) {
			combo.addItem(value);
		}
	}
	

	public static void  populateProgrammingParadigm(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Object-oriented");
		options.add("Service-oriented");
		options.add("Aspect-oriented");
		options.add("Declarative");
		options.add("Other");

		for (String value : options) {
			combo.addItem(value);
		}
		
	}

	public static void  populateCodeTypeFile(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Source");
		options.add("Library");
		
		for (String value : options) {
			combo.addItem(value);
		}	
	}	
	
	public static void  populateDeviceTypeFile(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Source");
		options.add("JAR");
		options.add("DDL");
		options.add("PY");
		
		for (String value : options) {
			combo.addItem(value);
		}	
	}	
	
	public static void  populateDependencyManager(JComboBox<String> combo) {
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Maven Central (Java)");
		options.add("NuGet (.NET)");
		options.add("Packagist (PHP)");
		options.add("PyPI (Python)");
		options.add("RubyGems (Ruby)");
		options.add("Other");
		
		for (String value : options) {
			combo.addItem(value);
		}	
	}	

	public static void  populateModelApproach(JComboBox<String> combo){
		ArrayList<String> options = new ArrayList<String>();
		options.add("---");
		options.add("Holistic");
		options.add("Organic Computing");
		options.add("Autonomic Computing");
		options.add("Control Theory");
		options.add("CAS");
		options.add("Other");
		
		for (String value : options) {
			combo.addItem(value);
		}	
	}
	
	public static void  populateRepositoryType(JComboBox<String> combo){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Local");
		options.add("External");
		
		for (String value : options) {
			combo.addItem(value);
		}	
	}

	public static void populateImplementationfromModel(JComboBox<AdaptivityModelImplementation> combo, String modelId) {
		DefaultComboBoxModel<AdaptivityModelImplementation> defaultComboBoxImplementation= new DefaultComboBoxModel<AdaptivityModelImplementation>();
		
		for(AdaptivityModelImplementation implementation: MainHM2AT.implementations) {
			if(implementation.getModel(MainHM2AT.models).getId().equalsIgnoreCase(modelId)) {
				defaultComboBoxImplementation.addElement(implementation);				
			}
		}
		
		combo.setModel(defaultComboBoxImplementation);
		combo.setRenderer(new ImplementationListCellRenderer());

	}
	
	public static void populateDepManagerfromImplementation(JComboBox<ImplementationDependency> combo, String implementationId) {
		DefaultComboBoxModel<ImplementationDependency> defaultComboBoxDependency= new DefaultComboBoxModel<ImplementationDependency>();
		
		for(ImplementationDependency dependency: MainHM2AT.dependencies) {
			if(dependency.getImplementation(MainHM2AT.implementations).getId().equalsIgnoreCase(implementationId)) {
				defaultComboBoxDependency.addElement(dependency);				
			}
		}
		
		combo.setModel(defaultComboBoxDependency);
		combo.setRenderer(new DependencyListCellRenderer());

	}
	
	public static void populateExamplefromImplementation(JComboBox<ImplementationExample> combo, String implementationId) {
		DefaultComboBoxModel<ImplementationExample> defaultComboBoxExample= new DefaultComboBoxModel<ImplementationExample>();
		
		for(ImplementationExample example: MainHM2AT.examples) {
			if(example.getImplementation(MainHM2AT.implementations).getId().equalsIgnoreCase(implementationId)) {
				defaultComboBoxExample.addElement(example);				
			}
		}
		
		combo.setModel(defaultComboBoxExample);
		combo.setRenderer(new ExampleListCellRenderer());

	}
	
}

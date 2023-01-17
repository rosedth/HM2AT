package org.rossedth.hm2aTool;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;


import frames.WorkingRepositoryDialog;
import logic.AdaptivityModel;
import logic.AdaptivityModelImplementation;
import logic.ImplementationDependency;
import logic.ImplementationExample;
import logic.Repository;
import utils.FileManager;
import utils.JSONManager2;

public class MainHM2AT {
	public static Path repository;
	public static Repository repo;
	
	public static List<AdaptivityModel> models;
	public static List<AdaptivityModelImplementation> implementations;
	public static List<ImplementationDependency> dependencies;	
	public static List<ImplementationExample> examples;
	
	public static Map<String,Integer> indexes;
	
	public MainHM2AT() {
		super();
		/**
		 * Look for Repository
		 */
		 loadSettings();
		 loadIndexes();
		 loadData();
	}
	
	private void loadSettings() {
		//look for ".metadata" to suggest previous configurations
		String metadata=FileManager.loadMetadata();
		WorkingRepositoryDialog chooserRepo=new WorkingRepositoryDialog(metadata);
		chooserRepo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		chooserRepo.setModal(true);
		chooserRepo.setLocationRelativeTo(null);
		chooserRepo.setVisible(true);
	}
	
	private void loadData() {
		// load models from repository
		MainHM2AT.models=JSONManager2.readModels();
		MainHM2AT.implementations=JSONManager2.readImplementations();
		MainHM2AT.dependencies=JSONManager2.readDependencies();
		MainHM2AT.examples=JSONManager2.readExamples();
	}
	
	private void loadIndexes() {
		// load indexes from repository
		MainHM2AT.indexes=FileManager.readIndexes(MainHM2AT.repository+"\\indexes.txt");
	}


	
	public static boolean loadRepository() {
		boolean result = false;
		List<Path> paths = new ArrayList<>();
		try {
			paths = FileManager.findByFileName(Paths.get("").toAbsolutePath(), "repo-config.json");
			switch (paths.size()) {
			case 1:
				ObjectMapper mapper = new ObjectMapper();
				try {
					repo = mapper.readValue(paths.get(0).toFile(), Repository.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				result = true;
				break;
			case 0:
				JOptionPane.showMessageDialog(null, "There is NO configuration file for the repository." + "\n"
						+ "Can't load the tool without the repository.");
				result = false;
				break;
			default:
				JOptionPane.showMessageDialog(null, "There should be ONLY one configuration file for the repository."
						+ "\n" + "Can't load the tool");
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}

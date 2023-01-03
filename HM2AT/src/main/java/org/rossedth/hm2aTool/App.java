package org.rossedth.hm2aTool;

import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import frames.Loading;
import frames.ToolModeFrame;
import logic.Repository;
import utils.FileManager;


public class App {
	public static Repository repo;
	/**
	 * Launch the application.
	 */
	public static void main(String args[]) {
		/* Set the FlatLaf look and feel from https://www.formdev.com/flatlaf/themes/ */
		FlatCyanLightIJTheme.setup();
		UIManager.put("Component.arrowType", "chevron");
		UIManager.put("ScrollBar.trackArc", 999);
		UIManager.put("ScrollBar.thumbArc", 999);
		UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
		UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				if (loadRepository()) {
					Repository.verifyStructure(repo);
					Repository.updateIndexes(repo);			
					Loading loadingScreen = new Loading(null, true);
					loadingScreen.setVisible(true);
					ToolModeFrame modeframe = new ToolModeFrame();
					modeframe.setVisible(true);
				}
			}
		});
	}

	private static boolean loadRepository() {
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

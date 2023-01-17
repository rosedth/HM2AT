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
				MainHM2AT main=new MainHM2AT();
				
				if (main.loadRepository()) {
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
}

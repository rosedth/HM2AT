package utils;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import logic.AdaptivityModelImplementation;

public class ImplementationListCellRenderer  extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (value instanceof AdaptivityModelImplementation) {
			AdaptivityModelImplementation implementation=(AdaptivityModelImplementation)value;
			value= implementation.getName();
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}

package utils;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import logic.AdaptivityModelImplementation;
import logic.ImplementationDependency;

public class DependencyListCellRenderer extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (value instanceof ImplementationDependency) {
			ImplementationDependency dependency=(ImplementationDependency)value;
			value= dependency.getManager();
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}

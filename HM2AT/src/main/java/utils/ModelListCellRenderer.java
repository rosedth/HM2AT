package utils;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import logic.AdaptivityModel;

public class ModelListCellRenderer extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (value instanceof AdaptivityModel) {
			AdaptivityModel model=(AdaptivityModel)value;
			value= model.getName();
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}

package utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import logic.MethodTable;




public class MethodTableModel extends AbstractTableModel {
	private Object[] columnNames = { "Id", "Owner", "Signature", "Hook?", "Hook type" };
	private List<MethodTable> methodsData = new ArrayList<MethodTable>();

	
	public MethodTableModel() {
		methodsData=new ArrayList<>();
	}

	
	public MethodTableModel(List<MethodTable> methodsData) {
		this.methodsData = methodsData;
	}


	@Override
	public int getRowCount() {
		return methodsData.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MethodTable method= methodsData.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return method.getId();
		case 1:
			return method.getOwnerClass();
		case 2:
			return method.getSignature();
		case 3:
			return method.isHook();
		case 4:
			return method.getHookType();
		}
		return null;
	}
	
	public void add(MethodTable item) {
		methodsData.add(item);
		int row = methodsData.indexOf(item);
		fireTableRowsInserted(row, row);
	}

	public void remove(MethodTable item) {
		if (methodsData.contains(item)) {
			int row = methodsData.indexOf(item);
			methodsData.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}
	
	/*
	 * JTable uses this method to determine the default renderer/ editor for each
	 * cell. If we didn't implement this method, then the last column would contain
	 * text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {

		System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of " + value.getClass() + ")");
		System.out.println("OLD value at " + row + "," + col +": "+getValueAt(row, col));

		MethodTable method = methodsData.get(row);
		switch (col) {
		case 0:
			method.setId((String) value);break;
		case 1:
			method.setOwnerClass((String) value);break;
		case 2:
			method.setSignature((String) value);break;
		case 3:
			method.setHook((Boolean) value);break;
		case 4:
			method.setHookType((String) value);break;
		}

		fireTableCellUpdated(row, col);

		System.out.println("New value at:" + row + "," + col +": "+getValueAt(row, col));

	}

}

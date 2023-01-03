package logic;

import javax.swing.table.AbstractTableModel;


public class MethodTableModel extends AbstractTableModel {
	//private Object[] columnNames = { "First Name", "Last Name", "Sport", "# of Years", "Vegetarian", "Hobby" };
	private Object[] columnNames= {"Id", "Owner", "Seignature","hook?","hook type" };
	private Object[][] data;


	public MethodTableModel(Object[][] data) {
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
		// return personData.size();
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
//	        Object returnValue = null;
//			PersonTable person=personData.get(row);
//	        switch (col) {
//	        case 0:
//	                returnValue = person.getFirstName();
//	            break;
//	        case 1:
//	                returnValue = person.getLastName();
//	            break;
//	        case 2:
//	                returnValue = person.getSport();
//	            break;
//	        case 3:
//	        	returnValue = person.getAge();
//	        case 4:
//	        	returnValue = person.getVegetarian(); 
//	            break;
//	        case 5:
//	        	returnValue = person.getHobby(); 
//	            break;
//	        }

		// return returnValue;
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
		data[row][col] = value;
		fireTableCellUpdated(row, col);

	}

	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i = 0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j = 0; j < numCols; j++) {
				System.out.print("  " + data[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}
}

package model;

import javax.swing.table.AbstractTableModel;

public class PluginTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6690022287132576741L;

	private final Object[][] rowData;

	private final String[] columnNames = { "Plugin", "Activated" };

	public PluginTableModel(Object rowData[][]) {
		this.rowData = rowData;
	}

	public Class getColumnClass(int column) {
		return (getValueAt(0, column).getClass());
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getRowCount() {
		return rowData.length;
	}

	public Object[][] getRowData() {
		return rowData;
	}

	public Object getValueAt(int row, int column) {
		return rowData[row][column];
	}

	public boolean isCellEditable(int row, int column) {
		return (column != 0);
	}

	public void setValueAt(Object value, int row, int column) {
		rowData[row][column] = value;
	}
}

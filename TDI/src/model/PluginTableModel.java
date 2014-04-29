package model;

import javax.swing.table.AbstractTableModel;

public class PluginTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6690022287132576741L;

	private final String[] columnNames = { "Plugin", "Activated" };

	private final Object[][] rowData;

	public PluginTableModel(Object rowData[][]) {
		this.rowData = rowData;
	}

	@Override
	public Class getColumnClass(int column) {
		return (getValueAt(0, column).getClass());
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return rowData.length;
	}

	public Object[][] getRowData() {
		return rowData;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return rowData[row][column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return (column != 0);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		rowData[row][column] = value;
	}
}

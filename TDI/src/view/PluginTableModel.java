package view;

import javax.swing.table.AbstractTableModel;

public class PluginTableModel extends AbstractTableModel{
	Object rowData[][];

	  String columnNames[] = { "Plugin", "Activated" };
	  
	  public PluginTableModel(Object rowData[][]){
		  this.rowData = rowData;
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

	  public Object getValueAt(int row, int column) {
	    return rowData[row][column];
	  }

	  public Class getColumnClass(int column) {
	    return (getValueAt(0, column).getClass());
	  }

	  public void setValueAt(Object value, int row, int column) {
	    rowData[row][column] = value;
	  }

	  public boolean isCellEditable(int row, int column) {
	    return (column != 0);
	  }
	  
	  public Object[][] getRowData(){
		  return rowData;
	  }
}

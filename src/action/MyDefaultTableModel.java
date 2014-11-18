/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gudangHS
 */
public class MyDefaultTableModel extends DefaultTableModel {

    private boolean[][] editable_cells; // 2d array to represent rows and columns

    protected MyDefaultTableModel(int rows, int cols) { // constructor
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return this.editable_cells[row][column];
    }

    protected static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (Object o : anArray) {
            v.addElement(o);
        }
        return v;
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        this.fireTableCellUpdated(row, col);
    }
}

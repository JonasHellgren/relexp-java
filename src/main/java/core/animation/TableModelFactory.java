package core.animation;

import javax.swing.table.DefaultTableModel;

public class TableModelFactory {

    public static DefaultTableModel of(String[] columnNames1) {
        var tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{}, columnNames1);
        return tableModel;
    }
}

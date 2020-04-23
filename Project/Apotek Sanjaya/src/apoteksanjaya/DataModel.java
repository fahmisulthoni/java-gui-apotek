/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author 
 */
public class DataModel extends AbstractTableModel {
    // inisialisasi variable
    private List<String> columnName = new ArrayList<>();
    private List<List> data = new ArrayList<>();
    {
        columnName.add("kode_supplier");
        columnName.add("nama_supplier");
        columnName.add("no_telepon");
        columnName.add("alamat");
    }
    
    // menambahkan data ke table
    public void addRow(List rowData){
        // menambahkan data
        data.add(rowData);
        // listener
        fireTableRowsInserted(data.size()-1, data.size()-1);
    }
    
    // menghapus data dari table satuan (satu per satu) sesuai index
    public void removeRow(int rowIndex){
        // menghapus data
        data.remove(rowIndex);
        // listener
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    // menghapus semua data dari table (keseluruhan)
    public void removeAllRow(){
        // inisialisasi variable
        int rows = getRowCount();
        // mengecek jika row kosong
        if(rows == 0){
            // keluar
            return;
        }
        // menghapus data
        data.clear();
        // listener
        fireTableRowsDeleted(0, rows-1);
    }
    
    // mengambil nama kolom
    public String getColumnName(int col) {
        try{
            // mengembalikan nama kolom
            return columnName.get(col);
        }catch (Exception e){
            return null;
        }
    }
    
    // mengambil jumlah baris
    @Override
    public int getRowCount() {
        return data.size();
    }

    // mengambil jumlah kolom
    @Override
    public int getColumnCount() {
        return columnName.size();
    }

    // mengambil value tertentu
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }
    
    // mengambil kolom index
    @Override
    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
    
    // set table untuk bisa diedit atau tidak
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // table tidak bisa di edit
        return false;
    }
}

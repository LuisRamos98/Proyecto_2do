package com.example.proyecto_2do;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDinamic {
    //DECLARACIÓN DE VARIABLES A USAR
    private TableLayout tableLayout;
    private Context context;
    private String[]header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;

    //CONSTRUCTOR DE LA CLASE
    public TableDinamic(TableLayout tableLayout, Context context) {
        this.tableLayout=tableLayout;
        this.context=context;
    }

    //FUNCION QUE CREA EL ENCABEZADO
    public void addHeader(String[]header){
        this.header=header;
        createHeader();
    }

    //AÑADE LOS DATOS QUE VAN A ESTAR EN LA TABLA
    public void addData(ArrayList<String[]>data){
        this.data=data;
        createDataTable(data);
    }

    //CREA LA FILA
    private void newRow(){
        tableRow = new TableRow(context);
    }

    //CREA NUEVA CELLDA O COLUMNA
    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(25);
    }

    //CREA EL ENCABEZADO DONDE ESTAN LOS NOMBRES QUE SE DESEA UTILIZAR
    private void createHeader(){
        indexC = 0;
        newRow();

        while (indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell,newTableRowParams());

        }
        tableLayout.addView(tableRow);
    }

    //CREA LA TABLE EN SI MISMA
    private void createDataTable(ArrayList<String[]>data){
        tableLayout.removeAllViews();
        createHeader();
        String info;
        for(indexR=1;indexR<=data.size();indexR++){
            newRow();
            for(indexC=0;indexC<=header.length;indexC++){
                newCell();
                String[] row = data.get(indexR-1);
                info = (indexC<row.length)?row[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }

    }

    //AÑADE ITEMS A LA TABLA
    /*
    public void addItems(String[]item){
        String info;
        data.add(item);
        indexC=0;
        newRow();
        while (indexC< header.length){
            newCell();
            info=(indexC<item.length)?item[indexC++]:"";
            txtCell.setText(info);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }*/

    public void addItems(String[]item){
        String info;
        data.add(item);
        indexC=0;
        newRow();
        while (indexC< header.length){
            newCell();
            info=(indexC<item.length)?item[indexC++]:"";
            txtCell.setText(info);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    //ESTA FUNCION SEPARA EL TEXTO PARA QUE NO ESTE TAN JUNTO Y SEA ENTENDIBLE
    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight=1;
        return params;
    }
}

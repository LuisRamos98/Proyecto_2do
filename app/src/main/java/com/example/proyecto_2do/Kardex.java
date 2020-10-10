package com.example.proyecto_2do;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;

public class Kardex extends AppCompatActivity {

    private TableLayout tableLayout;
    private String[] header ={"ID","Nombre","Telf","Tipo", "usuario"};
    private ArrayList<String[]> rows = new ArrayList<>();
    private TableDinamic tableDinamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);

        //DECLARACION Y ASIGNACION DE VARIABLES
        tableLayout = (TableLayout)findViewById(R.id.tabledinamic);

        tableDinamic = new TableDinamic(tableLayout, getApplicationContext());
        tableDinamic.addHeader(header);
        tableDinamic.addData(getClients());
    }

    //FUNCION QUE MUESTRA EN UNA TABLA LOS CLIENTES
    public void MostrarContact (View view){
        tableDinamic.addData(getClients());
    }

    //FUNCION QUE ADQUIERE Y CREA LA TABLA DE CONTACTOS
    private ArrayList<String[]> getClients(){
        rows.add(new String[]{"1","pedro","lopez","persona","user"});
        return rows;
    }

}
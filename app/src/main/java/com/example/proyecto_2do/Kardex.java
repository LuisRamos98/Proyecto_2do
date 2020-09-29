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
    private String[] header ={"ID","Nombre","Telf","Tipo"};
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

    }

    //FUNCION QUE MUESTRA EN UNA TABLA LOS CLIENTES
    public void MostrarContact (View view){
        tableDinamic.addData(getClients());
    }

    //FUNCION QUE ADQUIERE Y CREA LA TABLA DE CONTACTOS
    private ArrayList<String[]> getClients(){
        /*//Genera
        //en una variable de tipo string projec Se define los campor a seleccionar de las tabla: ContactsContract.Data, CLASE ContactsContract.CommonDataKinds con la cual se obtiene
        //el número de telefono y el tipo
        String[] projeccion = new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE};
        //Clausula de selección
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        //Ordenar
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        //Obtener los datos de cursor que llama a un ContentResolves que obtiene los datos
        Cursor c = getContentResolver().query(         // ContactResolves al cual al que pasarle Usr, projeccion,selectionClause,null,sortOrder)
                ContactsContract.Data.CONTENT_URI,     //URI ES UNA CADENA DE CONEXION A CUALQUIER TIPO DE FICHERO
                projeccion,                           //Datos a obtener las columnas
                selectionClause,                     //Criterios de selección
                null,                   //Si se usa el signo ?  se colocn la lista de parametros, si se coloca el camp se coloca null
                sortOrder);                          //El orden en el cual se desea recuperar los dator


        //Recorre el cursor y obtiene los datos de los contactos del telefono
        while(c.moveToNext()){
            rows.add(new String[]{c.getString(0), c.getString(1),c.getString(2),c.getString(3)});
        }
        c.close();*/
        return rows;
    }

}
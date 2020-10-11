package com.example.proyecto_2do;

import Entidades.Categoria;
import Entidades.Producto;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Kardex extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    Categoria categoria = null;
    int idCategoriaSelect;

    ArrayList<Producto> listaProducto;
    ArrayList<Categoria> listaCategoria;
    ArrayList<String> listaCategoriaFinal;

    private TableLayout tableLayout;
    private String[] header ={"Categoria","Producto","Cantidad","Precio Unitario", "Total"};
    private ArrayList<String[]> rows = new ArrayList<>();
    private TableDinamic tableDinamic;

    private Spinner spCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);

        spCategoria = (Spinner)findViewById(R.id.spCategoriaKardex);

        listaProducto = new ArrayList<>();
        listaCategoria = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //DECLARACION Y ASIGNACION DE VARIABLES
        tableLayout = (TableLayout)findViewById(R.id.tabledinamic);

        tableDinamic = new TableDinamic(tableLayout, getApplicationContext());
        tableDinamic.addHeader(header);
        tableDinamic.addData(ObtenerListaProducto());
        ConsultarCategorias();
    }

    //FUNCION QUE MUESTRA EN UNA TABLA LOS CLIENTES
    public void MostrarContact (View view){
        tableDinamic.addData(ObtenerListaProducto());
    }

    //FUNCION QUE ADQUIERE Y CREA LA TABLA DE CONTACTOS
    private ArrayList<String[]> ObtenerListaProducto(){
        //rows.add(new String[]{"1","pedro","lopez","persona","user"});
        for(int i=0; i<listaProducto.size(); i++){
            rows.add(String.valueOf(listaProducto.get(i).getCategoria()), listaProducto.get(i).getNombre_Producto(),
                    String.valueOf(listaProducto.get(i).getUnidades()),String.valueOf(listaProducto.get(i).getPrecio_Unitario()),
                    String.valueOf(listaProducto.get(i).getPrecio_Total()));
        }
        return rows;
    }

    private void ConsultarProductos() {
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=6&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void ConsultarCategorias(){
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=5&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("productos")){
            CargarProductos(response);
        } else if(response.has("categorias")){
            CargarCategorias(response);
        }
    }

    private void CargarCategorias(JSONObject response) {
        try {
            JSONArray jsonArray= response.getJSONArray("categorias");
            for(int i=0; i<jsonArray.length(); i++){
                categoria = new Categoria();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                categoria.setId_Empresa(jsonObject.getInt("empresa"));
                categoria.setId_Categoria(jsonObject.getInt("id"));
                categoria.setNombre_Categoria(jsonObject.getString("nombre"));
                listaCategoria.add(categoria);
            }
            ObtenerListaCategoria();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarProductos(JSONObject response) {
        listaProducto.clear();
        try {
            JSONArray jsonArray= response.getJSONArray("productos");
            for(int i=0; i<jsonArray.length(); i++){
                Producto producto = new Producto();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getInt("categoria")==idCategoriaSelect){
                    producto.setId_Empresa(jsonObject.getInt("empresa"));
                    producto.setCodigo_Producto(jsonObject.getString("codigo"));
                    producto.setId_Producto(jsonObject.getInt("id"));
                    producto.setId_Estado(jsonObject.getInt("estado"));
                    producto.setNombre_Producto(jsonObject.getString("nombre"));
                    producto.setId_Medida(jsonObject.getInt("medida"));
                    producto.setCategoria(jsonObject.getInt("categoria"));
                    producto.setUnidades(jsonObject.getInt("unidades"));
                    producto.setPrecio_Unitario(jsonObject.getDouble("pu"));
                    producto.setPrecio_Total(jsonObject.getDouble("pt"));
                    listaProducto.add(producto);
                }
            }
            ObtenerListaProducto();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void ObtenerListaProducto() {
        listaProductoFinal = new ArrayList<String>();
        listaProductoFinal.add("Seleccione");
        for(int i=0; i<listaProducto.size(); i++){
            listaProductoFinal.add(listaProducto.get(i).getNombre_Producto());
        }
        ArrayAdapter<CharSequence> adaptador =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProductoFinal);
        spNombre.setAdapter(adaptador);
        spNombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    productoSeleccionado.setId_Producto(-1);
                } else {
                    productoSeleccionado = listaProducto.get(i-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }*/

    private void ObtenerListaCategoria() {
        listaCategoriaFinal = new ArrayList<String>();
        listaCategoriaFinal.add("Seleccione");
        for(int i=0; i<listaCategoria.size(); i++){
            listaCategoriaFinal.add(String.valueOf(listaCategoria.get(i).getId_Categoria())+" - "+listaCategoria.get(i).getNombre_Categoria());
        }
        ArrayAdapter<CharSequence> adaptador =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategoriaFinal);
        spCategoria.setAdapter(adaptador);
        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    idCategoriaSelect = -1;
                } else {
                    idCategoriaSelect = listaCategoria.get(i-1).getId_Categoria();
                    ConsultarProductos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
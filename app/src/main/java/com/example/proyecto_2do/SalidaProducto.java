package com.example.proyecto_2do;

import Entidades.Categoria;
import Entidades.Producto;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class SalidaProducto extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private String codigoSalida;
    private int cantidad, idSalida;
    private int idCategoriaSelect;

    Categoria categoria = null;
    ArrayList<Categoria> listaCategoria;
    ArrayList<String> listaCategoriaFinal;
    Producto productoSeleccionado = new Producto();
    ArrayList<Producto> listaProducto;
    ArrayList<String> listaProductoFinal;

    private EditText txtCodigoSalida, txtCantidad;
    private Button btnRegistrar, btnRetirar;
    private Spinner spCategoria, spNombre;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_producto);
        requestQueue = Volley.newRequestQueue(this);
        listaCategoria = new ArrayList<>();
        listaProducto = new ArrayList<>();

        txtCodigoSalida = (EditText)findViewById(R.id.txtCodigoSalida);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrarCodigoSalida);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigoSalida = txtCodigoSalida.getText().toString();
                if(!codigoSalida.isEmpty()){
                    RegistrarSalida();
                } else {
                    Toast.makeText(SalidaProducto.this, "Campo Vacio\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCantidad = (EditText)findViewById(R.id.txtCantidadSalida);
        spCategoria = (Spinner)findViewById(R.id.spCategoriaSalida);
        spNombre = (Spinner)findViewById(R.id.spNombreSalida);
        btnRetirar = (Button)findViewById(R.id.btnRetirar);
        InhabilitarCampos();
        btnRetirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtCantidad.getText().toString().isEmpty()){
                    cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    if(productoSeleccionado.getUnidades()>=cantidad){
                        RegistrarSalidaProducto();
                    } else {
                        Toast.makeText(SalidaProducto.this, "Solo existen: "+String.valueOf(productoSeleccionado.getUnidades())+" unidades", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SalidaProducto.this, "Campos Vacios\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RegistrarSalidaProducto() {
        double valorTotal = cantidad*productoSeleccionado.getPrecio_Unitario();
        //http://localhost/BDremota/wsRegistroSalidaProducto.php?empresa=1&salida=1&producto=1&cantidad=2&pu=2&vt=4
        String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroSalidaProducto.php?empresa="+MainActivity.idEmpresa
                +"&salida="+idSalida+"&producto="+productoSeleccionado.getId_Producto()+"&cantidad="+cantidad
                +"&pu="+productoSeleccionado.getPrecio_Unitario()+"&vt="+valorTotal;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void RegistrarSalida() {
        String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroSalida.php?empresa="+MainActivity.idEmpresa+"&codigo="+codigoSalida;
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

    private void ConsultarProductos() {
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=6&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("salida")){
            ValidarRegistroSalida(response);
        } else if(response.has("categorias")){
            CargarCategorias(response);
        } else if(response.has("productos")){
            CargarProductos(response);
        } else if(response.has("salida_producto")){
            ValidarRegistroSP(response);
        } else if(response.has("producto")){
            ValidarActualizarDatos(response);
        }
    }

    private void ValidarActualizarDatos(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("producto");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(this, "No se Pudieron Actualizar los Datos", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidarRegistroSP(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("salida_producto");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    txtCantidad.setText("");
                    ObtenerListaProducto();
                    ObtenerListaCategoria();
                    ActualizarDatos();
                    break;
                case 0:
                    Toast.makeText(this, "Error al Registrar", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ActualizarDatos() {
        double cantidadfinal = productoSeleccionado.getUnidades()-cantidad;
        double preciofinal = (productoSeleccionado.getPrecio_Total()-productoSeleccionado.getPrecio_Unitario()*cantidad)/cantidadfinal;
        String url = "http://"+MainActivity.IP+"/BDremota/wsActualizarProducto.php?empresa="+MainActivity.idEmpresa+"&producto="
                +productoSeleccionado.getId_Producto()+"&unidades="+cantidadfinal+"&precio="+preciofinal+"&total="+(cantidadfinal*preciofinal);
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void ValidarRegistroSalida(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("salida");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    idSalida = jsonObject.getInt("id");
                    HabilitarCampos();
                    ConsultarCategorias();
                    break;
                case 0:
                    Toast.makeText(this, "Registro ya Existente\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(this, "Error al Registrar", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
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

    private void ObtenerListaProducto() {
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
                    productoSeleccionado.setId_Estado(-1);
                } else {
                    productoSeleccionado = listaProducto.get(i-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {}
    public void HabilitarCampos(){
        spCategoria.setEnabled(true);
        spNombre.setEnabled(true);
        txtCantidad.setEnabled(true);
        btnRetirar.setEnabled(true);
        txtCodigoSalida.setEnabled(false);
        btnRegistrar.setEnabled(false);
    }
    public void InhabilitarCampos(){
        spCategoria.setEnabled(false);
        spNombre.setEnabled(false);
        txtCantidad.setEnabled(false);
        btnRetirar.setEnabled(false);
    }
}
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

public class IngresoProducto extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    //variables de la activity
    private String codigo;
    private int cantidad;
    private double precioUnitario;
    private int idCategoriaSelect;
    private int idIngreso;

    //arreglos para mostrar la informacion en el spinner
    Categoria categoria = null;
    ArrayList<Categoria> listaCategoria;
    ArrayList<String> listaCategoriaFinal;
    Producto productoSeleccionado = new Producto();
    ArrayList<Producto> listaProducto;
    ArrayList<String> listaProductoFinal;

    //variables de la activity
    private EditText txtCodigoIngreso, txtPrecioUnitario, txtCantidad;
    private Spinner spCategoria, spNombre;
    private Button btnRegistrar, btnIngresar;

    //Objetos que permiten la conexion con los webService
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_producto);

        listaCategoria = new ArrayList<>();
        listaProducto = new ArrayList<>();
        //Instancia del objeto para la conexion remota
        requestQueue = Volley.newRequestQueue(this);
        //relacion logica-grafica
        txtCodigoIngreso = (EditText)findViewById(R.id.txtCodigoIngreso);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrarIngreso);
        //modificar el evento click
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validamos que los campos no se encuentren vacios para poder registrar
                codigo = txtCodigoIngreso.getText().toString();
                if(!codigo.isEmpty()){
                    RegistrarIngreso();
                } else {
                    Toast.makeText(IngresoProducto.this, "Campo Vacio\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //relacion logica-grafica
        spCategoria = (Spinner)findViewById(R.id.spCategoria);
        spNombre = (Spinner)findViewById(R.id.spNombreProducto);
        txtCantidad = (EditText)findViewById(R.id.txtCantidadEntrada);
        txtPrecioUnitario = (EditText)findViewById(R.id.txtPrecioUnitario);
        btnIngresar = (Button)findViewById(R.id.btnIngresarProducto);
        //llamada a la funcion para inhabilitar campos
        InhabilitarCampos();
        //modificar el evento click
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validamos que los campos no se encuentren vacios para poder registrar
                if(!txtPrecioUnitario.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty()){
                    cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    precioUnitario = Double.parseDouble(txtPrecioUnitario.getText().toString());
                    RegistrarIngresoProducto();
                } else {
                    Toast.makeText(IngresoProducto.this, "Campos Vacios\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //metodo para llamar al ws y registrar la accion de ingresar producto
    private void RegistrarIngresoProducto() {
        //llamada al WebService para registrar los datos, la respuesta la devolvera en el Onresponse()
        double valorTotal = cantidad*precioUnitario;
        String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroIngresoProducto.php?empresa="+MainActivity.idEmpresa
                +"&ingreso="+idIngreso+"&producto="+productoSeleccionado.getId_Producto()+"&cantidad="+cantidad+"&pu="+precioUnitario+"&vt="+valorTotal;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    //metodo para llamar al ws y registrar la accion de ingreso
    private void RegistrarIngreso() {
        //llamada al WebService para registrar los datos, la respuesta la devolvera en el Onresponse()
        String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroIngreso.php?empresa="+MainActivity.idEmpresa+"&codigo="+codigo;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    //metodo para llamar al ws y consultar las categorias
    private void ConsultarCategorias(){
        //llamada al WebService para registrar los datos, la respuesta la devolvera en el Onresponse()
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=5&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    //metodo para llamar al ws y consultar los productos
    private void ConsultarProductos() {
        //llamada al WebService para registrar los datos, la respuesta la devolvera en el Onresponse()
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=6&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    //metodo donde trataremos las respuestas del WebService
    @Override
    public void onResponse(JSONObject response) {
        //preguntamos si la respuesra es la que deseamos obtener
        if(response.has("ingreso")){
            ValidarRegistroIngreso(response);
        } else if(response.has("categorias")){
            CargarCategorias(response);
        } else if(response.has("productos")){
            CargarProductos(response);
        } else if(response.has("ingreso_producto")){
            ValidarRegistroIP(response);
        } else if(response.has("producto")){
            ValidarActualizarDatos(response);
        }
    }

    //valida que respuesta de la consulta nos devuelve el ws
    private void ValidarActualizarDatos(JSONObject response) {
        int respuesta;
        try {
            //obtenemos el objeto jsonArray del objeto que nos responde el ws
            JSONArray jsonArray = response.getJSONArray("producto");
            //obtenemos el primer elemento del arreglo
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            //obtenemos el dato cuya clave sea respuesta
            respuesta = jsonObject.getInt("respuesta");
            //validamos la respuesta
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

    private void ValidarRegistroIP(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("ingreso_producto");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    txtPrecioUnitario.setText("");
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
        double cantidadfinal = cantidad+productoSeleccionado.getUnidades();
        double preciofinal = (precioUnitario*cantidad+productoSeleccionado.getPrecio_Total())/cantidadfinal;
        String url = "http://"+MainActivity.IP+"/BDremota/wsActualizarProducto.php?empresa="+MainActivity.idEmpresa+"&producto="
                +productoSeleccionado.getId_Producto()+"&unidades="+cantidadfinal+"&precio="+preciofinal+"&total="+(cantidadfinal*preciofinal);
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void ValidarRegistroIngreso(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("ingreso");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    idIngreso = jsonObject.getInt("id");
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
                    productoSeleccionado.setId_Producto(-1);
                } else {
                    productoSeleccionado = listaProducto.get(i-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    //metodo donde se muestra la respuesta en caso de fallar la llamada al ws
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
    private void HabilitarCampos(){
        spCategoria.setEnabled(true);
        spNombre.setEnabled(true);
        txtPrecioUnitario.setEnabled(true);
        txtCantidad.setEnabled(true);
        btnIngresar.setEnabled(true);
        txtCodigoIngreso.setEnabled(false);
        btnRegistrar.setEnabled(false);
    }
    private void InhabilitarCampos(){
        spCategoria.setEnabled(false);
        spNombre.setEnabled(false);
        txtPrecioUnitario.setEnabled(false);
        txtCantidad.setEnabled(false);
        btnIngresar.setEnabled(false);
    }
}
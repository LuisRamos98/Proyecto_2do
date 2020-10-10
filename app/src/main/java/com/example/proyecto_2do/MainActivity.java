package com.example.proyecto_2do;

import Entidades.Empresa;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    //variables de la activity
    public static int idEmpresa = 0;
    public static String IP = "192.168.1.7";
    private Button login;
    private EditText txtUser, txtPass;
    Spinner sp_Empresas;
    private Empresa empresa = null;
    //arreglos para mostrar la informacion en el spinner
    private ArrayList<Empresa> listaEmpresas;
    private ArrayList<String> listafinal;
    //Objetos que permiten la conexion con los webService
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaEmpresas = new ArrayList<>();
        //Instancia del objeto para la conexion remota
        requestQueue = Volley.newRequestQueue(this);
        //relacion logica-grafica
        login = (Button)findViewById(R.id.btn_login);
        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPass = (EditText)findViewById(R.id.txtPass);
        sp_Empresas = (Spinner)findViewById(R.id.spEmpresa);
        //llamada a metodo para cargar en el spiner las empresas
        ConsultarEmpresas();
        //modificar el evento click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llamada al metodo para validar los datos en el login
                Validarusuario();
            }
        });
    }

    private void ConsultarEmpresas() {
        //llamada al WebService
        String url = "http://"+IP+"/BDremota/consultas.php?empresa="+MainActivity.idEmpresa+"&opcion=1";
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void Validarusuario() {
        String user, pass;
        //validamos que primero haya escogido una empresa antes de proceder a validar
        if(idEmpresa != 0 ){
            user = txtUser.getText().toString();
            pass = txtPass.getText().toString();
            if(!user.isEmpty() && !pass.isEmpty()){
                String url = "http://"+IP+"/BDremota/consultas.php?opcion=3&empresa="+idEmpresa+"&usuario="+user+"&pass="+pass;
                url = url.replace(" ","%20");
                //llamada al WebService
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
                requestQueue.add(jsonObjectRequest);
            } else {
                Toast.makeText(this, "Ingrese los Campos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Seleccione Una Empresa Por Favor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    //metodo donde trataremos las respuestas del WebService
    @Override
    public void onResponse(JSONObject response) {
        if(response.has("empresa")) {
            CargarEmpresas(response);
        } else if(response.has("Usuario")){
            ValidarRespuestaUsuario(response);
        }
    }

    private void ValidarRespuestaUsuario(JSONObject response) {
        try{
            //validar los datos deusario para el inicio de sesion
            JSONArray json = response.getJSONArray("Usuario");
            JSONObject jsonObject = json.getJSONObject(0);
            int id = jsonObject.getInt("id");
            if(id != 0){
                Intent sgt = new Intent(getApplicationContext(), Navegacion.class);
                startActivity(sgt);
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(this, "Datos Erroneos\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                txtUser.setText("");
                txtPass.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarEmpresas(JSONObject response){
        try{
            //Obtenemos el objeto JSONArray del objeto response que nos devuelve el WS
            JSONArray json = response.getJSONArray("empresa");
            //recorremos el arreglo y obtenemos los datos
            for (int i = 0; i < json.length(); i++) {
                empresa = new Empresa();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                empresa.setIdEmpresa(jsonObject.getInt("id"));
                empresa.setRucEmpresa(jsonObject.optString("ruc"));
                empresa.setNombreEmpresa(jsonObject.optString("nombre"));
                listaEmpresas.add(empresa);
            }
            //llamada al metodo para llenar el spinner
            ObtenerLista();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ObtenerLista() {
        listafinal = new ArrayList<String>();
        listafinal.add("Seleccione");
        for(int i=0; i<listaEmpresas.size(); i++){
            listafinal.add(listaEmpresas.get(i).getNombreEmpresa());
        }
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listafinal);
        sp_Empresas.setAdapter(adaptador);
        sp_Empresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    idEmpresa = listaEmpresas.get(i-1).getIdEmpresa();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
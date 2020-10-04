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

    public static int idEmpresa = 0;
    private Button login;
    private EditText txtUser, txtPass;
    Spinner sp_Empresas;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private Empresa empresa = null;
    private ArrayList<Empresa> listaEmpresas;
    private ArrayList<String> listafinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        listaEmpresas = new ArrayList<>();
        login = (Button)findViewById(R.id.btn_login);
        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPass = (EditText)findViewById(R.id.txtPass);
        sp_Empresas = (Spinner)findViewById(R.id.spEmpresa);
        ConsultarEmpresas();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validarusuario();
            }
        });
    }

    private void Validarusuario() {
        String user, pass;
        if(idEmpresa != 0){
            user = txtUser.getText().toString();
            pass = txtPass.getText().toString();
            String url = "http://192.168.1.9/BDremota/consultas.php?opcion=3&empresa="+idEmpresa+"&usuario="+user+"&pass="+pass;
            url = url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Seleccione Una Empresa Por Favor", Toast.LENGTH_SHORT).show();
        }
    }

    private void ConsultarEmpresas() {
        String url = "http://192.168.1.9/BDremota/consultas.php?opcion=1";
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if(response.has("empresa")) {
                JSONArray json = response.getJSONArray("empresa");
                for (int i = 0; i < json.length(); i++) {
                    empresa = new Empresa();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    empresa.setIdEmpresa(jsonObject.getInt("id"));
                    empresa.setRucEmpresa(jsonObject.optString("ruc"));
                    empresa.setNombreEmpresa(jsonObject.optString("nombre"));
                    listaEmpresas.add(empresa);
                }
                obtenerlista();
            } else if(response.has("Usuario")){
                JSONArray json = response.getJSONArray("Usuario");
                JSONObject jsonObject = json.getJSONObject(0);
                int id = jsonObject.getInt("id");
                if(id != 0){
                    Intent sgt = new Intent(getApplicationContext(), Navegacion.class);
                    startActivity(sgt);
                } else {
                    Toast.makeText(this, "Datos Erroneos\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerlista() {
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
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
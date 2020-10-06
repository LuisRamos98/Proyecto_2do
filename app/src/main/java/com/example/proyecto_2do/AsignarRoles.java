package com.example.proyecto_2do;

import Entidades.Rol;
import Entidades.Usuario;
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

public class AsignarRoles extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Spinner spRol1, spRol2, spUsuario;
    private Button btnAsignar;

    int idRolViejo, idRolNuevo, idUsuario;
    Rol rol = null;
    Usuario usuario = null;
    ArrayList<Rol> listaRoles;
    ArrayList<String> listaRolesFinal;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<String> listaUsuariosFinal;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_roles);
        requestQueue = Volley.newRequestQueue(this);
        spRol1 = (Spinner)findViewById(R.id.spRol1);
        spRol2 = (Spinner)findViewById(R.id.spRol2);
        spUsuario = (Spinner)findViewById(R.id.spUsuario);
        btnAsignar = (Button)findViewById(R.id.btnAsignarRol);
        listaRoles = new ArrayList<>();
        listaUsuarios = new ArrayList<>();
        ConsultarRoles();
        //ConsultarUsuarios();
        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idRolViejo==-1 || idRolNuevo==-1 || idUsuario==-1){
                    ActualizarRol();
                }
            }
        });
    }

    private void ActualizarRol() {

    }

    private void ConsultarUsuarios() {
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=4&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void ConsultarRoles() {
        String url = "http://"+MainActivity.IP+"/BDremota/consultas.php?opcion=2&empresa="+MainActivity.idEmpresa;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("roles")){
            CargarRoles(response);
        } else if(response.has("usuario")){
            CargarUsuarios(response);
        }
    }

    private void CargarRoles(JSONObject response) {
        try{
            JSONArray jsonArray= response.getJSONArray("roles");
            for(int i=0; i<jsonArray.length(); i++){
                rol = new Rol();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                rol.setId_Empresa(jsonObject.getInt("empresa"));
                rol.setId_Rol(jsonObject.getInt("id"));
                rol.setDescripcion_Rol(jsonObject.getString("rol"));
                listaRoles.add(rol);
            }
            ObtenerListaRoles();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarUsuarios(JSONObject response) {
        listaUsuarios.clear();
        try {
            JSONArray jsonArray= response.getJSONArray("usuario");
            for(int i=0; i<jsonArray.length(); i++){
                usuario = new Usuario();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getInt("rol") == idRolViejo){
                    usuario.setId_Empresa(jsonObject.getInt("empresa"));
                    usuario.setId_Usuario(jsonObject.getInt("id"));
                    usuario.setEstado(jsonObject.getInt("estado"));
                    usuario.setCI(jsonObject.getString("ci"));
                    usuario.setNombre(jsonObject.getString("nombre"));
                    usuario.setApellido(jsonObject.getString("apellido"));
                    usuario.setCorreo(jsonObject.getString("correo"));
                    usuario.setUser(jsonObject.getString("user"));
                    usuario.setId_Rol(jsonObject.getInt("rol"));
                    listaUsuarios.add(usuario);
                }
            }
            ObtenerListaUsuario();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ObtenerListaRoles() {
        listaRolesFinal = new ArrayList<String>();
        listaRolesFinal.add("Seleccione");
        for(int i=0; i<listaRoles.size(); i++){
            listaRolesFinal.add(listaRoles.get(i).getDescripcion_Rol());
        }
        ArrayAdapter<CharSequence> adaptador =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaRolesFinal);
        spRol1.setAdapter(adaptador);
        spRol2.setAdapter(adaptador);
        spRol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    idRolViejo = listaRoles.get(i-1).getId_Rol();
                    Toast.makeText(AsignarRoles.this, String.valueOf(idRolViejo), Toast.LENGTH_SHORT).show();
                    ConsultarUsuarios();
                } else {
                    idRolViejo = -1;
                    Toast.makeText(AsignarRoles.this, String.valueOf(idRolViejo), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spRol2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    idRolNuevo = listaRoles.get(i-1).getId_Rol();
                    Toast.makeText(AsignarRoles.this, String.valueOf(idRolNuevo), Toast.LENGTH_SHORT).show();
                } else {
                    idRolNuevo = -1;
                    Toast.makeText(AsignarRoles.this, String.valueOf(idRolNuevo), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void ObtenerListaUsuario() {
        listaUsuariosFinal = new ArrayList<String>();
        listaUsuariosFinal.add("Seleccione");
        for(int i=1; i<listaUsuarios.size(); i++){
            listaUsuariosFinal.add("CI: "+listaUsuarios.get(i).getCI()
                    +" - Usuario: "+listaUsuarios.get(i).getUser());
        }
        ArrayAdapter<CharSequence> adaptador =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaUsuariosFinal);
        spUsuario.setAdapter(adaptador);
        spUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    idUsuario = listaUsuarios.get(i-1).getId_Usuario();
                    Toast.makeText(AsignarRoles.this, String.valueOf(idUsuario), Toast.LENGTH_SHORT).show();
                } else {
                    idUsuario = -1;
                    Toast.makeText(AsignarRoles.this, String.valueOf(idUsuario)+" aqui", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
}
package com.example.proyecto_2do;

import Entidades.Empresa;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class AgregarUsuario extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText txtNombre, txtApellido, txtCedula, txtCorreo, txtUser, txtPass;
    private Button btnRegistrar;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);
        requestQueue = Volley.newRequestQueue(this);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtApellido = (EditText)findViewById(R.id.txtApellido);
        txtCedula = (EditText)findViewById(R.id.txtCedula);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtUser = (EditText)findViewById(R.id.txtUsuario);
        txtPass = (EditText)findViewById(R.id.txtPass);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrarUsuario);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUsuario();
            }
        });
    }

    private void RegistrarUsuario() {
        String nombre, apellido, ci, correo, user, pass;
        nombre = txtNombre.getText().toString();
        apellido = txtApellido.getText().toString();
        ci = txtCedula.getText().toString();
        correo = txtCorreo.getText().toString();
        user = txtUser.getText().toString();
        pass = txtPass.getText().toString();
        if(!nombre.isEmpty() && !apellido.isEmpty() && !ci.isEmpty() && !correo.isEmpty() && !user.isEmpty() && !pass.isEmpty()){
            String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroUsuario.php?empresa="
                    +MainActivity.idEmpresa+"&cedula="+ci+"&nombre="+nombre+"&apellido="+apellido+"&correo="
                    +correo+"&user="+user+"&pass="+pass;
            System.out.println(url);
            url = url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Campo Vacio\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("Usuario")){
            ValidarRegistroUsuario(response);
        }
    }

    private void ValidarRegistroUsuario(JSONObject response) {
        int respuesta;
        try {
            JSONArray jsonArray = response.getJSONArray("Usuario");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    LimpiarCampos();
                    break;
                case 0:
                    Toast.makeText(this, "Registro ya Existente\nCampos Cedula o Usuario Repetidos", Toast.LENGTH_SHORT).show();
                    txtUser.setText("");
                    txtCedula.setText("");
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

    private void LimpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtUser.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
}
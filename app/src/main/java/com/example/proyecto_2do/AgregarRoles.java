package com.example.proyecto_2do;

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

public class AgregarRoles extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText txtRol;
    private Button btnRegistrar;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_roles);
        requestQueue = Volley.newRequestQueue(this);
        txtRol = (EditText)findViewById(R.id.txtRol);
        btnRegistrar = (Button)findViewById(R.id.btn_RegistrarRol);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarRol();
            }
        });
    }

    private void RegistrarRol() {
        String rol = txtRol.getText().toString();
        if(!rol.isEmpty()){
            String url = "http://192.168.1.9/BDremota/wsJSONRegistroRol.php?empresa="+MainActivity.idEmpresa+"&rol="+rol;
            url = url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Campo Vacio\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("rol")){
            ValidarRegistroRol(response);
        }
    }

    private void ValidarRegistroRol(JSONObject response) {
        int id_rol;
        try {
            JSONArray jsonArray = response.getJSONArray("rol");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            id_rol = jsonObject.getInt("id");
            if(id_rol != 0){
                Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                txtRol.setText("");
            } else {
                Toast.makeText(this, "Registro ya Existente\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
}
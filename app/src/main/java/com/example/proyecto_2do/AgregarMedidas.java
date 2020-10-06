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

public class AgregarMedidas extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText txtMedida;
    private Button btnRegistrar;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medidas);
        requestQueue = Volley.newRequestQueue(this);
        txtMedida = (EditText)findViewById(R.id.txtMedida);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrarMedida);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarMedida();
            }
        });
    }

    private void RegistrarMedida() {
        String medida = txtMedida.getText().toString();
        if(!medida.isEmpty()){
            String url = "http://"+MainActivity.IP+"/BDremota/wsRegistroMedida.php?empresa="+MainActivity.idEmpresa+"&medida="+medida;
            url = url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Campo Vacio\nIntente Nuevamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response.has("medida")){
            ValidarRegistroMedida(response);
        }
    }

    private void ValidarRegistroMedida(JSONObject response) {
        int respuesta;
        try{
            JSONArray jsonArray = response.getJSONArray("medida");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            respuesta = jsonObject.getInt("respuesta");
            switch (respuesta){
                case 1:
                    Toast.makeText(this, "Registro Con Exito", Toast.LENGTH_SHORT).show();
                    txtMedida.setText("");
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
}
package com.example.proyecto_2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button)findViewById(R.id.btn_login);

    }

    public void Siguiente (View view){
        Intent Siguiente = new Intent(this,Navegacion.class);
        startActivity(Siguiente);

    }

    // Ayuda :,v
    //    // segundo intento
    //    //AMO A MI NOVIA UWU
    // :V xd
}
package com.example.proyecto_2do.ui.mantenimiento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyecto_2do.AgregarMedidas;
import com.example.proyecto_2do.AgregarRoles;
import com.example.proyecto_2do.AgregarUsuario;
import com.example.proyecto_2do.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageButton agrNuevoRol;
    private ImageButton agrNuevaMedida;
    private ImageButton agrNuevoUsuario;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        agrNuevoRol = (ImageButton)root.findViewById(R.id.btnKardex);
        agrNuevaMedida = (ImageButton)root.findViewById(R.id.btnAgrMedida);
        agrNuevoUsuario = (ImageButton)root.findViewById(R.id.btnAgrUser);

        agrNuevoRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAgregarRoles();
            }
        });

        agrNuevaMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAgregarMedida();
            }
        });

        agrNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAgregarUsuario();
            }
        });

        return root;
    }

    public void goToAgregarRoles (){
        Intent roles = new Intent(getContext(), AgregarRoles.class);
        startActivity(roles);
    }

    public void goToAgregarMedida (){
        Intent medida = new Intent(getContext(), AgregarMedidas.class);
        startActivity(medida);
    }

    public void goToAgregarUsuario (){
        Intent medida = new Intent(getContext(), AgregarUsuario.class);
        startActivity(medida);
    }



}
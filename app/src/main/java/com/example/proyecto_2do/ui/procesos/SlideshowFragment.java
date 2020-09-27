package com.example.proyecto_2do.ui.procesos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyecto_2do.AgregarRoles;
import com.example.proyecto_2do.DevolucionProducto;
import com.example.proyecto_2do.IngresoProducto;
import com.example.proyecto_2do.R;
import com.example.proyecto_2do.SalidaProducto;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ImageButton agrProducto;
    private ImageButton salProducto;
    private ImageButton devProducto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        agrProducto = (ImageButton)root.findViewById(R.id.btnIngreso);
        salProducto = (ImageButton)root.findViewById(R.id.btnSalida);
        devProducto = (ImageButton)root.findViewById(R.id.btnDevolucion);

        agrProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToIngresarProducto();
            }
        });
        salProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSalidaProducto ();
            }
        });
        devProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDevProducto ();
            }
        });

        return root;
    }

    public void goToIngresarProducto (){
        Intent roles = new Intent(getContext(), IngresoProducto.class);
        startActivity(roles);
    }

    public void goToSalidaProducto (){
        Intent roles = new Intent(getContext(), SalidaProducto.class);
        startActivity(roles);
    }

    public void goToDevProducto (){
        Intent roles = new Intent(getContext(), DevolucionProducto.class);
        startActivity(roles);
    }
}
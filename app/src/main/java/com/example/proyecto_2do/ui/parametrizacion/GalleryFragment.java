package com.example.proyecto_2do.ui.parametrizacion;

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
import com.example.proyecto_2do.AsignarRoles;
import com.example.proyecto_2do.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private ImageButton asgRoles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        asgRoles = (ImageButton)root.findViewById(R.id.asgRoles);

        asgRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAsignarRoles();
            }
        });

        return root;
    }

    public void goToAsignarRoles(){
        Intent roles = new Intent(getContext(), AsignarRoles.class);
        startActivity(roles);
    }
}
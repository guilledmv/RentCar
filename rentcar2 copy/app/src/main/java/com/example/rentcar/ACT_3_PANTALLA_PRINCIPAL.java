package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class ACT_3_PANTALLA_PRINCIPAL extends AppCompatActivity {

    // Variables visibles en pantalla
    private Button btn_historial,btn_realizar_reserva,btn_modificar_reserva,btn_borrar_reserva,btn_editar_perfil;
    private ProgressBar progressBar;
    /*
    BOTONES DE MODIFICAR Y ELIMINAR RESERVA--> 2ºCICLO, NO HACER
     */

    // Variable email
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_3_pantalla_principal);
        btn_historial = findViewById(R.id.btn_historial_ACT_3);
        btn_realizar_reserva = findViewById(R.id.btn_realizar_reserva_ACT_3);
        btn_modificar_reserva = findViewById(R.id.btn_modifica_reserva_ACT_3);
        btn_borrar_reserva = findViewById(R.id.btn_borrar_reserva_ACT_3);
        btn_editar_perfil = findViewById(R.id.btn_editar_perfil_ACT_3);
        progressBar = findViewById(R.id.pb_ACT_3_PANTALLA_PRINCIPAL);
        email = getIntent().getStringExtra("email");

        // Click en editar perfil de usuario--> Abre formulario registro modificable
        btn_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this, ACT_5_EDITAR_PERFIL_USUARIO.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
        // Click en realizar reserva--> Abre formulario
        btn_realizar_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // Nos vamos a la actividad ACT_4_FORMULARIO_RESERVA
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this,ACT_4_FORMULARIO_RESERVA.class);
                intent.putExtra("email",email);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
            }
        });
        // Click en modificar reserva--> busca numero y abre formulario
        btn_modificar_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nos vamos a la actividad ACT_4_FORMULARIO_RESERVA
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this,ACT_4_MODIFICAR_RESERVA.class);
                intent.putExtra("email",email);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
            }
        });
        // Click en historial reserva--> Abre una lista con los numeros de reserva asociados a un cliente
        btn_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this, ACT_4_HISTORIAL_BUSQUEDA.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
        // Click en borrar reserva--> Abre una lista con los numeros de reserva asociados a un cliente
        btn_borrar_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this, ACT_5_BORRAR_RESERVA.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

    }
}
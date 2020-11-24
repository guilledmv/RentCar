package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class ACT_3_PANTALLA_PRINCIPAL extends AppCompatActivity {

    // Variables visibles en pantalla
    private Button btn_historial,btn_realizar_reserva,btn_modificar_reserva,btn_borrar_reserva;
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
        progressBar = findViewById(R.id.pb_ACT_3_PANTALLA_PRINCIPAL);
        email = getIntent().getStringExtra("email");
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
        btn_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACT_3_PANTALLA_PRINCIPAL.this,ACT_4_HISTORIAL.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }
}
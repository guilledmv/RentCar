package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ACT_4_HISTORIAL extends AppCompatActivity {

    // Variables para obtener los datos
    private String email;
    // Variables de la pantalla
    private TextView tv_title_numero_reserva1,tv_num_reserva1,tv_title_nombre_reserva1,tv_nombre_reserva1
            , tv_datos_reserva1, tv_tittle_email_reserva1, tv_email_reserva1, tv_tittle_inicio_reserva1
            , tv_inicio_reserva1, tv_tittle_fin_reserva1, tv_fin_reserva1, tv_tittle_tipo_reserva1
            , tv_tipo_reserva1, tv_tittle_importe_reserva1, tv_importe_reserva1, tv_linea_separadora1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_historial);
        email = getIntent().getStringExtra("email");
        tv_title_numero_reserva1 = findViewById(R.id.tv_title_numero_reserva1);
        tv_num_reserva1 = findViewById(R.id.tv_num_reserva1);
        tv_title_nombre_reserva1 = findViewById(R.id.tv_title_nombre_reserva1);
        tv_nombre_reserva1 = findViewById(R.id.tv_nombre_reserva1);
        tv_datos_reserva1 = findViewById(R.id.tv_datos_reserva1);
        tv_tittle_email_reserva1 = findViewById(R.id.tv_tittle_email_reserva1);
        tv_email_reserva1 = findViewById(R.id.tv_email_reserva1);
        tv_tittle_inicio_reserva1 = findViewById(R.id.tv_tittle_inicio_reserva1);
        tv_inicio_reserva1 = findViewById(R.id.tv_inicio_reserva1);
        tv_tittle_fin_reserva1 = findViewById(R.id.tv_tittle_fin_reserva1);
        tv_fin_reserva1 = findViewById(R.id.tv_fin_reserva1);
        tv_tittle_tipo_reserva1 = findViewById(R.id.tv_tittle_tipo_reserva1);
        tv_tipo_reserva1 = findViewById(R.id.tv_tipo_reserva1);
        tv_tittle_importe_reserva1 = findViewById(R.id.tv_tittle_importe_reserva1);
        tv_importe_reserva1 = findViewById(R.id.tv_importe_reserva1);
        tv_linea_separadora1 = findViewById(R.id.tv_linea_separadora1);
    }
}
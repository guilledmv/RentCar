package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rentcar.R;

public class ACT_2_LOGIN_OPTIONS extends AppCompatActivity {
    // Variables usadas en la pantalla
    private Button btn_login_ACT_2,btn_create_user_ACT_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establezco el layout sobre el que pertenece esta clase
        setContentView(R.layout.act_2_login_options);
        // Asocio las variables con su id del layout
        btn_login_ACT_2 = findViewById(R.id.btn_login_ACT_2);
        btn_create_user_ACT_2 = findViewById(R.id.btn_crear_usuario_ACT_2);
        // Cuando pulsas el boton de login--> Te vas a otra actividad
        btn_create_user_ACT_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Paso a la actividad de creación de usuario
                Intent crear_usuario = new Intent(ACT_2_LOGIN_OPTIONS.this, ACT_2_CREATE_USER.class);
                startActivity(crear_usuario);
            }
        });
        btn_login_ACT_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Paso a la actividad de usuario
                Intent usuario = new Intent(ACT_2_LOGIN_OPTIONS.this, ACT_2_LOGIN.class);
                startActivity(usuario);
            }
        });
    }
}
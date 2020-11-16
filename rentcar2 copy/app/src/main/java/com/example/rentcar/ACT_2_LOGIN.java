package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ACT_2_LOGIN extends AppCompatActivity {

    // Variables que voy a utilizar para iniciar sesion
    private EditText edt_email_ACT_2_LOGIN, edt_password_ACT_2_LOGIN;
    private Button btn_iniciar_sesion_ACT_2_LOGIN;
    private ProgressBar pb_login;
    // Variables de firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;

    // Variable email
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_2_login);
        mAuth = FirebaseAuth.getInstance();
        edt_email_ACT_2_LOGIN = findViewById(R.id.edt_email_ACT_2_LOGIN);
        edt_password_ACT_2_LOGIN = findViewById(R.id.edt_password_ACT_2_LOGIN);
        btn_iniciar_sesion_ACT_2_LOGIN = findViewById(R.id.btn_login_ACT_2_LOGIN);
        pb_login = findViewById(R.id.pb_ACT_2_LOGIN);
        btn_iniciar_sesion_ACT_2_LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_login.setVisibility(View.VISIBLE);
                // Comprobamos que el email y la contraseña tienen un formato correctoy y no estan vacíos
                if (edt_email_ACT_2_LOGIN.getText().toString().isEmpty() || edt_password_ACT_2_LOGIN.getText().toString().isEmpty()) {
                    Toast.makeText(ACT_2_LOGIN.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    pb_login.setVisibility(View.INVISIBLE);
                } else {
                    email = edt_email_ACT_2_LOGIN.getText().toString();
                    mAuth.signInWithEmailAndPassword(edt_email_ACT_2_LOGIN.getText().toString(), edt_password_ACT_2_LOGIN.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            pb_login.setVisibility(View.INVISIBLE);
                            Toast.makeText(ACT_2_LOGIN.this, "Inicio de sesion realizado con éxito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ACT_2_LOGIN.this, ACT_3_PANTALLA_PRINCIPAL.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ACT_2_LOGIN.this, "Inicio de sesion incorrecto, por favor vuelve a introducir los datos", Toast.LENGTH_SHORT).show();
                            pb_login.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }
}
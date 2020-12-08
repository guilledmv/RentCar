package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ACT_4_MODIFICAR_RESERVA extends AppCompatActivity {

    // Variables de firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;
    // Variables locales
    private String email, id_reserva;
    private Button btn_buscar;
    private TextView intro;
    private EditText edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_modificar_reserva);
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
        intro = findViewById(R.id.textView);
        edt = findViewById(R.id.id_reserva);
        email = getIntent().getStringExtra("email");
        id_reserva = edt.getText().toString();
        btn_buscar.findViewById(R.id.btn_buscar_ACT_4);
        btn_buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email_validation(email, cloudReference, id_reserva);
            }
        });


    }
    private void email_validation (String email, FirebaseFirestore cloudReference, String id_reserva){
        cloudReference.collection("Usuarios").document(email)
                .collection("Reservas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qdsp) {
                if (qdsp.equals(id_reserva)) {
                    Intent intent = new Intent(ACT_4_MODIFICAR_RESERVA.this, ACT_5_MODIFICAR_FORMULARIO.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id_reserva", id_reserva);
                    startActivity(intent);
                } else {
                    Toast.makeText(ACT_4_MODIFICAR_RESERVA.this, "ID de la reserva inválido.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_MODIFICAR_RESERVA.this, "Fallo al conectar con la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
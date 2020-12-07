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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;


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
        btn_buscar = findViewById(R.id.btn_buscar_ACT_4);
        email = getIntent().getStringExtra("email");
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_reserva = edt.getText().toString();
                email_validation(email, cloudReference, id_reserva);
            }
        });
    }

    private void email_validation(String email, FirebaseFirestore cloudReference, String id_reserva) {
        cloudReference.collection("Usuarios").document(email)
                .collection("Reservas").document(id_reserva).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Toast.makeText(ACT_4_MODIFICAR_RESERVA.this, "Reserva encontrada con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ACT_4_MODIFICAR_RESERVA.this, ACT_5_MODIFICAR_FORMULARIO.class);
                    intent.putExtra("email",email);
                    intent.putExtra("id_reserva",id_reserva);
                    startActivity(intent);
                } else {
                    Toast.makeText(ACT_4_MODIFICAR_RESERVA.this,"Error, reserva inexistente",Toast.LENGTH_SHORT).show();
                    edt.setText(null);
                }
            }
        });
    }
}
package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ACT_5_BORRAR_RESERVA extends AppCompatActivity {

    // VARIABLES VISIBLES EN PANTALLA
    private TextView tv_tittle_numero_reserva_ACT_5_BORRAR_RESERVA;
    private EditText edt_numero_reserva_ACT_5_BORRAR_RESERVA;
    private Button btn_borrar_reserva_ACT_5;
    private ProgressBar pb_ACT_5_BORRAR_RESERVA;

    // VARIABLES FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;

    // VARIABLES MANEJO DE DATOS
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_5_borrar_reserva);
        tv_tittle_numero_reserva_ACT_5_BORRAR_RESERVA = findViewById(R.id.tv_tittle_numero_reserva_ACT_5_BORRAR_RESERVA);
        edt_numero_reserva_ACT_5_BORRAR_RESERVA = findViewById(R.id.edt_numero_reserva_ACT_5_BORRAR_RESERVA);
        btn_borrar_reserva_ACT_5 = findViewById(R.id.btn_borrar_reserva_ACT_5);
        pb_ACT_5_BORRAR_RESERVA = findViewById(R.id.pb_ACT_5_BORRAR_RESERVA);
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");

        // Si clickamos sobre el boton de borrar reserva

        btn_borrar_reserva_ACT_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_numero_reserva_ACT_5_BORRAR_RESERVA.getText().toString().isEmpty()){
                    buscaYEliminaReserva(cloudReference,email,edt_numero_reserva_ACT_5_BORRAR_RESERVA.getText().toString());
                } else {
                    pb_ACT_5_BORRAR_RESERVA.setVisibility(View.VISIBLE);
                    Toast.makeText(ACT_5_BORRAR_RESERVA.this,"Debes rellenar el campo numero de reserva",Toast.LENGTH_SHORT).show();
                    pb_ACT_5_BORRAR_RESERVA.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    // METODO AUXILIAR QUE BUSCAR UNA RESERVA INTRODUCIDA POR PANTALLA EN LAS RESERVAS DEL CLIENTE
    private void buscaYEliminaReserva(FirebaseFirestore cloudReference,String email, String numeroReservaPantalla){
        pb_ACT_5_BORRAR_RESERVA.setVisibility(View.VISIBLE);
        // COMPROBAMOS QUE LA RESERVA EXISTE
        cloudReference.collection("Usuarios").document(email).collection("Reservas").document(numeroReservaPantalla).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.getString("numero reserva").equals(numeroReservaPantalla)) {
                        // ELIMINAMOS LA RESERVA
                        cloudReference.collection("Usuarios").document(email).collection("Reservas").document(numeroReservaPantalla).delete();
                        pb_ACT_5_BORRAR_RESERVA.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_5_BORRAR_RESERVA.this, "Reserva eliminada con éxito", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pb_ACT_5_BORRAR_RESERVA.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_5_BORRAR_RESERVA.this, "Reserva inexistente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
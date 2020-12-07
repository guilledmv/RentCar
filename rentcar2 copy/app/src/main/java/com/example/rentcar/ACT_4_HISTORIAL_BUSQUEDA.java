package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ACT_4_HISTORIAL_BUSQUEDA extends AppCompatActivity {

    // VARIABLES PANTALLA
    private ListView lv_ACT_4_HISTORIAL_BUSQUEDA;
    private ArrayAdapter adapter;

    // VARIABLES FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;

    // VARIABLES MANEJO DE DATOS
    private String email;
    private ArrayList<String> listaNumsReserva = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_historial_busqueda);
        lv_ACT_4_HISTORIAL_BUSQUEDA = findViewById(R.id.lv_ACT_4_HISTORIAL_BUSQUEDA);
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");
        añadeNumerosReservaALista(cloudReference, email);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNumsReserva);
        lv_ACT_4_HISTORIAL_BUSQUEDA.setAdapter(adapter);
        lv_ACT_4_HISTORIAL_BUSQUEDA.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lv_ACT_4_HISTORIAL_BUSQUEDA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String numSeleccionado = listaNumsReserva.get(position);
                Intent intent = new Intent(ACT_4_HISTORIAL_BUSQUEDA.this, ACT_4_HISTORIAL_FACTURA.class);
                intent.putExtra("email", email);
                intent.putExtra("numero", numSeleccionado);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // METODO AUXILIAR QUE AÑADE LOS NUMEROS DE RESERVA A UNA LISTA
    private void añadeNumerosReservaALista(FirebaseFirestore cloudReference, String email) {
        // Instruccion para acceder a los datos que necesitamos
        cloudReference.collection("Usuarios").document(email).collection("Reservas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Iterator<QueryDocumentSnapshot> it = task.getResult().iterator();
                    while (it.hasNext()) {
                        String elem = it.next().getData().get("numero reserva").toString();
                        Toast.makeText(ACT_4_HISTORIAL_BUSQUEDA.this, elem, Toast.LENGTH_SHORT).show();
                        listaNumsReserva.add(elem);
                    }
                }
            }
        });
        String h = Integer.toString(listaNumsReserva.size());
        Toast.makeText(ACT_4_HISTORIAL_BUSQUEDA.this,h,Toast.LENGTH_SHORT).show();
    }
}
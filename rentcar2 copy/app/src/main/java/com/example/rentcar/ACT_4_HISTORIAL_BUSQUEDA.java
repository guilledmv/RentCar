package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ACT_4_HISTORIAL_BUSQUEDA extends AppCompatActivity {

    // VARIABLES PANTALLA
    private ListView lv_ACT_4_HISTORIAL_BUSQUEDA;
    private ArrayAdapter adapter;

    // VARIABLES FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;

    // VARIABLES MANEJO DE DATOS
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_historial_busqueda);
        lv_ACT_4_HISTORIAL_BUSQUEDA = findViewById(R.id.lv_ACT_4_HISTORIAL_BUSQUEDA);
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,añadeNumerosReservaALista(cloudReference,email));
        lv_ACT_4_HISTORIAL_BUSQUEDA.setAdapter(adapter);
    }

    // METODO AUXILIAR QUE AÑADE LOS NUMEROS DE RESERVA A UNA LISTA
    private ArrayList<String> añadeNumerosReservaALista(FirebaseFirestore cloudReference,String email) {
        ArrayList<String> num = new ArrayList<String>();
        // Instruccion para acceder a los datos que necesitamos
        cloudReference.collection("Usuarios").document(email).collection("Reservas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Iterator<QueryDocumentSnapshot> it = task.getResult().iterator();
                    while(it.hasNext()){
                        String elem = it.next().getData().get("numero reserva").toString();
                        Toast.makeText(ACT_4_HISTORIAL_BUSQUEDA.this,elem,Toast.LENGTH_SHORT).show();
                        num.add(elem);
                    }
                }
            }
        });
        String h = Integer.toString(num.size());
        Toast.makeText(ACT_4_HISTORIAL_BUSQUEDA.this,h,Toast.LENGTH_SHORT).show();
        return num;
    }
}
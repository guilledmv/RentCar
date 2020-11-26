package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class ACT_4_HISTORIAL extends AppCompatActivity {

    // Variables para obtener los datos
    private String email;
    // Variables de la pantalla
    private TextView tv_title_numero_reserva1,tv_num_reserva1,tv_title_nombre_reserva1,tv_nombre_reserva1
            , tv_datos_reserva1, tv_tittle_email_reserva1, tv_email_reserva1, tv_tittle_inicio_reserva1
            , tv_inicio_reserva1, tv_tittle_fin_reserva1, tv_fin_reserva1, tv_tittle_tipo_reserva1
            , tv_tipo_reserva1, tv_tittle_importe_reserva1, tv_importe_reserva1, tv_linea_separadora1
            , tv_tittle_matricula_reserva1,tv_matricula_reserva1,tv_title_numero_reserva2,tv_num_reserva2,
            tv_title_nombre_reserva2,tv_nombre_reserva2
            , tv_datos_reserva2, tv_tittle_email_reserva2, tv_email_reserva2, tv_tittle_inicio_reserva2
            , tv_inicio_reserva2, tv_tittle_fin_reserva2, tv_fin_reserva2, tv_tittle_tipo_reserva2
            , tv_tipo_reserva2, tv_tittle_importe_reserva2, tv_importe_reserva2, tv_linea_separadora2
            , tv_tittle_matricula_reserva2,tv_matricula_reserva2;
    // Variables firebase
    private FirebaseAuth mAuth;
    // Variable para almacenar en la base de datos cloudFirebase
    private FirebaseFirestore cloudReference;

    // Constantes
    private final String NOMBRE = "nombre";
    private final String EMAIL = "email";
    private final String INICIO = "inicio reserva";
    private final String FIN = "fin reserva";
    private final String MATRICULA = "matricula";
    private final String TIPO = "tipo";
    private final String IMPORTE = "importe";

    // Lista con los datos que tiene que leer
    private List<String> datosReserva = Arrays.asList(NOMBRE,EMAIL,INICIO,FIN,MATRICULA,TIPO,IMPORTE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_historial);
        email = getIntent().getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
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
        tv_tittle_matricula_reserva1 = findViewById(R.id.tv_tittle_matricula_reserva1);
        tv_matricula_reserva1 = findViewById(R.id.tv_matricula_reserva1);
        tv_title_numero_reserva2 = findViewById(R.id.tv_title_numero_reserva2);
        tv_num_reserva2 = findViewById(R.id.tv_num_reserva2);
        tv_title_nombre_reserva2 = findViewById(R.id.tv_title_nombre_reserva2);
        tv_nombre_reserva2 = findViewById(R.id.tv_nombre_reserva2);
        tv_datos_reserva2 = findViewById(R.id.tv_datos_reserva2);
        tv_tittle_email_reserva2 = findViewById(R.id.tv_tittle_email_reserva2);
        tv_email_reserva2 = findViewById(R.id.tv_email_reserva2);
        tv_tittle_inicio_reserva2 = findViewById(R.id.tv_tittle_inicio_reserva2);
        tv_inicio_reserva2 = findViewById(R.id.tv_inicio_reserva2);
        tv_tittle_fin_reserva2 = findViewById(R.id.tv_tittle_fin_reserva2);
        tv_fin_reserva2 = findViewById(R.id.tv_fin_reserva2);
        tv_tittle_tipo_reserva2 = findViewById(R.id.tv_tittle_tipo_reserva2);
        tv_tipo_reserva2 = findViewById(R.id.tv_tipo_reserva2);
        tv_tittle_importe_reserva2 = findViewById(R.id.tv_tittle_importe_reserva2);
        tv_importe_reserva2 = findViewById(R.id.tv_importe_reserva2);
        tv_tittle_matricula_reserva2 = findViewById(R.id.tv_tittle_matricula_reserva2);
        tv_matricula_reserva2 = findViewById(R.id.tv_matricula_reserva2);
        compruebaExistenReservas(cloudReference);
    }
    // Metodo auxiliar que comprueba si tenemos 1 reserva
    private void compruebaExistenReservas(FirebaseFirestore cloudReference){
        cloudReference.collection("Usuarios").document(email).collection("Datos personales").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.contains("numero reserva1")) {
                    tv_num_reserva1.setText(documentSnapshot.get("numero reserva1").toString());
                    leeDatosReserva1(cloudReference);
                } else {
                    tv_num_reserva1.setText("No disponible");
                }
                if(documentSnapshot.contains("numero reserva2")) {
                    tv_num_reserva1.setText(documentSnapshot.get("numero reserva2").toString());
                    leeDatosReserva2(cloudReference);
                } else {
                    tv_num_reserva2.setText("No disponible");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_HISTORIAL.this,"Error al leer los datos de las reservas",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo auxiliar que devuelve todos los datos de la reserva1
    private void leeDatosReserva1(FirebaseFirestore cloudReference){
        cloudReference.collection("Usuarios").document(email).collection("Reservas").document(tv_num_reserva1.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                escribeDatosReserva1(documentSnapshot,datosReserva);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_HISTORIAL.this,"Error al escribir los datos de las reservas",Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Metodo auxiliar que devuelve todos los datos de la reserva1
    private void leeDatosReserva2(FirebaseFirestore cloudReference){
        cloudReference.collection("Usuarios").document(email).collection("Reservas").document(tv_num_reserva2.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                escribeDatosReserva2(documentSnapshot,datosReserva);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_HISTORIAL.this,"Error al escribir los datos de las reservas",Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Metodo auxiliar que escribe
    private void escribeDatosReserva1(DocumentSnapshot documentSnapshot,List<String> list){
        int i = 0;
        int len = list.size();
        while (i < len){
            if(documentSnapshot.contains(list.get(i))){
                switch (i){
                    // NOMBRE
                    case 0:
                        tv_nombre_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // EMAIL
                    case 1:
                        tv_email_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // INICIO
                    case 2:
                        tv_inicio_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // FIN
                    case 3:
                        tv_fin_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // MATRICULA
                    case 4:
                        tv_matricula_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // TIPO
                    case 5:
                        tv_tipo_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // IMPORTE
                    case 6:
                        tv_importe_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                }
                i++;
            }
        }
    }
    // Metodo auxiliar que escribe
    private void escribeDatosReserva2(DocumentSnapshot documentSnapshot,List<String> list){
        int i = 0;
        int len = list.size();
        while (i < len){
            if(documentSnapshot.contains(list.get(i))){
                switch (i){
                    // NOMBRE
                    case 0:
                        tv_nombre_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // EMAIL
                    case 1:
                        tv_email_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // INICIO
                    case 2:
                        tv_inicio_reserva1.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // FIN
                    case 3:
                        tv_fin_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // MATRICULA
                    case 4:
                        tv_matricula_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // TIPO
                    case 5:
                        tv_tipo_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                    // IMPORTE
                    case 6:
                        tv_importe_reserva2.setText(documentSnapshot.get(list.get(i)).toString());
                        break;
                }
                i++;
            }
        }
    }
}
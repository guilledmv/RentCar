package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ACT_5_EDITAR_PERFIL_USUARIO extends AppCompatActivity {

    // VARIABLES VISIBLES EN PANTALLA
    private TextView tv_dni_ACT_5_EDITAR_PERFIL, tv_domicilio_ACT_5_EDITAR_PERFIL, tv_edad_ACT_5_EDITAR_PERFIL,
            tv_email_ACT_5_EDITAR_PERFIL, tv_nombre_ACT_5_EDITAR_PERFIL;
    private EditText edt_dni_ACT_5_EDITAR_PERFIL, edt_domicilio_ACT_5_EDITAR_PERFIL, edt_edad_EDITAR_PERFIL,
            edt_email_ACT_5_EDITAR_PERFIL, edt_nombre_ACT_5_EDITAR_PERFIL;
    private RadioGroup tipo;
    private RadioButton rb_particular_ACT_5_EDITAR_PERFIL, rb_empresa_ACT_5_EDITAR_PERFIL;
    private ProgressBar pb_ACT_5_EDITAR_PERFIL;
    private Button btn_guardar_cambios;

    // VARIABLES FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseFirestore cloudReference;

    // CONSTANTES PARA ACCEDER A LA BBDD DE FIREBASE
    private final String NOMBRE = "nombre";
    private final String DNI = "dni";
    private final String EDAD = "edad";
    private final String DOMICILIO = "domicilio";
    private final String EMAIL = "email";
    private final String TIPO = "tipo";

    // VARIABLES MANEJO DE DATOS
    private String email;
    private List<String> datosUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_5_editar_perfil_usuario);
        tv_dni_ACT_5_EDITAR_PERFIL = findViewById(R.id.tv_dni_ACT_5_EDITAR_PERFIL);
        tv_domicilio_ACT_5_EDITAR_PERFIL = findViewById(R.id.tv_domicilio_ACT_5_EDITAR_PERFIL);
        tv_edad_ACT_5_EDITAR_PERFIL = findViewById(R.id.tv_edad_ACT_5_EDITAR_PERFIL);
        tv_email_ACT_5_EDITAR_PERFIL = findViewById(R.id.tv_email_ACT_5_EDITAR_PERFIL);
        tv_nombre_ACT_5_EDITAR_PERFIL = findViewById(R.id.tv_nombre_ACT_5_EDITAR_PERFIL);
        edt_dni_ACT_5_EDITAR_PERFIL = findViewById(R.id.edt_dni_ACT_5_EDITAR_PERFIL);
        edt_domicilio_ACT_5_EDITAR_PERFIL = findViewById(R.id.edt_domicilio_ACT_5_EDITAR_PERFIL);
        edt_edad_EDITAR_PERFIL = findViewById(R.id.edt_edad_ACT_5_EDITAR_PERFIL);
        edt_email_ACT_5_EDITAR_PERFIL = findViewById(R.id.edt_email_ACT_5_EDITAR_PERFIL);
        edt_nombre_ACT_5_EDITAR_PERFIL = findViewById(R.id.edt_nombre_ACT_5_EDITAR_PERFIL);
        tipo = findViewById(R.id.rg_ACT_5_EDITAR_PERFIL);
        rb_particular_ACT_5_EDITAR_PERFIL = findViewById(R.id.rb_particular_ACT_5_EDITAR_PERFIL);
        rb_empresa_ACT_5_EDITAR_PERFIL = findViewById(R.id.rb_empresa_ACT_5_EDITAR_PERFIL);
        pb_ACT_5_EDITAR_PERFIL = findViewById(R.id.pb_ACT_5_EDITAR_PERFIL);
        btn_guardar_cambios = findViewById(R.id.btn_ACT_5_EDITAR_PERFIL);
        mAuth = FirebaseAuth.getInstance();
        cloudReference = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");
        datosUsuario = new ArrayList<String>(Arrays.asList(DNI, DOMICILIO, EDAD, EMAIL, NOMBRE, TIPO));
        // CARGAMOS DATOS ACTUALES DEL USUARIO
        leeDatosUsuario(cloudReference, email);
        // ACTUALIZAMOS LOS DATOS QUE EL USUARIO ESTA CARGANDO
        btn_guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_nombre_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty() && (!edt_dni_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty())
                        && (!edt_edad_EDITAR_PERFIL.getText().toString().isEmpty()) && (!edt_domicilio_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty()) &&
                        (!edt_email_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty()) && (rb_particular_ACT_5_EDITAR_PERFIL.isChecked() || rb_empresa_ACT_5_EDITAR_PERFIL.isChecked()) &&
                        emailValido(edt_email_ACT_5_EDITAR_PERFIL.getText().toString()) && edadValida(edt_edad_EDITAR_PERFIL.getText().toString())
                        && dniValido(edt_dni_ACT_5_EDITAR_PERFIL.getText().toString())){
                    // SI CUMPLE TODOS LOS REQUISITOS
                    pb_ACT_5_EDITAR_PERFIL.setVisibility(View.VISIBLE);
                    // Crep mapa donde inserto los datos
                    HashMap<String,Object>map = new HashMap<>();
                    // Metodo que inserta los datos en un map
                    insertaDatosEnMapa(map);
                    // Insertamos los datos en la base de datos firebase
                    cloudReference.collection("Usuarios").document(email).collection("Datos personales").document(email).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Datos añadidos con exito
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this,"Error al cargar datos en la base de datos",Toast.LENGTH_SHORT).show();
                        }
                    });
                    pb_ACT_5_EDITAR_PERFIL.setVisibility(View.INVISIBLE);
                    // Añadir datos del usuario al cloud
                    Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                }
                else if(edt_nombre_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty() || edt_dni_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty() || edt_edad_EDITAR_PERFIL.getText().toString().isEmpty()||
                        edt_domicilio_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty() || edt_email_ACT_5_EDITAR_PERFIL.getText().toString().isEmpty()
                        || rb_particular_ACT_5_EDITAR_PERFIL.isChecked() && rb_empresa_ACT_5_EDITAR_PERFIL.isChecked()){
                    Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this, "Rellenar todos los campos, por favor", Toast.LENGTH_SHORT).show();
                } else if(!emailValido(edt_email_ACT_5_EDITAR_PERFIL.getText().toString())){
                    Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this, "Email incorrecto,vuelve a introducir un email válido", Toast.LENGTH_SHORT).show();
                    edt_email_ACT_5_EDITAR_PERFIL.setText(null);
                } else if(!edadValida(edt_edad_EDITAR_PERFIL.getText().toString())){
                    Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this, "Edad invalida, debes tener entre 18 y 89 años", Toast.LENGTH_SHORT).show();
                    edt_edad_EDITAR_PERFIL.setText(null);
                } else if(!dniValido(edt_dni_ACT_5_EDITAR_PERFIL.getText().toString())){
                    Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this, "DNI incorrecto", Toast.LENGTH_SHORT).show();
                    edt_dni_ACT_5_EDITAR_PERFIL.setText(null);
                }
            }
        });
    }

    // METODO AUXILIAR QUE LEE LOS DATOS DEL USUARIO
    private void leeDatosUsuario(FirebaseFirestore cloudReference, String email) {
        cloudReference.collection("Usuarios").document(email).collection("Datos personales").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                escribeDatosUsuario(documentSnapshot, datosUsuario);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    // METODO AUXILIAR QUE ESCRIBE LOS DATOS DEL USUARIO EN LA PANTALLA
    private void escribeDatosUsuario(DocumentSnapshot documentSnapshot, List<String> datosUsuario) {
        int i = 0;
        int len = datosUsuario.size();
        while (i < len) {
            if (documentSnapshot.contains(datosUsuario.get(i))) {
                switch (i) {
                    // DNI
                    case 0:
                        edt_dni_ACT_5_EDITAR_PERFIL.setText(documentSnapshot.get(datosUsuario.get(i)).toString());
                        break;
                    // DOMICILIO
                    case 1:
                        edt_domicilio_ACT_5_EDITAR_PERFIL.setText(documentSnapshot.get(datosUsuario.get(i)).toString());
                        break;
                    // EDAD
                    case 2:
                        edt_edad_EDITAR_PERFIL.setText(documentSnapshot.get(datosUsuario.get(i)).toString());
                        break;
                    // EMAIL
                    case 3:
                        edt_email_ACT_5_EDITAR_PERFIL.setText(documentSnapshot.get(datosUsuario.get(i)).toString());
                        break;
                    // NOMBRE
                    case 4:
                        edt_nombre_ACT_5_EDITAR_PERFIL.setText(documentSnapshot.get(datosUsuario.get(i)).toString());
                        break;
                    // TIPO
                    case 5:
                        String tipo = documentSnapshot.getString(TIPO);
                        if(tipo.equals("Particular")){
                            rb_particular_ACT_5_EDITAR_PERFIL.setChecked(true);
                        } else if(tipo.equals("Empresa")){
                            rb_empresa_ACT_5_EDITAR_PERFIL.setChecked(true);
                        }
                        break;
                }
                i++;
            }
        }
    }
    // Metodo auxiliar que inserta los datos en un mapa
    private void insertaDatosEnMapa(HashMap<String,Object> map){
        map.put(NOMBRE,edt_nombre_ACT_5_EDITAR_PERFIL.getText().toString());
        map.put(DNI,edt_dni_ACT_5_EDITAR_PERFIL.getText().toString());
        map.put(EDAD,edt_edad_EDITAR_PERFIL.getText().toString());
        map.put(DOMICILIO,edt_domicilio_ACT_5_EDITAR_PERFIL.getText().toString());
        map.put(EMAIL,edt_email_ACT_5_EDITAR_PERFIL.getText().toString());
        if(rb_particular_ACT_5_EDITAR_PERFIL.isChecked()){
            map.put(TIPO,"Particular");
        } else {
            map.put(TIPO,"Empresa");
        }
    }
    // Metodo auxiliar para contraseña valida
    private boolean contrasenaValida(String contrasena){
        int long_contraseña = contrasena.length();
        if(long_contraseña < 8) {
            //Toast.makeText(ACT_2_CREATE_USER.this,"La contraseña debe contener más de 8 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    // Metodo auxiliar que valida el email
    private boolean emailValido(String email){
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
    // Metodo auxiliar que comprueba si la edad es valida
    private boolean edadValida(String edad) {
        int edad_aux = Integer.parseInt(edad);
        if(edad_aux >=18 && edad_aux < 90){
            return true;
        } else {
            //Toast.makeText(ACT_2_CREATE_USER.this,"La edad debe estar entre 18 y 89 años",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Metodo auxiliar que comprueba el formato de un dni
    private boolean dniValido(String dniAComprobar){

        // Array con las letras posibles del dni en su posición
        char[] letraDni = {
                'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D',  'X',  'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'
        };

        String num= "";
        int ind = 0;

        // boolean que nos indicara si es un dni correcto o no
        boolean valido;

        if(dniAComprobar.length() < 8){
            return false;
        }

        // existen dnis con 7 digitos numericos, si fuese el caso
        // le añado un cero al principio
        if(dniAComprobar.length() == 8) {
            dniAComprobar = "0" + dniAComprobar;
        }

        // compruebo que el 9º digito es una letra
        if (!Character.isLetter(dniAComprobar.charAt(8))) {
            Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
            return false;
        }

        // compruebo su longitud que sea 9
        if (dniAComprobar.length() != 9){
            Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
            return false;
        }

        // Compruebo que lo 8 primeros digitos sean numeros
        for (int i=0; i<8; i++) {

            if(!Character.isDigit(dniAComprobar.charAt(i))){
                Toast.makeText(ACT_5_EDITAR_PERFIL_USUARIO.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
                return false;
            }
            // si es numero, lo recojo en un String
            num += dniAComprobar.charAt(i);
        }

        // paso a int el string donde tengo el numero del dni
        ind = Integer.parseInt(num);

        // calculo la posición de la letra en el array que corresponde a este dni
        ind %= 23;

        // si el flujo de la funcion llega aquí, es que el dni es correcto
        return true;
    }
}
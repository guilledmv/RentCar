package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rentcar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ACT_2_CREATE_USER extends AppCompatActivity {
    // Variables de la pantalla

    // EditText
    private EditText edt_name_ACT_2_CREATE_USER,edt_dni_ACT_2_CREATE_USER,edt_edad_ACT_2_CREATE_USER,
            edt_domicilio_ACT_2_CREATE_USER,edt_email_ACT_2_CREATE_USER,edt_password_ACT_2_CREATE_USER;
    // RadioGroup
    private RadioGroup rg_tipo_cliente_ACT_2_CREATE_USER;
    // RadioButton
    private RadioButton rb_particular_ACT_2_CREATE_USER,rb_empresa_ACT_2_CREATE_USER;
    // Button
    private Button btn_create_user_ACT_2_CREATE_USER;
    // Barra de progreso
    private ProgressBar pb_ACT_2_CREATE_USER;

    // Variables Firebase para la gestion de la base de datos
    private FirebaseAuth mAuth;
    private FirebaseUser usuarioActual;
    // Variable para almacenar en la base de datos cloudFirebase
    private FirebaseFirestore cloudReference = FirebaseFirestore.getInstance();

    // Constantes con las que referenciamos a los objetos de la base de datos firebase
    private final String NOMBRE = "nombre";
    private final String DNI = "dni";
    private final String EDAD = "edad";
    private final String DOMICILIO = "domicilio";
    private final String EMAIL = "email";
    private final String TIPO = "tipo";


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_2_create_user);
        context = this;
        mAuth = FirebaseAuth.getInstance();
        // Asocio variables con sus id del layout
        edt_name_ACT_2_CREATE_USER = findViewById(R.id.edt_name_ACT_2_CREATE_USER);
        edt_dni_ACT_2_CREATE_USER = findViewById(R.id.edt_dni_ACT_2_CREATE_USER);
        edt_edad_ACT_2_CREATE_USER = findViewById(R.id.edt_edad_ACT_2_CREATE_USER);
        edt_domicilio_ACT_2_CREATE_USER = findViewById(R.id.edt_domicilio_ACT_2_CREATE_USER);
        edt_email_ACT_2_CREATE_USER = findViewById(R.id.edt_email_ACT_2_CREATE_USER);
        edt_password_ACT_2_CREATE_USER = findViewById(R.id.edt_password_ACT_2_CREATE_USER);
        rg_tipo_cliente_ACT_2_CREATE_USER = findViewById(R.id.rg_tipo_cliente_ACT_2_CREATE_USER);
        rb_particular_ACT_2_CREATE_USER = findViewById(R.id.rb_particular_ACT_2_CREATE_USER);
        rb_empresa_ACT_2_CREATE_USER = findViewById(R.id.rb_empresa_ACT_2_CREATE_USER);
        btn_create_user_ACT_2_CREATE_USER = findViewById(R.id.btn_crear_ACT_2);
        pb_ACT_2_CREATE_USER = findViewById(R.id.pb_ACT_2_CREATE_USER);
        btn_create_user_ACT_2_CREATE_USER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Caso bueno, los datos estan rellenos
                if((!edt_name_ACT_2_CREATE_USER.getText().toString().isEmpty()) && (!edt_dni_ACT_2_CREATE_USER.getText().toString().isEmpty())
                        && (!edt_edad_ACT_2_CREATE_USER.getText().toString().isEmpty()) && (!edt_domicilio_ACT_2_CREATE_USER.getText().toString().isEmpty()) &&
                        (!edt_email_ACT_2_CREATE_USER.getText().toString().isEmpty()) && (!edt_password_ACT_2_CREATE_USER.getText().toString().isEmpty())
                        && (rb_particular_ACT_2_CREATE_USER.isChecked() || rb_empresa_ACT_2_CREATE_USER.isChecked()) &&
                        contrasenaValida(edt_password_ACT_2_CREATE_USER.getText().toString()) &&
                        emailValido(edt_email_ACT_2_CREATE_USER.getText().toString()) && edadValida(edt_edad_ACT_2_CREATE_USER.getText().toString())
                        && dniValido(edt_dni_ACT_2_CREATE_USER.getText().toString())) {
                    // Inicia barra de progreso
                    pb_ACT_2_CREATE_USER.setVisibility(View.VISIBLE);
                    String email = edt_email_ACT_2_CREATE_USER.getText().toString();
                    String password = edt_password_ACT_2_CREATE_USER.getText().toString();
                    // Creo nuevo usuario
                    mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                        usuarioActual = authResult.getUser();
                        // Envia email de verificacion de registro
                        usuarioActual.sendEmailVerification();
                        // Crep mapa donde inserto los datos
                        HashMap<String,Object>map = new HashMap<>();
                        // Metodo que inserta los datos en un map
                        insertaDatosEnMapa(map);
                        // Insertamos los datos en la base de datos firebase
                        cloudReference.collection("Usuarios").document(edt_email_ACT_2_CREATE_USER.getText().toString()).collection("Datos personales").document(edt_email_ACT_2_CREATE_USER.getText().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Datos añadidos con exito
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ACT_2_CREATE_USER.this,"Error al cargar datos en la base de datos",Toast.LENGTH_SHORT).show();
                            }
                        });
                        pb_ACT_2_CREATE_USER.setVisibility(View.INVISIBLE);
                        // Añadir datos del usuario al cloud
                        Toast.makeText(ACT_2_CREATE_USER.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ACT_2_CREATE_USER.this,ACT_2_LOGIN.class);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        pb_ACT_2_CREATE_USER.setVisibility(View.GONE);
                        Toast.makeText(ACT_2_CREATE_USER.this, "Fallo al registrar usuario", Toast.LENGTH_SHORT).show();
                    });
                } else if(edt_name_ACT_2_CREATE_USER.getText().toString().isEmpty() || edt_dni_ACT_2_CREATE_USER.getText().toString().isEmpty() || edt_edad_ACT_2_CREATE_USER.getText().toString().isEmpty()||
                        edt_domicilio_ACT_2_CREATE_USER.getText().toString().isEmpty() || edt_email_ACT_2_CREATE_USER.getText().toString().isEmpty() || edt_password_ACT_2_CREATE_USER.getText().toString().isEmpty()
                        || rb_particular_ACT_2_CREATE_USER.isChecked() && rb_empresa_ACT_2_CREATE_USER.isChecked()){
                    Toast.makeText(ACT_2_CREATE_USER.this, "Rellenar todos los campos, por favor", Toast.LENGTH_SHORT).show();
                } else if(!contrasenaValida(edt_password_ACT_2_CREATE_USER.getText().toString())) {
                    Toast.makeText(ACT_2_CREATE_USER.this, "Contraseña invalida, debe contener mas de 8 caracteres", Toast.LENGTH_SHORT).show();
                    edt_password_ACT_2_CREATE_USER.setText(null);
                } else if(!emailValido(edt_email_ACT_2_CREATE_USER.getText().toString())){
                    Toast.makeText(ACT_2_CREATE_USER.this, "Email incorrecto,vuelve a introducir un email válido", Toast.LENGTH_SHORT).show();
                    edt_email_ACT_2_CREATE_USER.setText(null);
                } else if(!edadValida(edt_edad_ACT_2_CREATE_USER.getText().toString())){
                    Toast.makeText(ACT_2_CREATE_USER.this, "Edad invalida, debes tener entre 18 y 89 años", Toast.LENGTH_SHORT).show();
                    edt_edad_ACT_2_CREATE_USER.setText(null);
                } else if(!dniValido(edt_dni_ACT_2_CREATE_USER.getText().toString())){
                    Toast.makeText(ACT_2_CREATE_USER.this, "DNI incorrecto", Toast.LENGTH_SHORT).show();
                    edt_dni_ACT_2_CREATE_USER.setText(null);
                }
            }
        });
    }
    // Metodo auxiliar que inserta los datos en un mapa
    private void insertaDatosEnMapa(HashMap<String,Object> map){
        map.put(NOMBRE,edt_name_ACT_2_CREATE_USER.getText().toString());
        map.put(DNI,edt_dni_ACT_2_CREATE_USER.getText().toString());
        map.put(EDAD,edt_edad_ACT_2_CREATE_USER.getText().toString());
        map.put(DOMICILIO,edt_domicilio_ACT_2_CREATE_USER.getText().toString());
        map.put(EMAIL,edt_email_ACT_2_CREATE_USER.getText().toString());
        if(rb_particular_ACT_2_CREATE_USER.isChecked()){
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
            Toast.makeText(ACT_2_CREATE_USER.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
            return false;
        }

        // compruebo su longitud que sea 9
        if (dniAComprobar.length() != 9){
            Toast.makeText(ACT_2_CREATE_USER.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
            return false;
        }

        // Compruebo que lo 8 primeros digitos sean numeros
        for (int i=0; i<8; i++) {

            if(!Character.isDigit(dniAComprobar.charAt(i))){
                Toast.makeText(ACT_2_CREATE_USER.this,"Fallo en formato DNI",Toast.LENGTH_SHORT).show();
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
package com.example.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ACT_4_FORMULARIO_RESERVA extends AppCompatActivity {

    // Variables pantalla formulario de reserva
    private TextView tv_inicio_alquiler,tv_fin_alquiler,tv_seleccion,tv_franquicia_origen,
                    tv_franquicia_destino,tv_modelo,tv_tarifa,tv_disponibilidad,tv_matricula,
                    tv_dni,tv_nombre,tv_tipo;
    private EditText edt_fecha_inicio_alquiler,edt_fecha_fin_alquiler,edt_hora_inicio_alquiler,
                        edt_hora_fin_alquiler;
    private Button btn_crear_reserva,btn_crear_presupuesto;

    private Spinner spinner_franquicia_origen,spinner_franquicia_destino,spinner_marca_vehiculo;

    private ProgressBar pb_calcular_presupuesto,pb_crear_reserva;

    private Calendar fechaActual = Calendar.getInstance();

    private int numero_dias;
    private int numero_meses;
    private int numero_años;

    private CheckBox cb_automatico_porsche,cb_calefactables_porsche,cb_techo_panoramico_porsche,
            cb_version_deportiva_porsche,cb_navegador_seat,cb_control_crucero_seat,cb_sensores_seat,
            cb_navegador_dacia,cb_bluetooth_dacia,cb_aire_dacia;

    // Constantes para acceder a la base de datos firebase
    private final String VEHICULOS = "Vehículos";
    private final String DACIA = "Dacia";
    private final String PORSCHE = "Porsche";
    private final String SEAT = "Seat";
    private final String MODELO = "modelo";
    private final String AUTOMATICO = "automatico";
    private final String CALEFACTABLE = "calefactables";
    private final String TECHO = "techo";
    private final String VERSION = "deportivo";
    private final String NAVEGADOR = "navegador";
    private final String CONTROL_CRUCERO = "control crucero";
    private final String SENSORES = "sensores";
    private final String BLUETOOTH = "bluetooth";
    private final String AIRE_ACONDICIONADO = "aire acondicionado";

    // Variables firebase

    private FirebaseAuth mAuth;
    private FirebaseUser usuarioActual;
    // Variable para almacenar en la base de datos cloudFirebase
    private FirebaseFirestore cloudReference;

    // Variable email
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_4_formulario_reserva);
        tv_inicio_alquiler = findViewById(R.id.tv_inicio_ACT_4_FORMULARIO);
        tv_fin_alquiler = findViewById(R.id.tv_fin_ACT_4_FORMULARIO);
        tv_seleccion = findViewById(R.id.tv_seleccion_ACT_4_FORMULARIO);
        edt_fecha_inicio_alquiler = findViewById(R.id.edt_fecha_inicio_ACT_4_FORMULARIO);
        edt_fecha_fin_alquiler = findViewById(R.id.edt_fecha_fin_ACT_4_FORMULARIO);
        edt_hora_inicio_alquiler = findViewById(R.id.edt_hora_inicio_ACT_4_FORMULARIO);
        edt_hora_fin_alquiler = findViewById(R.id.edt_hora_fin_ACT_4_FORMULARIO);
        btn_crear_reserva = findViewById(R.id.btn_crear_reserva_ACT_4_CREAR_RESERVA);
        btn_crear_presupuesto = findViewById(R.id.btn_calcular_presupuesto_ACT_4_FORMULARIO);
        tv_franquicia_origen = findViewById(R.id.tv_franquicia_origen_ACT_4_FORMULARIO_RESERVA);
        tv_franquicia_destino = findViewById(R.id.tv_franquicia_destino_ACT_4_FORMULARIO_RESERVA);
        spinner_marca_vehiculo = findViewById(R.id.spinner_marca_ACT_4_FORMULARIO_RESERVA);
        spinner_franquicia_origen = findViewById(R.id.spinner_franquicia_origen_ACT_4_FORMULARIO_RESERVA);
        spinner_franquicia_destino = findViewById(R.id.spinner_franquicia_destino_ACT_4_FORMULARIO_RESERVA);
        tv_modelo = findViewById(R.id.tv_modelo_ACT4_formulario);
        tv_disponibilidad = findViewById(R.id.tv_disponibilidad_ACT4_formulario);
        tv_tarifa = findViewById(R.id.tv_tarifa_ACT4_formulario);
        tv_matricula = findViewById(R.id.tv_matricula_ACT_4_FORMULARIO);
        tv_dni = findViewById(R.id.tv_dni_ACT_4_FORMULARIO);
        tv_nombre = findViewById(R.id.tv_nombre_ACT_4_FORMULARIO);
        tv_tipo = findViewById(R.id.tv_tipo_ACT_4_FORMULARIO);
        pb_calcular_presupuesto = findViewById(R.id.pb_calcular_presupuesto_ACT_4_FORMULARIO);
        pb_crear_reserva = findViewById(R.id.pb_crear_reserva_ACT_4_FORMULARIO_RESERVA);
        cb_automatico_porsche = findViewById(R.id.cb_automatico_porsche);
        cb_calefactables_porsche = findViewById(R.id.cb_calefactables_porsche);
        cb_techo_panoramico_porsche = findViewById(R.id.cb_techo_panoramico_porsche);
        cb_version_deportiva_porsche = findViewById(R.id.cb_version_deportiva_porsche);
        cb_navegador_seat = findViewById(R.id.cb_navegador_seat);
        cb_control_crucero_seat = findViewById(R.id.cb_control_crucero_seat);
        cb_sensores_seat = findViewById(R.id.cb_sensores_seat);
        cb_navegador_dacia = findViewById(R.id.cb_navegador_dacia);
        cb_bluetooth_dacia = findViewById(R.id.cb_bluetooth_dacia);
        cb_aire_dacia = findViewById(R.id.cb_aire_dacia);
        mAuth = FirebaseAuth.getInstance();
        usuarioActual = mAuth.getCurrentUser();
        cloudReference = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");

        spinner_marca_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object marca1 = parent.getItemAtPosition(position);
                String marca = marca1.toString();
                if (marca.equals(PORSCHE)) {
                    escondeExtrasSeat();
                    escondeExtrasDacia();
                    enseñaExtrasPorsche();
                } else if(marca.equals(SEAT)) {
                    escondeExtrasPorsche();
                    escondeExtrasDacia();
                    enseñaExtrasSeat();
                } else if(marca.equals(DACIA)){
                    escondeExtrasPorsche();
                    escondeExtrasSeat();
                    enseñaExtrasDacia();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Cuando pulsamos el boton de crear presupuesto
        btn_crear_presupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SI EXISTE ALGUN CAMPO VACIO--> ERROR NO SE PUEDE CREAR PRESUPUESTO
                if (edt_fecha_inicio_alquiler.getText().toString().isEmpty() || edt_fecha_fin_alquiler.getText().toString().isEmpty() ||
                        edt_hora_inicio_alquiler.getText().toString().isEmpty() || edt_hora_fin_alquiler.getText().toString().isEmpty() || spinner_franquicia_origen.getSelectedItem().toString().isEmpty()
                        || spinner_franquicia_destino.getSelectedItem().toString().isEmpty() || spinner_marca_vehiculo.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, se deben rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }
                // COMPROBAMOS FECHAS Y HORAS
                pb_calcular_presupuesto.setVisibility(View.VISIBLE);
                if (!compruebaFecha(edt_fecha_inicio_alquiler.getText().toString())) {
                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaFecha(edt_fecha_fin_alquiler.getText().toString())) {
                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha fin alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_inicio_alquiler.getText().toString())) {
                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Hora inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_fin_alquiler.getText().toString())) {
                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Hora fin alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (compruebaFecha(edt_fecha_inicio_alquiler.getText().toString()) && compruebaFecha(edt_fecha_fin_alquiler.getText().toString())
                        && compruebaHora(edt_hora_inicio_alquiler.getText().toString()) && compruebaHora(edt_hora_fin_alquiler.getText().toString())) {
                    String mesInicio = edt_fecha_inicio_alquiler.getText().toString().substring(3, 5);
                    String añoInicio = edt_fecha_inicio_alquiler.getText().toString().substring(6, 10);
                    String diaInicio = edt_fecha_inicio_alquiler.getText().toString().substring(0, 2);
                    String horaInicio = edt_hora_inicio_alquiler.getText().toString().substring(0, 2);
                    String minutosInicio = edt_hora_inicio_alquiler.getText().toString().substring(3, 5);
                    String horaFin = edt_hora_fin_alquiler.getText().toString().substring(0, 2);
                    String minutosFin = edt_hora_fin_alquiler.getText().toString().substring(3, 5);
                    String mesFin = edt_fecha_fin_alquiler.getText().toString().substring(3, 5);
                    String añoFin = edt_fecha_fin_alquiler.getText().toString().substring(6, 10);
                    String diaFin = edt_fecha_fin_alquiler.getText().toString().substring(0, 2);
                    numero_dias = Integer.parseInt(diaFin) - Integer.parseInt(diaInicio);
                    numero_meses = Integer.parseInt(mesFin) - Integer.parseInt(mesInicio);
                    numero_años = Integer.parseInt(añoFin) - Integer.parseInt(añoInicio);
                    // Compruebo que la fecha de fin de reserva > fecha inicio reserva
                    // 1ºcaso--> Año inicio > año fin--> peta
                    if (Integer.parseInt(añoInicio) > Integer.parseInt(añoFin)) {
                        pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, años incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    // 2ºcaso--> Estan en el mismo año--> Mes inicio >= mes fin
                    if (Integer.parseInt(añoInicio) == Integer.parseInt(añoFin)) {
                        // CASO 2.1: Mes fin < mes inicio-> peta
                        if (Integer.parseInt(mesInicio) > Integer.parseInt(mesFin)) {
                            pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, meses incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        // CASO 2.2: Mes fin == Mes inicio--> comprobamos el dia
                        if (Integer.parseInt(mesInicio) == Integer.parseInt(mesFin)) {
                            if (Integer.parseInt(diaInicio) > Integer.parseInt(diaFin)) {
                                pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, el dia de inicio no puede ser mayor que el de fin de alquiler", Toast.LENGTH_SHORT).show();
                            }
                            // CASO PENDIENTE : los dias son iguales, comparar horas.
                            if (Integer.parseInt(diaInicio) == Integer.parseInt(diaFin)) {
                                // Si la horaInicio < horaFin
                                if (Integer.parseInt(horaInicio) > Integer.parseInt(horaFin)) {
                                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, la hora necesaria no es válida", Toast.LENGTH_SHORT).show();
                                }
                                // Si las horas son iguales--> comprueba los minutos
                                if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
                                    if (Integer.parseInt(minutosInicio) > Integer.parseInt(minutosFin)) {
                                        pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, los minutos son inválidos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }

                    // Obtenemos marca elegida
                    String marca = spinner_marca_vehiculo.getSelectedItem().toString();

                    // Obtenemos los datos del usuario
                    obtenDatosUsuario(cloudReference, tv_dni, tv_nombre, tv_tipo);

                    // Obtenemos los datos de las marcas y calculamos su tarifa
                    if (marca.equals(PORSCHE)) {
                        obtenDatosMarca(cloudReference, VEHICULOS, PORSCHE, tv_modelo, tv_disponibilidad, tv_tarifa, tv_matricula, numero_dias, numero_meses, numero_años);
                    }
                    if (marca.equals(SEAT)) {
                        obtenDatosMarca(cloudReference, VEHICULOS, SEAT, tv_modelo, tv_disponibilidad, tv_tarifa, tv_matricula, numero_dias, numero_meses, numero_años);
                    }
                    if (marca.equals(DACIA)) {
                        obtenDatosMarca(cloudReference, VEHICULOS, DACIA, tv_modelo, tv_disponibilidad, tv_tarifa, tv_matricula, numero_dias, numero_meses, numero_años);
                    }
                    pb_calcular_presupuesto.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Cuando pulsamos el boton de crear reserva
        btn_crear_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SI EXISTE ALGUN CAMPO VACIO--> ERROR NO SE PUEDE CREAR RESERVA
                if (edt_fecha_inicio_alquiler.getText().toString().isEmpty() || edt_fecha_fin_alquiler.getText().toString().isEmpty() ||
                        edt_hora_inicio_alquiler.getText().toString().isEmpty() || edt_hora_fin_alquiler.getText().toString().isEmpty() || spinner_franquicia_origen.getSelectedItem().toString().isEmpty()
                        || spinner_franquicia_destino.getSelectedItem().toString().isEmpty() || spinner_marca_vehiculo.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, se deben rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }
                // COMPROBAMOS FECHAS Y HORAS
                pb_crear_reserva.setVisibility(View.VISIBLE);
                if (!compruebaFecha(edt_fecha_inicio_alquiler.getText().toString())) {
                    pb_crear_reserva.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaFecha(edt_fecha_fin_alquiler.getText().toString())) {
                    pb_crear_reserva.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha fin alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_inicio_alquiler.getText().toString())) {
                    pb_crear_reserva.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Hora inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_fin_alquiler.getText().toString())) {
                    pb_crear_reserva.setVisibility(View.INVISIBLE);
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Hora fin alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (compruebaFecha(edt_fecha_inicio_alquiler.getText().toString()) && compruebaFecha(edt_fecha_fin_alquiler.getText().toString())
                        && compruebaHora(edt_hora_inicio_alquiler.getText().toString()) && compruebaHora(edt_hora_fin_alquiler.getText().toString())) {
                    String mesInicio = edt_fecha_inicio_alquiler.getText().toString().substring(3, 5);
                    String añoInicio = edt_fecha_inicio_alquiler.getText().toString().substring(6, 10);
                    String diaInicio = edt_fecha_inicio_alquiler.getText().toString().substring(0, 2);
                    String horaInicio = edt_hora_inicio_alquiler.getText().toString().substring(0, 2);
                    String minutosInicio = edt_hora_inicio_alquiler.getText().toString().substring(3, 5);
                    String horaFin = edt_hora_fin_alquiler.getText().toString().substring(0, 2);
                    String minutosFin = edt_hora_fin_alquiler.getText().toString().substring(3, 5);
                    String mesFin = edt_fecha_fin_alquiler.getText().toString().substring(3, 5);
                    String añoFin = edt_fecha_fin_alquiler.getText().toString().substring(6, 10);
                    String diaFin = edt_fecha_fin_alquiler.getText().toString().substring(0, 2);
                    // Compruebo que la fecha de fin de reserva > fecha inicio reserva
                    // 1ºcaso--> Año inicio > año fin--> peta
                    if (Integer.parseInt(añoInicio) > Integer.parseInt(añoFin)) {
                        pb_crear_reserva.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, años incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    // 2ºcaso--> Estan en el mismo año--> Mes inicio >= mes fin
                    if (Integer.parseInt(añoInicio) == Integer.parseInt(añoFin)) {
                        // CASO 2.1: Mes fin < mes inicio-> peta
                        if (Integer.parseInt(mesInicio) > Integer.parseInt(mesFin)) {
                            pb_crear_reserva.setVisibility(View.INVISIBLE);
                            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, meses incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        // CASO 2.2: Mes fin == Mes inicio--> comprobamos el dia
                        if (Integer.parseInt(mesInicio) == Integer.parseInt(mesFin)) {
                            if (Integer.parseInt(diaInicio) > Integer.parseInt(diaFin)) {
                                pb_crear_reserva.setVisibility(View.INVISIBLE);
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, el dia de inicio no puede ser mayor que el de fin de alquiler", Toast.LENGTH_SHORT).show();
                            }
                            // CASO PENDIENTE : los dias son iguales, comparar horas.
                            if (Integer.parseInt(diaInicio) == Integer.parseInt(diaFin)) {
                                // Si la horaInicio < horaFin
                                if (Integer.parseInt(horaInicio) > Integer.parseInt(horaFin)) {
                                    pb_crear_reserva.setVisibility(View.INVISIBLE);
                                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, la hora necesaria no es válida", Toast.LENGTH_SHORT).show();
                                }
                                // Si las horas son iguales--> comprueba los minutos
                                if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
                                    if (Integer.parseInt(minutosInicio) > Integer.parseInt(minutosFin)) {
                                        pb_crear_reserva.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, los minutos son inválidos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                    // Creamos reserva en firebase
                    Random random = new Random();
                    String numero_reserva = Integer.toString(random.nextInt(100000));
                    String inicio_reserva = edt_fecha_inicio_alquiler.getText().toString() + " " + edt_hora_inicio_alquiler.getText().toString();
                    String fin_reserva = edt_fecha_fin_alquiler.getText().toString() + " " + edt_hora_fin_alquiler.getText().toString();
                    String marca = spinner_marca_vehiculo.getSelectedItem().toString();
                    Map<String, Object> datosUsuario = new HashMap<String, Object>();
                    Map<String, Object> datosReserva = new HashMap<String, Object>();
                    // Obtenemos marca elegida
                    String marca2 = spinner_marca_vehiculo.getSelectedItem().toString();
                    // Obtenemos franquicias
                    String franquiciaOrigen = spinner_franquicia_origen.getSelectedItem().toString();
                    String franquiciaDestino = spinner_franquicia_destino.getSelectedItem().toString();
                    datosReserva.put("numero reserva", numero_reserva);
                    datosUsuario.put("numero reserva" + Integer.toString(1), numero_reserva);
                    datosReserva.put("inicio reserva", inicio_reserva);
                    datosReserva.put("fin reserva", fin_reserva);
                    datosReserva.put("marca",marca2);
                    datosReserva.put("franquicia origen",franquiciaOrigen);
                    datosReserva.put("franquicia destino",franquiciaDestino);
                    datosReserva.put("email", email);
                    datosReserva.put("dni", tv_dni.getText().toString());
                    datosReserva.put("nombre", tv_nombre.getText().toString());
                    datosReserva.put("tipo", tv_tipo.getText().toString());
                    datosReserva.put("matricula", tv_matricula.getText().toString());
                    // AÑADIMOS INFO A LA BBDD SI ESTAN AÑADIENDO EXTRAS
                    // CASO EXTRAS PORSCHE
                    if (marca.equals(PORSCHE)) {
                        if (cb_automatico_porsche.isChecked()) {
                            datosReserva.put(AUTOMATICO, "SI");
                        } else {
                            datosReserva.put(AUTOMATICO, "NO");
                        }
                        if (cb_calefactables_porsche.isChecked()) {
                            datosReserva.put(CALEFACTABLE, "SI");
                        } else {
                            datosReserva.put(CALEFACTABLE, "NO");
                        }
                        if (cb_techo_panoramico_porsche.isChecked()) {
                            datosReserva.put(TECHO, "SI");
                        } else {
                            datosReserva.put(TECHO, "NO");
                        }
                        if (cb_version_deportiva_porsche.isChecked()) {
                            datosReserva.put(VERSION, "SI");
                        } else {
                            datosReserva.put(VERSION, "NO");
                        }
                    }
                    // CASO EXTRAS SEAT
                    if(marca.equals(SEAT)){
                        if(cb_navegador_seat.isChecked()){
                            datosReserva.put(NAVEGADOR,"SI");
                        } else {
                            datosReserva.put(NAVEGADOR,"NO");
                        }
                        if(cb_control_crucero_seat.isChecked()){
                            datosReserva.put(CONTROL_CRUCERO,"SI");
                        } else {
                            datosReserva.put(CONTROL_CRUCERO,"NO");
                        }
                        if(cb_sensores_seat.isChecked()){
                            datosReserva.put(SENSORES,"SI");
                        } else {
                            datosReserva.put(SENSORES,"NO");
                        }
                    }
                    // CASO EXTRAS DACIA
                    if(marca.equals(DACIA)){
                        if(cb_navegador_dacia.isChecked()){
                            datosReserva.put(NAVEGADOR,"SI");
                        } else {
                            datosReserva.put(NAVEGADOR,"NO");
                        }
                        if(cb_bluetooth_dacia.isChecked()){
                            datosReserva.put(BLUETOOTH,"SI");
                        } else {
                            datosReserva.put(BLUETOOTH,"NO");
                        }
                        if(cb_aire_dacia.isChecked()){
                            datosReserva.put(AIRE_ACONDICIONADO,"SI");
                        } else {
                            datosReserva.put(AIRE_ACONDICIONADO,"NO");
                        }
                    }

                    if (tv_tarifa.getText().toString().equals("no disponible")) {
                        pb_crear_reserva.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fallo al crear reserva, debido a vehículo no disponible", Toast.LENGTH_SHORT).show();
                    } else if (tv_tarifa.getText().toString().equals("Error en tarifa")) {
                        pb_crear_reserva.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fallo al crear reserva, debido a la tarifa", Toast.LENGTH_SHORT).show();
                    } else if (tv_tarifa.getText().toString().equals("Tarifa")) {
                        pb_crear_reserva.setVisibility(View.INVISIBLE);
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fallo al crear reserva, debes calcular un presupuesto antes de crear una reserva", Toast.LENGTH_SHORT).show();
                    } else {
                        datosReserva.put("importe", tv_tarifa.getText().toString());
                        // Insertamos en la base de datos
                        cloudReference.collection("Usuarios").document(email).collection("Reservas").document(numero_reserva).set(datosReserva).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pb_crear_reserva.setVisibility(View.INVISIBLE);
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pb_crear_reserva.setVisibility(View.INVISIBLE);
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fallo al crear reserva", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Actualizamos estado del vehiculo
                        Map<String, Object> mapUpdate = new HashMap<String, Object>();
                        mapUpdate.put("estado", "no disponible");
                        cloudReference.collection(VEHICULOS).document(marca).update(mapUpdate);
                        // Cargamos el numero de reserva en el usuario
                        cloudReference.collection("Usuarios").document(email).collection("Datos personales").document(email).update(datosUsuario);
                        // Pasamos a la siguiente actividad
                        Intent intent = new Intent(ACT_4_FORMULARIO_RESERVA.this, ACT_3_PANTALLA_PRINCIPAL.class);
                        intent.putExtra("numeroReserva", numero_reserva);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    // Metodo privado que obtiene el modelo del vehiculo
    private void obtenDatosMarca(FirebaseFirestore cloudReference, String Vehiculo, String marca, TextView tv_modelo, TextView tv_disponibilidad,
                                 TextView tv_tarifa,TextView tv_matricula,int numero_dias,int numero_meses, int numero_años){
        cloudReference.collection(Vehiculo).document(marca).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String modelo = documentSnapshot.getString("modelo");
                String estado = documentSnapshot.getString("estado");
                String tarifa = documentSnapshot.getString("tarifa");
                String matricula = documentSnapshot.getString("matricula");
                tv_modelo.setText(modelo);
                tv_disponibilidad.setText(estado);
                if (estado.equals("no disponible")) {
                    tv_tarifa.setText("no disponible");
                    tv_matricula.setText(matricula);
                }
                if (estado.equals("disponible")) {
                    int extra = calculaExtra(marca);
                    // CALCULAMOS LA TARIFA TOTAL
                    if (numero_años == 0 && numero_meses == 0) {
                        // MINIMO CONTAMOS UN DIA PARA EL ALQUILER
                        if (numero_dias == 0) {
                            if(tv_tipo.getText().toString().equals("Particular")) {
                                int tarifaTotal1 = Integer.parseInt(tarifa) + extra;
                                String tarifaTotal2 = Integer.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            } else if(tv_tipo.getText().toString().equals("Empresa")){
                                // CALCULAMOS TARIFA BASE
                                int tarifaBase = Integer.parseInt(tarifa) + extra;
                                double tarifaTotal1 = tarifaBase - (0.21 * tarifaBase);
                                String tarifaTotal2 = Double.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            }
                            // TENEMOS VARIOS DIAS
                        } else {
                            if(tv_tipo.getText().toString().equals("Particular")) {
                                int tarifaTotal1 = Integer.parseInt(tarifa) * numero_dias + extra;
                                if (tarifaTotal1 > 0) {
                                    String tarifaTotal2 = Integer.toString(tarifaTotal1);
                                    tv_tarifa.setText(tarifaTotal2 + "€");
                                } else {
                                    tv_tarifa.setText("Error en tarifa");
                                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                                }
                            } else if(tv_tipo.getText().toString().equals("Empresa")){
                                int tarifaBase = Integer.parseInt(tarifa) * numero_dias + extra ;
                                double tarifaTotal1 = tarifaBase - (0.21 * tarifaBase);
                                if (tarifaTotal1 > 0) {
                                    String tarifaTotal2 = Double.toString(tarifaTotal1);
                                    tv_tarifa.setText(tarifaTotal2 + "€");
                                } else {
                                    tv_tarifa.setText("Error en tarifa");
                                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    if (numero_años == 0 && numero_meses != 0) {
                        if(tv_tipo.getText().toString().equals("Particular")) {
                            int tarifaTotal1 = Integer.parseInt(tarifa) * numero_dias * numero_meses * 30 + extra;
                            if (tarifaTotal1 > 0) {
                                String tarifaTotal2 = Integer.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            } else {
                                tv_tarifa.setText("Error en tarifa");
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                            }
                        } else if(tv_tipo.getText().toString().equals("Empresa")){
                            int tarifaBase = Integer.parseInt(tarifa) * numero_dias * numero_meses * 30 + extra;
                            double tarifaTotal1 = tarifaBase - (0.21 * tarifaBase);
                            if (tarifaTotal1 > 0) {
                                String tarifaTotal2 = Double.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            } else {
                                tv_tarifa.setText("Error en tarifa");
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (numero_años != 0) {
                        if(tv_tipo.getText().toString().equals("Particular")) {
                            int tarifaTotal1 = Integer.parseInt(tarifa) * numero_dias * numero_años * 365 + extra;
                            if (tarifaTotal1 > 0) {
                                String tarifaTotal2 = Integer.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            } else {
                                tv_tarifa.setText("Error en tarifa");
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                            }
                        } else if(tv_tipo.getText().toString().equals("Empresa")){
                            int tarifaBase = Integer.parseInt(tarifa) * numero_dias * numero_años * 365 + extra;
                            double tarifaTotal1 = tarifaBase - (0.21 * tarifaBase);
                            if (tarifaTotal1 > 0) {
                                String tarifaTotal2 = Double.toString(tarifaTotal1);
                                tv_tarifa.setText(tarifaTotal2 + "€");
                            } else {
                                tv_tarifa.setText("Error en tarifa");
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, fallo al calcular presupuesto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    tv_matricula.setText(matricula);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Fallo lectura modelo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo privado para obtener datos del usuario
    private void obtenDatosUsuario(FirebaseFirestore cloudReference,TextView tv_dni,TextView tv_nombre, TextView tv_tipo){
        cloudReference.collection("Usuarios").document(email).collection("Datos personales").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dni = documentSnapshot.getString("dni");
                String nombre = documentSnapshot.getString("nombre");
                String tipo = documentSnapshot.getString("tipo");
                tv_dni.setText(dni);
                tv_nombre.setText(nombre);
                tv_tipo.setText(tipo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Fallo lectura datos usuario",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo privado para comprobar la hora
    private boolean compruebaHora(String hora){
        int long_hora = hora.length();
        int horaActual = fechaActual.get(Calendar.HOUR_OF_DAY);
        int minutosActuales = fechaActual.get(Calendar.MINUTE);
        // COMPRUEBO QUE LA HORA NO ESTE VACIA
        if(hora.isEmpty()){
            //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Debes rellenar la hora de la reserva",Toast.LENGTH_SHORT).show();
            return false;
        }
        // COMPRUEBO EL FORMATO CORRECTO DE LA HORA
        if(long_hora != 5 || !hora.contains(":")){
           //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de hora inválido",Toast.LENGTH_SHORT).show();
            return false;
        }
        // COMPROBAR QUE EL DIA SEA EL MISMO
        String horas = hora.substring(0,2);
        String minutos = hora.substring(3,5);
        if (Integer.parseInt(horas) < 0 || Integer.parseInt(horas) > 24) {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato de hora invalido", Toast.LENGTH_SHORT).show();
                return false;
        }
        if(Integer.parseInt(minutos) < 0 || Integer.parseInt(minutos) > 59){
            //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de minutos invalido",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Metodo privado para comprobar formato correcto de fecha
    private boolean compruebaFecha(String fecha){
        int longitudFecha = fecha.length();
        // COMPRUEBO QUE LA FECHA NO ESTE VACIA
        if(fecha.isEmpty()){
           // Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Debes rellenar la fecha de la reserva",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(longitudFecha != 10 || !fecha.contains("/")){
            //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de fecha incorrecto",Toast.LENGTH_SHORT).show();
            return false;
        }
        String dia = fecha.substring(0,2);
        String mes = fecha.substring(3,5);
        String año = fecha.substring(6,10);
        // CASO AÑO ANTERIOR
        if(Integer.parseInt(año) < 2020){
            //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "No puedes realizar una reserva de una fecha pasada", Toast.LENGTH_SHORT).show();
            return  false;
        }
        // CASO AÑO ACTUAL
        else if(Integer.parseInt(año) == 2020){
            int diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);
            int mesActual = fechaActual.get(Calendar.MONTH)+1;
            // CASO EN EL QUE LOS MESES SON IGUALES
            if(mesActual == Integer.parseInt(mes)){
                if (Integer.parseInt(mes) < 01 || Integer.parseInt(mes) > 12) {
                    //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de mes", Toast.LENGTH_SHORT).show();
                    return false;
                }
                // COMPROBAMOS QUE EL DIA QUE LE PASAMOS SEA MAYOR QUE EL ACTUAL
                if(diaActual <= Integer.parseInt(dia)){
                    if (Integer.parseInt(dia) < 01 || Integer.parseInt(dia) > 31) {
                        //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de día", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Error, no puede ser un dia del pasado",Toast.LENGTH_SHORT).show();
                    return false;
                }
                // CASO EN EL QUE EL MES QUE LE PASAMOS ES MENOR QUE EL ACTUAL--> NO SE PUEDE HACER RESERVA
            } else if(mesActual > Integer.parseInt(mes)){
                //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Error, no puede ser un mes del pasado",Toast.LENGTH_SHORT).show();
                return false;
                // CASO EN EL QUE EL MES QUE LE METEMOS ES MAYOR QUE EL MES ACTUAL-> NO HACE FALTA COMPROBAR DIA
            } else if(mesActual < Integer.parseInt(mes)){
                if (Integer.parseInt(mes) < 01 || Integer.parseInt(mes) > 12) {
                    //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de mes", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        // CASO AÑO 2021 O FUTURO
        else if(Integer.parseInt(año) >= 2021) {
            if (Integer.parseInt(dia) < 01 || Integer.parseInt(dia) > 31) {
                //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de día", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (Integer.parseInt(mes) < 01 || Integer.parseInt(mes) > 12) {
                //Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de mes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    // METODO AUXILIAR QUE HACE VISIBLES LOS EXTRAS DEL PORSCHE
    public void enseñaExtrasPorsche(){
        cb_automatico_porsche.setVisibility(View.VISIBLE);
        cb_calefactables_porsche.setVisibility(View.VISIBLE);
        cb_techo_panoramico_porsche.setVisibility(View.VISIBLE);
        cb_version_deportiva_porsche.setVisibility(View.VISIBLE);
    }
    // METODO AUXILIAR QUE HACE INVISIBLES LOS EXTRAS DEL PORSCHE
    public void escondeExtrasPorsche(){
        cb_automatico_porsche.setVisibility(View.INVISIBLE);
        cb_calefactables_porsche.setVisibility(View.INVISIBLE);
        cb_techo_panoramico_porsche.setVisibility(View.INVISIBLE);
        cb_version_deportiva_porsche.setVisibility(View.INVISIBLE);
    }
    // METODO AUXILIAR QUE HACE VISIBLES LOS EXTRAS SEAT
    public void enseñaExtrasSeat(){
        cb_navegador_seat.setVisibility(View.VISIBLE);
        cb_sensores_seat.setVisibility(View.VISIBLE);
        cb_control_crucero_seat.setVisibility(View.VISIBLE);
    }
    // METODO AUXILIAR QUE HACE INVISIBLES LOS EXTRAS SEAT
    public void escondeExtrasSeat(){
        cb_navegador_seat.setVisibility(View.INVISIBLE);
        cb_sensores_seat.setVisibility(View.INVISIBLE);
        cb_control_crucero_seat.setVisibility(View.INVISIBLE);
    }
    // METODO AUXILIAR QUE HACE VISIBLES LOS EXTRAS DACIA
    public void enseñaExtrasDacia(){
        cb_navegador_dacia.setVisibility(View.VISIBLE);
        cb_bluetooth_dacia.setVisibility(View.VISIBLE);
        cb_aire_dacia.setVisibility(View.VISIBLE);
    }
    // METODO AUXILIAR QUE HACE VISIBLES LOS EXTRAS DACIA
    public void escondeExtrasDacia(){
        cb_navegador_dacia.setVisibility(View.INVISIBLE);
        cb_bluetooth_dacia.setVisibility(View.INVISIBLE);
        cb_aire_dacia.setVisibility(View.INVISIBLE);
    }
    // METODO AUXILIAR QUE CALCULA EL EXTRA A SUMA EN EL COSTE DE LA TARIFA
    private int calculaExtra(String marca){

    // AÑADIMOS LOS EXTRAS DEPENDIENDO DEL MODELO

        // CASO MARCA PORSCHE
        if (marca.equals(PORSCHE)) {
            // SI NO TIENE NINGUN EXTRA-> TARIFA NORMAL
            if (!cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 0;
            } // SI TIENE EXTRA AUTOMATICO-> TARIFA + 20

            if (cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 20;
            } // SI TIENE EXTRA TECHO -> TARIFA + 5

            else if (!cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 5;
            } // SI TIENE EXTRA CALEFACTABLES

            else if (!cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 10;
            } // SI TIENE EXTRA VERSION DEPORTIVA

            else if (!cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 15;
            } // SI TIENE EXTRA AUTOMATICO + TECHO

            else if (cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 25;
            } // SI TIENE EXTRA AUTOMATICO + CALEFACTABLES

            else if (cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
               return 30;
            } // SI TIENE EXTRA AUTOMACTICO + VERSION DEPORTIVA

            else if (cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 35;
            } // SI TIENE EXTRA TECHO + CALEFACTABLES

            else if (!cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 15;
            } // SI TIENE EXTRA TECHO + VERSION DEPORTIVA

            else if (!cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 25;
            } // SI TIENE EXTRA CALEFACTABLE + VERSION DEPORTIVA

            else if (!cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 25;
            } // SI TIENE EXTRA AUTOMATICO + TECHO + CALEFACTABLE

            else if (cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    !cb_version_deportiva_porsche.isChecked()) {
                return 35;
            } // SI TIENE EXTRA AUTOMATICO + TECHO + VERSION DEPORTIVA

            else if (cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && !cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 40;
            } // SI TIENE EXTRA AUTOMATICO + CALEFACTABLE + VERSION DEPORTIVA

            else if (cb_automatico_porsche.isChecked() && !cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 45;
            } // SI TIENE EXTRA TECHO + CALEFACTABLE + VERSION DEPORTIVA

            else if (!cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 30;
            } // FULL EXTRAS

            else if (cb_automatico_porsche.isChecked() && cb_techo_panoramico_porsche.isChecked() && cb_calefactables_porsche.isChecked() &&
                    cb_version_deportiva_porsche.isChecked()) {
                return 50;
            }
        // CASO MARCA SEAT

        } else if(marca.equals(SEAT)){
            // CASO NINGUN EXTRA

            if(!cb_navegador_seat.isChecked() && !cb_control_crucero_seat.isChecked() && !cb_sensores_seat.isChecked()){
                return 0;
            }// SI TIENE EXTRA NAVEGADOR

            else if(cb_navegador_seat.isChecked() && !cb_control_crucero_seat.isChecked() && !cb_sensores_seat.isChecked()){
                return 15;
            } // SI TIENE EXTRA CONTROL DE CRUCERO

            else if(!cb_navegador_seat.isChecked() && cb_control_crucero_seat.isChecked() && !cb_sensores_seat.isChecked()){
                return 10;
            } // SI TIENE EXTRA SENSORES APARCAMIENTO

            else if(!cb_navegador_seat.isChecked() && !cb_control_crucero_seat.isChecked() && cb_sensores_seat.isChecked()){
                return 5;
            } // SI TIENE EXTRA NAVEGADOR + CONTROL CRUCERO

            else if(cb_navegador_seat.isChecked() && cb_control_crucero_seat.isChecked() && !cb_sensores_seat.isChecked()){
                return 25;
            } // SI TIENE EXTRA NAVEGADOR + SENSORES

            else if(cb_navegador_seat.isChecked() && !cb_control_crucero_seat.isChecked() && cb_sensores_seat.isChecked()){
                return 20;
            } // SI TIENE EXTRA CONTROL CRUCERO + SENSORES

            else if(!cb_navegador_seat.isChecked() && cb_control_crucero_seat.isChecked() && cb_sensores_seat.isChecked()){
                return 15;
            } // SI TIENE FULL EXTRAS
            else if(cb_navegador_seat.isChecked() && cb_control_crucero_seat.isChecked() && cb_sensores_seat.isChecked()){
                return 30;
            }
            // CASO MARCA DACIA
        } else if(marca.equals(DACIA)) {
            // SI NO CONTIENE EXTRAS
            if (!cb_navegador_dacia.isChecked() && !cb_bluetooth_dacia.isChecked() && !cb_aire_dacia.isChecked()) {
                return 0;
            } // SI TIENE EXTRA NAVEGADOR

            else if (cb_navegador_dacia.isChecked() && !cb_bluetooth_dacia.isChecked() && !cb_aire_dacia.isChecked()) {
                return 10;
            } // SI TIENE EXTRA BLUETOOTH

            else if (!cb_navegador_dacia.isChecked() && cb_bluetooth_dacia.isChecked() && !cb_aire_dacia.isChecked()) {
                return 5;
            } // SI TIENE EXTRA AIRE ACONDICIONADO

            else if(!cb_navegador_dacia.isChecked() && !cb_bluetooth_dacia.isChecked() && cb_aire_dacia.isChecked()){
                return 15;
            } // SI TIENE EXTRA NAVEGADOR + BLUETOOTH

            else if(cb_navegador_dacia.isChecked() && cb_bluetooth_dacia.isChecked() && !cb_aire_dacia.isChecked()){
                return 15;
            } // SI TIENE EXTRA NAVEGADOR + AIRE ACONDICIONADO

            else if(cb_navegador_dacia.isChecked() && !cb_bluetooth_dacia.isChecked() && cb_aire_dacia.isChecked()){
                return 25;
            } // SI TIENE EXTRA BLUETOOTH + AIRE ACONDICIONADO

            else if(!cb_navegador_dacia.isChecked() && cb_bluetooth_dacia.isChecked() && cb_aire_dacia.isChecked()){
                return 20;
            } // SI TIENE FULL EXTRAS
            else if(cb_navegador_dacia.isChecked() && cb_bluetooth_dacia.isChecked() && cb_aire_dacia.isChecked()){
                return 30;
            }
        }
        return 0;
    }
}
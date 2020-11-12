package com.example.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ACT_4_FORMULARIO_RESERVA extends AppCompatActivity {

    // Variables pantalla formulario de reserva
    private TextView tv_inicio_alquiler,tv_fin_alquiler,tv_seleccion,tv_franquicia_origen,
                    tv_franquicia_destino;
    private EditText edt_fecha_inicio_alquiler,edt_fecha_fin_alquiler,edt_hora_inicio_alquiler,
                        edt_hora_fin_alquiler;
    private Button btn_crear_reserva;

    private Spinner spinner_franquicia_origen,spinner_franquicia_destino,spinner_marca_vehiculo;

    private Calendar fechaActual = Calendar.getInstance();

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
        tv_franquicia_origen = findViewById(R.id.tv_franquicia_origen_ACT_4_FORMULARIO_RESERVA);
        tv_franquicia_destino = findViewById(R.id.tv_franquicia_destino_ACT_4_FORMULARIO_RESERVA);
        spinner_marca_vehiculo = findViewById(R.id.spinner_marca_ACT_4_FORMULARIO_RESERVA);
        spinner_franquicia_origen = findViewById(R.id.spinner_franquicia_origen_ACT_4_FORMULARIO_RESERVA);
        spinner_franquicia_destino = findViewById(R.id.spinner_franquicia_destino_ACT_4_FORMULARIO_RESERVA);



        // Cuando pulsamos el boton de crear reserva
        btn_crear_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprobamos que las fechas sean validas
                if (!compruebaFecha(edt_fecha_inicio_alquiler.getText().toString())) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaFecha(edt_fecha_fin_alquiler.getText().toString())) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Fecha fin alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_inicio_alquiler.getText().toString())) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Hora inicio alquiler incorrecta", Toast.LENGTH_SHORT).show();
                }
                if (!compruebaHora(edt_hora_fin_alquiler.getText().toString())) {
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
                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, años incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    // 2ºcaso--> Estan en el mismo año--> Mes inicio >= mes fin
                    if (Integer.parseInt(añoInicio) == Integer.parseInt(añoFin)) {
                        // CASO 2.1: Mes fin < mes inicio-> peta
                        if (Integer.parseInt(mesInicio) > Integer.parseInt(mesFin)) {
                            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, meses incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        // CASO 2.2: Mes fin == Mes inicio--> comprobamos el dia
                        if (Integer.parseInt(mesInicio) == Integer.parseInt(mesFin)) {
                            if (Integer.parseInt(diaInicio) > Integer.parseInt(diaFin)) {
                                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, el dia de inicio no puede ser mayor que el de fin de alquiler", Toast.LENGTH_SHORT).show();
                            }
                            // CASO PENDIENTE : los dias son iguales, comparar horas.
                            if (Integer.parseInt(diaInicio) == Integer.parseInt(diaFin)) {
                                // Si la horaInicio < horaFin
                                if (Integer.parseInt(horaInicio) < Integer.parseInt(horaFin)) {
                                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, la hora de inicio no puede ser mayor que el de fin de alquiler", Toast.LENGTH_SHORT).show();
                                }
                                // Si las horas son iguales--> comprueba los minutos
                                if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
                                    if (Integer.parseInt(minutosInicio) > Integer.parseInt(minutosFin)) {
                                        Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Error, los minutos de inicio no puede ser mayor que el de fin de alquiler", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }
    // Metodo privado para comprobar la hora
    private boolean compruebaHora(String hora){
        int long_hora = hora.length();
        // COMPRUEBO QUE LA HORA NO ESTE VACIA
        if(hora.isEmpty()){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Debes rellenar la hora de la reserva",Toast.LENGTH_SHORT).show();
            return false;
        }
        // COMPRUEBO EL FORMATO CORRECTO DE LA HORA
        if(long_hora != 5 || !hora.contains(":")){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de hora inválido",Toast.LENGTH_SHORT).show();
            return false;
        }
        String horas = hora.substring(0,2);
        String minutos = hora.substring(3,5);
        // FORMATO DE HORA
        if(Integer.parseInt(horas) < 0 || Integer.parseInt(horas) > 24){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de hora invalido",Toast.LENGTH_SHORT).show();
            return false;
        }
        // FORMATO DE MINUTOS
        if(Integer.parseInt(minutos) < 0 || Integer.parseInt(minutos) > 59){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de minutos invalido",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Metodo privado para comprobar formato correcto de fecha
    private boolean compruebaFecha(String fecha){
        int longitudFecha = fecha.length();
        // COMPRUEBO QUE LA FECHA NO ESTE VACIA
        if(fecha.isEmpty()){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Debes rellenar la fecha de la reserva",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(longitudFecha != 10 || !fecha.contains("/")){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Formato de fecha incorrecto",Toast.LENGTH_SHORT).show();
            return false;
        }
        String dia = fecha.substring(0,2);
        String mes = fecha.substring(3,5);
        String año = fecha.substring(6,10);
        // CASO AÑO ANTERIOR
        if(Integer.parseInt(año) < 2020){
            Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "No puedes realizar una reserva de una fecha pasada", Toast.LENGTH_SHORT).show();
            return  false;
        }
        // CASO AÑO ACTUAL
        else if(Integer.parseInt(año) == 2020){
            int diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);
            if( diaActual <= Integer.parseInt(dia)){
                if (Integer.parseInt(dia) < 01 || Integer.parseInt(dia) > 31) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de día", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Error, no puede ser un dia del pasado",Toast.LENGTH_SHORT).show();
                return false;
            }
            int mesActual = fechaActual.get(Calendar.MONTH);
            if(mesActual <= Integer.parseInt(mes)){
                if (Integer.parseInt(mes) < 01 || Integer.parseInt(mes) > 12) {
                    Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de mes", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this,"Error, no puede ser un mes del pasado",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // CASO AÑO 2021 O FUTURO
        else if(Integer.parseInt(año) >= 2021) {
            if (Integer.parseInt(dia) < 01 || Integer.parseInt(dia) > 31) {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de día", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (Integer.parseInt(mes) < 01 || Integer.parseInt(mes) > 12) {
                Toast.makeText(ACT_4_FORMULARIO_RESERVA.this, "Formato incorrecto de mes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
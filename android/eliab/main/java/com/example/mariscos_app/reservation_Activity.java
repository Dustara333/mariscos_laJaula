package com.example.mariscos_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class reservation_Activity extends AppCompatActivity {
    EditText myDate, myNombre;
    Spinner myHour, myPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_);
        myDate = (EditText)findViewById(R.id.etFecha);
        myNombre = (EditText)findViewById(R.id.Nombre);
        myHour = (Spinner)findViewById(R.id.spHora);
        myPeople = (Spinner)findViewById(R.id.spPeople);
        myDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                myDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void RETURN(View view){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    public void SEND(View view){
        final String sname = myNombre.getText().toString();
        final String sday = myDate.getText().toString();
        final String shour = myHour.getSelectedItem().toString();
        final String speople = myPeople.getSelectedItem().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (sname.isEmpty()){
            myNombre.setError("Ingresar nombre");
        }
        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://eliabwebii.000webhostapp.com/db_phpcrud/insertar.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Reservacion exitosa")) {
                        Toast.makeText(reservation_Activity.this, "Reservacion exitosa", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(reservation_Activity.this, "response", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(reservation_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String>getParams() throws AuthFailureError{
                    Map<String, String>parametros = new HashMap<String, String>();
                    parametros.put("nombre", sname);
                    parametros.put("dia", sday);
                    parametros.put("hora", shour);
                    parametros.put("personas", speople);
                    return parametros;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertar(View view) {
        final String sname = myNombre.getText().toString();
        final String sday = myDate.getText().toString().trim();
        final String shour = myHour.getSelectedItem().toString();
        final String speople = myPeople.getSelectedItem().toString().trim();
        LocalDate  ldate= LocalDate.now();

        final String sstatus = "activo";
        final String created = ldate.toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        if(sname.isEmpty()){
            Toast.makeText(this, "Ingresar nombre", Toast.LENGTH_SHORT).show();
        }else if(sday.isEmpty()){
            myDate.setError("Ingresar fecha");
        }else if(speople.equals("Personas")){
            Toast.makeText(this, "Ingresar numero de personas", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://eliabwebii.000webhostapp.com/db_phpcrud/insertar.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Datos Insertados")) {
                        Toast.makeText(reservation_Activity.this, "Datos Insertados", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(reservation_Activity.this, "Estuvo Raro", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(reservation_Activity.this, "Hubo Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>parametros = new HashMap<String, String>();
                    parametros.put("nombre", sname);
                    parametros.put("dia", sday);
                    parametros.put("hora", shour);
                    parametros.put("personas", speople);
                    parametros.put("estado", sstatus);
                    parametros.put("created", created);
                    return parametros;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(reservation_Activity.this);
            requestQueue.add(request);
        }
       // Toast.makeText(reservation_Activity.this, speople, Toast.LENGTH_LONG).show();
    }
}


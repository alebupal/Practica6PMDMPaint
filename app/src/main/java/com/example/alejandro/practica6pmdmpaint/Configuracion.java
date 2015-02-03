package com.example.alejandro.practica6pmdmpaint;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class Configuracion extends ActionBarActivity {
    private RadioButton rbPincel, rbGoma, rbRectangulo, rbLinea, rbCirculo;
    private RadioGroup rg;
    private SeekBar sb;
    private CheckBox cbRelleno;
    private TextView tvTamano;
    String forma;
    boolean relleno;
    float tamano = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);


        rbPincel = (RadioButton) findViewById(R.id.rbPincel);
        rbGoma = (RadioButton) findViewById(R.id.rbGoma);
        rbRectangulo = (RadioButton) findViewById(R.id.rbRectangulo);
        rbCirculo = (RadioButton) findViewById(R.id.rbCirculo);
        rbLinea = (RadioButton) findViewById(R.id.rbLinea);
        rg = (RadioGroup) findViewById(R.id.rg);
        sb = (SeekBar) findViewById(R.id.seekBar);
        cbRelleno = (CheckBox) findViewById(R.id.cbRelleno);
        tvTamano = (TextView) findViewById(R.id.tvTamano);


        Bundle bundle = getIntent().getExtras();
        tamano = bundle.getFloat("tamano");
        forma = bundle.getString("forma");
        relleno = bundle.getBoolean("relleno");

        if (forma.compareToIgnoreCase("circulo") == 0) {
            rbCirculo.setChecked(true);
            if (relleno == true) {
                cbRelleno.setChecked(true);
            }else if(relleno == false){
                cbRelleno.setChecked(false);
            }
        } else if (forma.compareToIgnoreCase("rectangulo") == 0) {
            rbRectangulo.setChecked(true);
            if (relleno == true) {
                Log.v("ree", "");
                cbRelleno.setChecked(true);
            }else if(relleno == false){
                cbRelleno.setChecked(false);
            }
        } else if (forma.compareToIgnoreCase("pincel") == 0) {
            rbPincel.setChecked(true);
            cbRelleno.setEnabled(false);
        } else if (forma.compareToIgnoreCase("goma") == 0) {
            rbGoma.setChecked(true);
            cbRelleno.setEnabled(false);
        } else if (forma.compareToIgnoreCase("linea") == 0) {
            rbLinea.setChecked(true);
            cbRelleno.setEnabled(false);
        }


        sb.setProgress(Math.round(tamano));
        tvTamano.setText(sb.getProgress() + "");
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                tvTamano.setText(progresValue + "");
                tamano = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        cbRelleno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRelleno.isChecked()) {
                    relleno = true;
                }else if(cbRelleno.isChecked()==false){
                    relleno = false;
                }

            }
        });
        cbRelleno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRelleno.isChecked()) {
                    relleno = true;
                }else if(cbRelleno.isChecked()==false){
                    Log.v("falso","");
                    relleno = false;
                }
                Log.v("estado",cbRelleno.isChecked()+"");

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbRectangulo.isChecked() || rbCirculo.isChecked()) {
                    cbRelleno.setEnabled(true);
                } else {
                    cbRelleno.setEnabled(false);
                    cbRelleno.setChecked(false);
                    relleno=false;
                }
                if (rbRectangulo.isChecked()) {
                    forma = "rectangulo";
                } else if (rbCirculo.isChecked()) {
                    forma = "circulo";
                } else if (rbPincel.isChecked()) {
                    forma = "pincel";
                } else if (rbGoma.isChecked()) {
                    forma = "goma";
                } else if (rbLinea.isChecked()) {
                    forma = "linea";
                }
            }
        });
    }

    public void aceptar(View v) {
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("forma", forma);
        bundle.putFloat("tamano", tamano);
        Log.v("asasdas", relleno + "");
        bundle.putBoolean("relleno", relleno);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }

}

package com.example.alejandro.practica6pmdmpaint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Dibujo extends ActionBarActivity {
    private Vista v;
    private final static int CONFIG=1;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujo);

        rl = (RelativeLayout)findViewById(R.id.layout);
        v = new Vista(this);
        rl.addView(v);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.m_color) {
            colorPicker();
            return true;
        }else if(id == R.id.m_configuracion) {
            Intent i= new Intent(this, Configuracion.class);
            i.putExtra("forma",v.getForma());
            i.putExtra("tamano",v.getTamano());
            i.putExtra("relleno",v.isRelleno());
            startActivityForResult(i, CONFIG);
            return true;
        }else if(id == R.id.m_nuevo) {

            rl.removeView(v);
            v = new Vista(this);
            rl.addView(v);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void colorPicker(){
        int colorInicial = Color.BLACK;
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, colorInicial, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                tostada(color+"");
                v.setColor(color);
            }

        });
        colorPickerDialog.show();
    }
    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode == CONFIG) {
            String forma=data.getStringExtra("forma");
            float tamano=data.getFloatExtra("tamano",1);
            boolean relleno = data.getBooleanExtra("relleno",false);
            tostada(relleno+"");
            v.setForma(forma);
            v.setTamano(tamano);
            v.setRelleno(relleno);
        }
    }



}

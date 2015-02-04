package com.example.alejandro.practica6pmdmpaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Dibujo extends ActionBarActivity {
    private Vista v;
    private final static int CONFIG=1, CARGAR=2;
    private RelativeLayout rl;
    String nombre;

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
            configuracion();
            return true;
        }else if(id == R.id.m_nuevo) {

            rl.removeView(v);
            v = new Vista(this);
            rl.addView(v);
            return true;
        }else if(id == R.id.m_cargar) {
            cargarImagen();
            return true;
        }else if(id == R.id.m_guardar) {
            guardarImagen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void colorPicker(){
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, v.getColor(), new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
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

            v.setForma(forma);
            v.setTamano(tamano);
            v.setRelleno(relleno);
        }else if(requestCode == CARGAR && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (Exception ex){
                bitmap = null;
            }
            v.cargarImagen(bitmap);
        }

    }

    public void cargarImagen(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent i = Intent.createChooser(galleryIntent, "imagen");
        startActivityForResult(i, CARGAR);
    }

    public void configuracion(){
        Intent i= new Intent(this, Configuracion.class);
        i.putExtra("forma",v.getForma());
        i.putExtra("tamano",v.getTamano());
        i.putExtra("relleno",v.isRelleno());
        startActivityForResult(i, CONFIG);
    }
    public void guardarImagen(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.t_guardar);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialog_guardar, null);
        alert.setView(vista);
        final EditText et=(EditText)vista.findViewById(R.id.tvNombre);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (et.getText().toString().compareTo("")==0){
                            tostada(getString(R.string.tostada_guardar));
                        }else{
                            nombre=et.getText().toString();
                            Bitmap mapaDeBits= v.getMapaDeBits();
                            File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
                            File archivo = new File(carpeta, nombre+".PNG");
                            try {
                                FileOutputStream fos = new FileOutputStream(archivo);
                                mapaDeBits.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(archivo);
                            intent.setData(uri);
                            getApplicationContext().sendBroadcast(intent);
                            tostada(getString(R.string.tostada_guardarBien));
                        }
                    }
                });
        alert.setNegativeButton(android.R.string.no,null);
        alert.show();

    }



}

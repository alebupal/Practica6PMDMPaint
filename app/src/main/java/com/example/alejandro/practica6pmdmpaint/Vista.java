package com.example.alejandro.practica6pmdmpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Alejandro on 03/02/2015.
 */
public class Vista extends View implements Serializable {

    private Paint pincel;
    private int alto, ancho;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private double radio = 0;
    private int color = Color.BLACK;
    private Path rectaPoligonal = new Path();
    private float x0 = 0, y0 = 0, xi = 0, yi = 0;
    private String forma="pincel";
    private float tamano=5;
    public boolean relleno =false;


    public Vista(Context context) {
        super(context);

        pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(tamano);
        pincel.setStyle(Paint.Style.STROKE);

    }

    public void setColor(int e){
        this.color=e;
        Log.v("cambio",""+e);
    }

    public float getTamano() {
        return tamano;
    }

    public void setTamano(float tamano) {
        this.tamano = tamano;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public boolean isRelleno() {
        return relleno;
    }

    public void setRelleno(boolean relleno) {
        this.relleno = relleno;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        alto = h;
        ancho = w;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getForma().compareToIgnoreCase("circulo")==0){
            herramientaCirculo(event);
        }else if(getForma().compareToIgnoreCase("pincel")==0 || getForma().compareToIgnoreCase("goma")==0){
            herramientaPincel(event);
        }
        else if(getForma().compareToIgnoreCase("linea")==0){
            herramientaLinea(event);
        }else if(getForma().compareToIgnoreCase("rectangulo")==0){
            herramientaRectangulo(event);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mapaDeBits, 0, 0, null);

        if(getForma().compareToIgnoreCase("circulo")==0){
            drawCirculo(canvas);
        }else if(getForma().compareToIgnoreCase("pincel")==0){
            drawPincel(canvas);
        }else if(getForma().compareToIgnoreCase("linea")==0){
            drawLinea(canvas);
        }else if(getForma().compareToIgnoreCase("rectangulo")==0){
            drawRectangulo(canvas);
        }else if(getForma().compareToIgnoreCase("goma")==0){
            drawGoma(canvas);
        }

    }




    public void herramientaPincel(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = xi = event.getX();
                y0 = yi = event.getY();
                rectaPoligonal.reset();
                rectaPoligonal.moveTo(x0, y0);
                break;
            case MotionEvent.ACTION_MOVE:
                rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi) / 2);
                xi = x;
                yi = y;
                x0 = xi;
                y0 = yi;
                lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawPath(rectaPoligonal, pincel);
                x0 = y0 = xi = yi = -1;
                invalidate();
                break;
        }

    }

    public void drawPincel(Canvas canvas){
        pincel.setColor(color);
        pincel.setStrokeWidth(tamano);
        canvas.drawPath(rectaPoligonal, pincel);
    }

    public void herramientaLinea(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                invalidate();
                break;
        }

    }

    public void drawLinea(Canvas canvas){
        pincel.setColor(color);
        pincel.setStrokeWidth(tamano);
        canvas.drawLine(x0, y0, xi, yi, pincel);
    }

    public void herramientaCirculo(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(relleno==true){
                    pincel.setStyle(Paint.Style.FILL);
                }else if (relleno==false){
                    pincel.setStyle(Paint.Style.STROKE);
                }
                x0 = x;
                y0 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                radio = Math.sqrt(Math.pow((xi - x0), 2) + Math.pow((yi - y0), 2));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawCircle(x0, y0, (float) radio, pincel);
                radio = 0;
                invalidate();
                break;
        }

    }

    public void drawCirculo(Canvas canvas){
        pincel.setColor(color);
        pincel.setStrokeWidth(tamano);
        canvas.drawCircle(x0, y0, (float) radio, pincel);
    }

    public void herramientaRectangulo(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(relleno==true){
                    pincel.setStyle(Paint.Style.FILL);
                }else if (relleno==false){
                    pincel.setStyle(Paint.Style.STROKE);
                }
                x0 = x;
                y0 = y;
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                invalidate();
                break;
        }

    }

    public void drawRectangulo(Canvas canvas){
        pincel.setColor(color);
        pincel.setStrokeWidth(tamano);
        float xorigen = Math.min(x0, xi);
        float xdestino = Math.max(x0, xi);
        float yorigen = Math.min(y0, yi);
        float ydestino = Math.max(y0, yi);
        canvas.drawRect(xorigen, yorigen, xdestino, ydestino, pincel);
    }

    public void drawGoma(Canvas canvas){
        pincel.setColor(Color.WHITE);
        pincel.setStrokeWidth(tamano);
        canvas.drawRect(x0, y0, xi, yi, pincel);
    }

}

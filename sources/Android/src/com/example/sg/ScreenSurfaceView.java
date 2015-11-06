package com.example.sg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ScreenSurfaceView extends SurfaceView  implements  SurfaceHolder.Callback {

    Context context;
    SurfaceHolder holder;
    Paint screenPaint;
    String TextToSend = "";


    public void setTextToSend(String str){
    	TextToSend = str;
    }
    
    public void init() {
        holder = getHolder();
        holder.addCallback(this);
       

        screenPaint = new Paint();
        screenPaint.setColor(Color.WHITE);
		screenPaint.setStyle(Style.FILL_AND_STROKE);
		screenPaint.setTextSize(7);
    }

    public ScreenSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();

    }

    public ScreenSurfaceView(Context context, AttributeSet attr) {
        super(context,attr);
        this.context = context;
        init();
    }

    public ScreenSurfaceView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.context = context;
        init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub


    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      

    }

    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	canvas.drawRGB(0,0,0);
    	for(int i=0; i<=TextToSend.length()/21; i++){
    		canvas.drawText(TextToSend.substring(0+21*i, Math.min(21*(i+1), TextToSend.length())), 1,7*(i+1)+i, screenPaint);
    	}
    	
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.d("surfaceCreated", "surfaceCreated()");
       
    }

}

package vn.blueskythien.demoprocessing.applet;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;

import processing.core.PApplet;

public class Game extends PApplet implements GMouseTracking.GMouseTrackingListener, SensorEventListener {

    public final Handler handler;
    public double gx = 0;
    public double gy =9.8;
    public double gz = 0;
    public static final Game createInstance()
    {
        return new Game(new Handler());
    }

    private Game(Handler handler)
    {
        this.handler = handler;
    }
    Ball[] bs = new Ball[1000];
    int nb  = 0;
    @Override
    public void settings() {
        super.settings();

        fullScreen();
    }

    int lastT;
    @Override
    public void setup() {
        super.setup();
        frameRate(60);
        getSurfaceView().setOnTouchListener(tracking);
        lastT = millis();


    }



    @Override
    public void draw() {
        background(0);
        int ct = millis();
        double dt = (ct-lastT)/1000.0;
        lastT = ct;
        updateBalls(dt);

        /**********************/
        drawBalls();
        text("GX = "+ gx+ " "+ "GY = "+ gy, 100, 100);
    }


    void updateBalls(double dt)
    {
        for (int i = 0; i< nb; ++i)
        {
            bs[i].update(dt);
        }
        int c = 0;
        for (int i = 0; i< nb; ++i)
            if (bs[i].isAlive())
            {
                bs[c++] = bs[i];
            }
        nb = c;
    }

    void drawBalls()
    {
        for (int i = 0; i < nb; ++i)
            bs[i].draw();
    }

    void  addBall(double x, double y)
    {
        bs[nb] = new Ball(this, x, y);
        ++nb;
    }

    GMouseTracking tracking = new GMouseTracking(this);
    Ball bct = null;
    @Override
    public boolean onMousePressed(GMouseTracking tracking, int mouseId) {

        GMouseTracking.GMouse m = tracking.getMouses(mouseId);
        for (int i = nb-1; i>=0; --i)
        {
            if (bs[i].isHover(m.getX(), m.getY()))
            {
                bct = bs[i];
                bct.onCatch(m.getX(), m.getY());
                return true;
            }
        }

        addBall( m.getX(), m.getY());
        return true;
    }

    @Override
    public boolean onMouseRelease(GMouseTracking tracking, int mouseId) {
        if (bct!= null)
        {
            bct.onRelease();
            bct = null;
        }
        return true;
    }

    @Override
    public void onMouseMove(GMouseTracking tracking, int mouseId) {
        GMouseTracking.GMouse m = tracking.getMouses(mouseId);
        if (bct!= null)
            bct.onMove(m.getX(), m.getY());
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (frameCount%5 == 0){
          gx = -sensorEvent.values[0];
          gy = sensorEvent.values[1];
        gz = abs(sensorEvent.values[2]);
//    }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
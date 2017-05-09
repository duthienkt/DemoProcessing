package vn.blueskythien.demoprocessing;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import processing.android.PFragment;
import vn.blueskythien.demoprocessing.applet.Game;

public class MainActivity extends AppCompatActivity {

    private SensorManager manager;
    private Sensor accel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PFragment fragment = new PFragment();
        Game game = Game.createInstance();
        fragment.setSketch(game);
        getFragmentManager().beginTransaction().replace(R.id.drawer_space, fragment).commit();
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(game, accel, SensorManager.SENSOR_DELAY_GAME);
    }
}

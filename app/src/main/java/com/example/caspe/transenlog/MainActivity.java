package com.example.caspe.transenlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import junit.framework.Test;

public class MainActivity extends AppCompatActivity {

    private ShakeActivity mShaker;
    //Casper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeActivity(this) {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mShaker.setOnShakeListener(new ShakeActivity.OnShakeListener() {
            @Override
            public void onShake() {
                vibe.vibrate(100);
                /*new AlertDialog.Builder(this)
                        .setPositiveButton(android.R.string.ok, null)
                        .setMessage("Shooken")
                        .show();*/
                Toast toast = Toast.makeText(MainActivity.this, "Shooken", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }

    @Override
    public void onResume(){
        mShaker.resume();
        super.onResume();
    }

    @Override
    public void onPause(){
        mShaker.pause();
        super.onPause();
    }
}

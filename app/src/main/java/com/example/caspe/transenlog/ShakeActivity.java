package com.example.caspe.transenlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

/**
 * Created by caspe on 07-03-2018.
 */

public abstract class ShakeActivity implements SensorEventListener {

    private static final int FORCE_TRESHOLD = 350;
    private static final int TIME_TRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;

    private SensorManager mSensorMgr;
    private float mLastX =- 1.0f, mLastY =-1.0f, mLastZ=-1.0f;
    private long mLastTime;
    private OnShakeListener mShakelistener;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;

    public interface OnShakeListener{
        public void onShake();
    }

    public ShakeActivity(Context context){
        mContext = context;
        resume();
    }

    public void setOnShakeListener(OnShakeListener listener){
        mShakelistener = listener;
    }

    public void resume() {
        mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorMgr == null){
            throw new UnsupportedOperationException("Sensor not supported");
        }
        boolean supported = mSensorMgr.registerListener((SensorListener) this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        if (!supported){
            mSensorMgr.unregisterListener((SensorListener) this, SensorManager.SENSOR_ACCELEROMETER);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public void pause(){
        if (mSensorMgr != null){
            mSensorMgr.unregisterListener((SensorListener) this, SensorManager.SENSOR_ACCELEROMETER);
            mSensorMgr = null;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent, int sensor, float[] values) {
        if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
        long now = System.currentTimeMillis();

        if ((now - mLastForce) > SHAKE_TIMEOUT) {
            mShakeCount = 0;
        }

        if ((now - mLastTime) > TIME_TRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(values [SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z]
                    - mLastX - mLastY - mLastZ) / diff * 1000;
            if (speed > FORCE_TRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakelistener != null){
                        mShakelistener.onShake();
                    }
                }
                mLastForce = now;
            }

            mLastTime = now;
            mLastX = values[SensorManager.DATA_X];
            mLastY = values[SensorManager.DATA_Y];
            mLastZ = values[SensorManager.DATA_Z];
        }

    }

    public void onAccuracyChanged(Sensor sensor, int sensors, int accuracy) {

    }
}

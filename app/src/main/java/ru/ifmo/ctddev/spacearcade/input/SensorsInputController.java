package ru.ifmo.ctddev.spacearcade.input;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

import ru.ifmo.ctddev.spacearcade.MainActivity;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class SensorsInputController extends InputController {

    private static final double DEGREES_IN_RADIAN = 57.295780d;
    private static final double ANGLE = 30.0d;

    private final Activity activity;

    private final float[] rotationMatrix = new float[16];
    private final float[] orientation = new float[3];
    private final float[] lastMagneticFields = new float[3];
    private final float[] lastAccelerometerFields = new float[3];

    private final int rotation;

    private final SensorEventListener magneticChangesListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, lastMagneticFields, 0, 3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener accelerometerChangesListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, lastAccelerometerFields, 0, 3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public SensorsInputController(MainActivity activity) {
        this.activity = activity;
        rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void onStart() {
        registerListeners();
    }

    private void registerListeners() {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        sensorManager.registerListener(accelerometerChangesListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(magneticChangesListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onResume() {
        registerListeners();
    }

    @Override
    public void onStop() {
        unregisterListeners();
    }

    private void unregisterListeners() {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(accelerometerChangesListener);
        sensorManager.unregisterListener(magneticChangesListener);
    }

    @Override
    public void onPause() {
        unregisterListeners();
    }

    @Override
    public void onUpdate() {
        horizontalFactor = getHorizontalAxis() / ANGLE;

        if (horizontalFactor > 1) {
            horizontalFactor = 1;
        } else if (horizontalFactor < -1) {
            horizontalFactor = -1;
        }

        verticalFactor = 0;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private double getHorizontalAxis() {
        if (SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometerFields, lastMagneticFields)) {
            if (rotation == Surface.ROTATION_0) {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrix);
                SensorManager.getOrientation(rotationMatrix, orientation);
                return orientation[1] * DEGREES_IN_RADIAN;
            } else {
                SensorManager.getOrientation(rotationMatrix, orientation);
                return -orientation[1] * DEGREES_IN_RADIAN;
            }
        } else {
            if (rotation == Surface.ROTATION_0) {
                return -lastAccelerometerFields[0] * 5;
            } else {
                return -lastAccelerometerFields[1] * -5;
            }
        }
    }
}

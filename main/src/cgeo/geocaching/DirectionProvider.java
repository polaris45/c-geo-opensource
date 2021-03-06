package cgeo.geocaching;

import cgeo.geocaching.compatibility.Compatibility;
import cgeo.geocaching.utils.MemorySubject;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DirectionProvider extends MemorySubject<Float> implements SensorEventListener {

    private final SensorManager sensorManager;

    public DirectionProvider(final Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onFirstObserver() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onLastObserver() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        /*
        * There is a bug in Android, which appearently causes this method to be called every
        * time the sensor _value_ changed, even if the _accuracy_ did not change. So logging
        * this event leads to the log being flooded with multiple entries _per second_,
        * which I experienced when running cgeo in a building (with GPS and network being
        * unreliable).
        *
        * See for example https://code.google.com/p/android/issues/detail?id=14792
        */

        //Log.i(Settings.tag, "Compass' accuracy is low (" + accuracy + ")");
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        notifyObservers(event.values[0]);
    }

    public static float getDirectionNow(final Activity activity, final float direction) {
        return Compatibility.getDirectionNow(direction, activity);
    }

}

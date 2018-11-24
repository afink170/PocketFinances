package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for handling LiveData returned from test database queries
 */
public class LiveDataTestUtil {

    /**
     * Method for unwrapping the passed LiveData and returning value of the unwrapped object.
     *
     * @param liveData Parameter should be an object or list of objects wrapped in a LiveData object
     * @param <T> T specifies the object type
     * @return An unwrapped object of type T (as input)
     * @throws InterruptedException
     */
    public static < T > T getValue(LiveData< T > liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer< T > observer = new Observer < T > () {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }
}

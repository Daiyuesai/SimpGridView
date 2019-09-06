package com.xiaozhanxiang.simplegridview;

import android.content.Context;
import android.graphics.Matrix;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.xiaozhanxiang.simplegridview", appContext.getPackageName());

        Matrix matrix = new Matrix();

        matrix.postTranslate(10,10);
        float[] points = new float[2];
        points[0] = 0;
        points[1] = 0;
        matrix.mapPoints(points);

        Log.i(TAG, "testMapPoint: " + points[0] + "  ,:" + points[1]);
    }
}

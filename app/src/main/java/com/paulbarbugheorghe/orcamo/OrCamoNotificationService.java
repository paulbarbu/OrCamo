package com.paulbarbugheorghe.orcamo;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class OrCamoNotificationService extends Service {
    private static final String TAG = "OrCamoNotificationService";

    private WindowManager wm;
    private ImageView notificationCamo;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] pos = intent.getIntArrayExtra(OrCamoAccessibilityService.EXTRA_POS);

        Log.d(TAG, "POS: left=" + pos[0] + " top=" + pos[1] +
                " right=" + pos[2] + " bot=" + pos[3]);


        Log.d(TAG, "Service started!");
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        params.x = pos[0]; // left
        params.y = pos[1]; // top
        params.width = pos[2]- pos[0]; // right - left
        params.height = pos[3] - pos[1]; // bottom - top
        params.gravity = Gravity.TOP | Gravity.LEFT;

        Log.d(TAG, "x=" + params.x + " y= " + params.y + " w=" + params.width + " h=" + params.height);

        // only if I'm in the messenger application I need to account for the keyboard

        notificationCamo = new ImageView(this);
        notificationCamo.setImageResource(R.drawable.ic_launcher); //TODO: change this and remove the original one
        notificationCamo.setScaleType(ImageView.ScaleType.FIT_XY);

        wm.addView(notificationCamo, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(notificationCamo != null)
        {
            wm.removeView(notificationCamo);
        }

        Log.d(TAG, "Service destroyed!");
    }
}

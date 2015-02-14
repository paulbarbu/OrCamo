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

public class OrCamoService extends Service {
    private static final String TAG = "OrCamoService";

    private WindowManager wm;
    private ImageView image;
    private int x, y, w, h;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] pos = intent.getIntArrayExtra(OrCamoAccessibilityService.EXTRA_POS);

        Log.d(TAG, "POS: left=" + pos[0] + " top=" + pos[1] +
                " right=" + pos[2] + " bot=" + pos[3]);

        x = pos[0]; // left
        y = pos[1]; // top
        w = 20;
        h = 20;


        Log.d(TAG, "Service started!");
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        image = new ImageView(this);
        image.setImageResource(R.drawable.ic_launcher); //TODO: change this and remove the original one

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.OPAQUE
        );

        params.x = x;
        params.y = y;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.height = h;
        params.width = w;


        wm.addView(image, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(image != null)
        {
            wm.removeView(image);
        }

        Log.d(TAG, "Service destroyed!");
    }
}

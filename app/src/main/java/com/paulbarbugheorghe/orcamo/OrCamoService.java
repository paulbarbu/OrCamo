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
    private ImageView camo;

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
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        params.x = pos[0]; // left
        params.y = pos[1]; // top
        params.width = pos[2]- pos[0]; // right - left
        params.height = pos[3] - pos[1]; // bottom - top
        params.gravity = Gravity.TOP | Gravity.LEFT;

        Log.d(TAG, "x=" + params.x + " y= " + params.y + " w=" + params.width + " h=" + params.height);

        camo = new ImageView(this);
        camo.setImageResource(R.drawable.ic_launcher); //TODO: change this and remove the original one

        wm.addView(camo, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(camo != null)
        {
            wm.removeView(camo);
        }

        Log.d(TAG, "Service destroyed!");
    }
}

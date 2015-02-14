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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "Service created!");
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        image = new ImageView(this);
        image.setImageResource(R.drawable.ic_launcher); //TODO: change this

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.x = 0;
        params.y = 0;

        wm.addView(image, params);
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

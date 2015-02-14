package com.paulbarbugheorghe.orcamo;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class OrCamoAccessibilityService extends AccessibilityService {
    public final static String EXTRA_POS = "com.paulbarbugheorghe.orcamo.POS";
    private static final String TAG = "OrCamoAccessibilityService";
    private static final String ELEM_CLASS_NAME = "android.widget.ImageButton";
    private static final String ELEM_DESC = "Like";
    private boolean isOrcaActive = false;
    private boolean isCamouflageServiceRunning = false;

    private void toggleService(AccessibilityEvent event)
    {
        if(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED != event.getEventType())
        {
            return;
        }

        Log.d(TAG, "pkg=" + event.getPackageName() + " winid="+event.getWindowId());
        Log.d(TAG, "type: " + Integer.toHexString(event.getEventType()));

        AccessibilityNodeInfo src = event.getSource();

        //TODO: recycle

        List<AccessibilityNodeInfo> nodes = src.findAccessibilityNodeInfosByText(ELEM_DESC);
        Log.d(TAG, "Found (" + nodes.size() + "): " + nodes.toString());

        if(nodes.size() < 1)
        {
            Log.w(TAG, "Cannot find a node by text (" + ELEM_DESC + ") in the window");
            stopService();
            return;
        }

        AccessibilityNodeInfo thumbsUp = null;
        for (AccessibilityNodeInfo node : nodes)
        {
            if(ELEM_CLASS_NAME.equals(node.getClassName()))
            {
                thumbsUp = node;
                //TODO: what if there are multiple nodes that have the class? - I currently look only at the first
                break;
            }
        }

        if(null == thumbsUp) {
            Log.d(TAG, "Cannot find the node by class (" + ELEM_CLASS_NAME + ")");
            stopService();
            return;
        }

        Rect bounds = new Rect();
        thumbsUp.getBoundsInScreen(bounds);
        Log.d(TAG, "Thumbsup - bounds in screen: left=" + bounds.left + " top=" + bounds.top +
                " right=" + bounds.right + " bot=" + bounds.bottom);

        if(isCamouflageServiceRunning) {
            Log.d("AccService", "Camouflage service already running");

            stopService(); // because the position may change if the keyboard is shown/hidden
        }

        Intent intent = new Intent(this, OrCamoService.class);

        int[] pos = {bounds.left, bounds.top, bounds.right, bounds.bottom};
        intent.putExtra(EXTRA_POS, pos);

        //TODO: position the overlay properly
        Log.d(TAG, "Starting window service");
        startService(intent);
        isCamouflageServiceRunning = true;
    }

    private void stopService()
    {
        if(!isCamouflageServiceRunning) {
            Log.d(TAG, "Camouflage service already stopped");
            return;
        }
        Log.d(TAG, "stopService()");

        Intent intent = new Intent(this, OrCamoService.class);
        Log.d(TAG, "Stopping window service");
        stopService(intent);
        isCamouflageServiceRunning = false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
        {
            Log.d(TAG, "Window state changed: " + event.getPackageName());

            if("com.facebook.orca".equals(event.getPackageName().toString())) {
                isOrcaActive = true;
                toggleService(event);
            }
            else {
                isOrcaActive = false;
                stopService();
            }
        }
        else if(isOrcaActive)
        {
            toggleService(event);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}

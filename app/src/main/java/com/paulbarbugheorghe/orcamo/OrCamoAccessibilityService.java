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
    private int[] lastPos = {-1, -1, -1, -1}; // last position of the like button

    /**
     * This will be called only when in messenger and its purpose is to start/stop the camouflage
     * service depending on what it finds in the messenger window (the like button and its position)
     */
    private void toggleCamouflage(AccessibilityEvent event)
    {
        int eventType = event.getEventType();

        Log.d(TAG, "pkg=" + event.getPackageName() + " winid="+event.getWindowId());
        Log.d(TAG, "type: " + event.eventTypeToString(eventType));

        // only handle the situation where the window is shown
        // or it is uncovered by the notification "bar"
        if(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED != eventType &&
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED != eventType)
        {
            return;
        }

        AccessibilityNodeInfo src = event.getSource();
        if(null == src) {
            Log.w(TAG, "src is null");
            stopCamouflage();
            return;
        }

        List<AccessibilityNodeInfo> nodes = src.findAccessibilityNodeInfosByText(ELEM_DESC);
        Log.d(TAG, "Found (" + nodes.size() + "): " + nodes.toString());

        if(nodes.size() < 1)
        {
            Log.w(TAG, "Cannot find a node by text (" + ELEM_DESC + ") in the window");
            stopCamouflage();
            return;
        }

        AccessibilityNodeInfo thumbsUp = null;
        for (AccessibilityNodeInfo node : nodes)
        {
            if(ELEM_CLASS_NAME.equals(node.getClassName()))
            {
                thumbsUp = node;
                //FIXME: what if there are multiple nodes that have the class? - I currently look only at the first
                break;
            }
        }

        if(null == thumbsUp) {
            Log.d(TAG, "Cannot find the node by class (" + ELEM_CLASS_NAME + ")");
            src.recycle();
            stopCamouflage();
            return;
        }

        Rect bounds = new Rect();
        thumbsUp.getBoundsInScreen(bounds);
        Log.d(TAG, "Thumbs up - bounds in screen: left=" + bounds.left + " top=" + bounds.top +
                " right=" + bounds.right + " bot=" + bounds.bottom);
        int[] pos = {bounds.left, bounds.top, bounds.right, bounds.bottom};

        if(isCamouflageServiceRunning) {
            Log.d(TAG, "Camouflage service already running");

            if(isSamePosition(pos)) {
                Log.d(TAG, "Thumbs up is in the same position");
                src.recycle();
                return;
            }
            else {
                // the position has changed, I have to stop the camouflage and restart it in a new position
                stopCamouflage();
            }
        }

        lastPos = pos;

        Intent intent = new Intent(this, OrCamoService.class);
        intent.putExtra(EXTRA_POS, pos);

        Log.d(TAG, "Starting window service");
        startService(intent);
        isCamouflageServiceRunning = true;
        src.recycle();
    }

    private boolean isSamePosition(int[] pos)
    {
        for(int i=0; i<4; ++i)
        {
            if(pos[i] != lastPos[i])
            {
                return false;
            }
        }

        return true;
    }

    private void stopCamouflage()
    {
        if(!isCamouflageServiceRunning) {
            Log.d(TAG, "Camouflage service already stopped");
            return;
        }
        Log.d(TAG, "stopCamouflage()");

        Intent intent = new Intent(this, OrCamoService.class);
        Log.d(TAG, "Stopping window service");
        stopService(intent);
        isCamouflageServiceRunning = false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String pkgName = event.getPackageName().toString();
        
        if("com.paulbarbugheorghe.orcamo".equals(pkgName))
        {
            // if I wouldn't ignore self events I'd end up stopping myself
            Log.d(TAG, "Event in self, ignoring");
            return;
        }

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
        {
            Log.d(TAG, "Window state changed: " + pkgName);

            if("com.facebook.orca".equals(pkgName)) {
                isOrcaActive = true;
                toggleCamouflage(event);
            }
            else {
                isOrcaActive = false;
                stopCamouflage();
            }
        }
        else if(isOrcaActive)
        {
            toggleCamouflage(event);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}

package com.paulbarbugheorghe.orcamo;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class OrCamoAccessibilityService extends AccessibilityService {
    private static final String TAG = "OrCamoAccessibilityService";
    private static final String TEXT_LINE_CLASS_NAME = "android.widget.ImageButton";
    private static final String TEXT_LINE_DESC = "Write a message";
    private boolean isOrcaActive = false;
    private boolean isTextLineSelected = false;
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

        List<AccessibilityNodeInfo> nodes = src.findAccessibilityNodeInfosByText(TEXT_LINE_DESC);
        Log.d(TAG, "Found (" + nodes.size() + "): " + nodes.toString());

        if(nodes.size() < 1)
        {
            Log.w(TAG, "Cannot find a node by text (" + TEXT_LINE_DESC + ") in the window");
            // necessary workaround for when the text line gets selected and the keyboard appears
            // when the keyboard appears the window content is modified, hence a new accessibility
            // event is fired and this method won't find the line then, but previously it was selected,
            // so this keeps the camouflage service running
            if (!isTextLineSelected) {
                //TODO: BUG: Open chat - select the textinput - go back to contact list, service is still running (bug)
                stopService();
            }
            return;
        }

        AccessibilityNodeInfo textLine = null;
        for (AccessibilityNodeInfo node : nodes)
        {
            if(TEXT_LINE_CLASS_NAME.equals(node.getClassName()))
            {
                textLine = node;
                //TODO: what if there are multiple nodes that have the class? - I currently look only at the first
                break;
            }
        }

        if(null == textLine) {
            Log.d(TAG, "Cannot find the node by class (" + TEXT_LINE_CLASS_NAME + ")");

            // necessary workaround for when the text line gets selected and the keyboard appears
            // when the keyboard appears the window content is modified, hence a new accessibility
            // event is fired and this method won't find the line then, but previously it was selected,
            // so this keeps the camouflage service running
            if (!isTextLineSelected)
            {
                stopService();
            }
            return;
        }

        Log.d(TAG, "text line is selected: " + textLine.isSelected());
        isTextLineSelected = textLine.isSelected();

        if(isCamouflageServiceRunning) {
            Log.d("AccService", "Camouflage service already running");
            return;
        }

        Intent intent = new Intent(this, OrCamoService.class);

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

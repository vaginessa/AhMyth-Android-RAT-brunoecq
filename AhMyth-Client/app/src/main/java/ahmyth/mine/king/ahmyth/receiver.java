package ahmyth.mine.king.ahmyth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try
        {
            //Toast.makeText(context,"THIS IS MY ALARM",Toast.LENGTH_LONG).show();

            //Toast.makeText(context,"ALARM RECEIVER",Toast.LENGTH_LONG).show()
            Intent i = new Intent(context, MainService.class);
            context.startService(i);

        }
        catch ( Exception e  )
        {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

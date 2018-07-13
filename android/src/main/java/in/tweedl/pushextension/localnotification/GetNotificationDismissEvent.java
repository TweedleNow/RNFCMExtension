package in.tweedl.pushextension.localnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetNotificationDismissEvent extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String inboxStyleKey = intent.getStringExtra("myNotificationId");
        Log.e("foooooo", " rec " + inboxStyleKey);
        if (inboxStyleKey != null) {
            DatabaseManager dm = new DatabaseManager();
            dm.deleteIboxStyle(context, inboxStyleKey);
        }
    }
}
/* Your code to handle the event here */

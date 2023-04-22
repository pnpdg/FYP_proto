package fyp.test.fyp_proto;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class UnhideAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String number  = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        if(number.equals("*1234#")){
            PackageManager packageManager = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, SplashActivity.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Intent newintent = new Intent(context, SplashActivity.class);
            newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newintent);

            setResultData(null);
        }
    }
}

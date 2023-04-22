package fyp.test.fyp_proto;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class UserInactivityTimeoutUtil extends Application{
    private static Handler mHandler = new Handler();
    private static Runnable mIdleRunnable = new Runnable() {
        @Override
        public void run() {
            logout();
        }
    };
    private static int mTimeout = 5000;
    private static Context mContext;

    private UserInactivityTimeoutUtil(){}

    public static void startTimeout(Context context){
        mContext = context;
        mHandler.postDelayed(mIdleRunnable, mTimeout);
    }

    public static void stopTimeout(){
        mHandler.removeCallbacks(mIdleRunnable);
    }

    private static void logout(){
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }

    public void onUserInteracted() {
        startTimeout(this);
    }
}

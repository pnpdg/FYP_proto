package fyp.test.fyp_proto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity implements LogoutListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).registerSessionListener(this);
        ((MyApp) getApplication()).startUserSession();
    }


    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        ((MyApp) getApplication()).onUserInteracted();
    }

    @Override
    public void onSessionLogout() {
        finish();

        startActivity(new Intent(this, SplashActivity.class));
    }

}

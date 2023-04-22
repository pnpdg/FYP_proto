package fyp.test.fyp_proto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity implements LogoutListener{

    //private Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        sw = findViewById(R.id.switch_AutoLock);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    UserInactivityTimeoutUtil.startTimeout(BaseActivity.this);
                }
                else{
                    UserInactivityTimeoutUtil.stopTimeout();
                }
            }
        });*/
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).registerSessionListener(this);
        ((MyApp) getApplication()).startUserSession();
    }
    //}

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        ((MyApp) getApplication()).onUserInteracted();
    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event){
        UserInactivityTimeoutUtil.startTimeout(this);
        return super.onTouchEvent(event);
    }*/

    @Override
    public void onSessionLogout() {
        finish();

        startActivity(new Intent(this, SplashActivity.class));
    }

}

package fyp.test.fyp_proto;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;


import java.util.Set;

public class Setting extends BaseActivity {
    ImageView backBtn;
    //private Switch sw;
    //Switch s1;
    MyApp settings;
    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String SWITCH1 = "switch1";
    //private boolean switchOnOff;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //sw = findViewById(R.id.switch_AutoLock);
        //s1 = findViewById(R.id.switch_AddtoScreen);
        backBtn = findViewById(R.id.left_icon);
        settings = (MyApp) getApplication();
        //loadSharedPreferences();
        //updateViews();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChooseFunction.class));
                finish();
            }
        });

        /*sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    UserInactivityTimeoutUtil.startTimeout(Settings.this);
                }
                else{
                    UserInactivityTimeoutUtil.stopTimeout();
                }
            }
        });*/

        /*s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    hideApp();
                    //saveData();
                }
                else{
                    //unhideApp();
                    //saveData();
                }
            }
        });*/

    }

    /*private void loadSharedPreferences() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sp.getBoolean(SWITCH1, false);
    }*/

    /*public void updateViews(){
        s1.setChecked(switchOnOff);
    }*/

    /*public void saveData(){
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(SWITCH1, s1.isChecked());
        editor.apply();
    }*/

    public void hideApp(){
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, SplashActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public void unhideApp(){
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, SplashActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event){
        UserInactivityTimeoutUtil.startTimeout(this);
        return super.onTouchEvent(event);
    }*/



}

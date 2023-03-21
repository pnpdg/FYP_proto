package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainNote extends AppCompatActivity {

    Button note1Btn,note2Btn,note3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

        note1Btn = findViewById(R.id.note1_btn);
        note2Btn = findViewById(R.id.note2_btn);
        note3Btn = findViewById(R.id.note3_btn);

        note1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
}
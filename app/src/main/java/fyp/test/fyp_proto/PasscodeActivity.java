package fyp.test.fyp_proto;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;

public class PasscodeActivity extends AppCompatActivity {
    EditText passEditText;
    Button enterBtn;
    String title,content,password,docId;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        passEditText = findViewById(R.id.note_pass_text);
        enterBtn = findViewById(R.id.enter_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        password = getIntent().getStringExtra("password");
        docId = getIntent().getStringExtra("docId");


        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passEditText.getText().toString().equals(password)){
                    Intent intent = new Intent(context,NoteDetailsActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("content",content);
                    intent.putExtra("password",password);
                    intent.putExtra("docId",docId);

                    Toast.makeText(PasscodeActivity.this, "Password Correct", Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                }else{
                    Toast.makeText(PasscodeActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class user_detail_activity extends AppCompatActivity {

    EditText fullName, userEmail, Role1, Role2;
    TextView Uid;
    ImageButton saveUserBtn;
    String name,email,role1,role2,userId;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.fullname_text);
        userEmail = findViewById(R.id.email_text);
        Role1 = findViewById(R.id.role1_text);
        Role2 = findViewById(R.id.role2_text);
        Uid = findViewById(R.id.uid_text);

        // receive
        name = getIntent().getStringExtra("FullName");
        email = getIntent().getStringExtra("UserEmail");
        role1 = getIntent().getStringExtra("Role1");
        role2 = getIntent().getStringExtra("Role2");
        userId = getIntent().getStringExtra("Uid");

        fullName.setText(name);
        userEmail.setText(email);
        Role1.setText(role1);
        Role2.setText(role2);
        Uid.setText(userId);


        saveUserBtn = findViewById(R.id.save_user_btn);

        saveUserBtn.setOnClickListener(v->saveUser(Uid.getText().toString()));

    }

    void saveUser(String s){
        String fullname = fullName.getText().toString();
        String useremail = userEmail.getText().toString();
        String role1 = Role1.getText().toString();
        String role2 = Role2.getText().toString();

        if(fullname ==null || fullname.isEmpty()){
            fullName.setError("FullName is required!!");
            return;
        }

        if(useremail ==null || useremail.isEmpty()){
            userEmail.setError("User Email is required!!");
            return;
        }

        if(role1 == null || role1.isEmpty()){
            Role1.setError("Role is required!!");
            return;
        }

        User user = new User();
        user.setFullName(fullname);
        user.setUserEmail(useremail);
        user.setRole1(role1);
        user.setRole2(role2);

        // get current user
        Toast.makeText(user_detail_activity.this,"User Detail Updated",Toast.LENGTH_SHORT).show();
        // put current user uid
        DocumentReference df = fStore.collection("Users").document(s);
        // store data
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("FullName",user.FullName);
        userInfo.put("UserEmail",user.UserEmail);
        // specify if the user's role

        userInfo.put("Role1",Role1.getText().toString());
        userInfo.put("Role2",Role2.getText().toString());
        userInfo.put("Uid",userId);

        df.set(userInfo);

        // go back to login activity
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();


    }
    /*
    void saveUserToFireBase(User user){
        // get current user
        Toast.makeText(user_detail_activity.this,"User Detail Updated",Toast.LENGTH_SHORT).show();
        // put current user uid
        DocumentReference df = fStore.collection("Users").document(userId);
        // store data
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("FullName",user.FullName);
        userInfo.put("UserEmail",user.UserEmail);
        // specify if the user's role

        userInfo.put("Role",7);
        userInfo.put("uId",userId);

        df.set(userInfo);

        // go back to login activity
        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        finish();


    }*/
}
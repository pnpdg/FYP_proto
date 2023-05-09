package fyp.test.fyp_proto;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    EditText fullName;
    TextView userEmail,Uid,Role;
    ImageButton saveUserBtn;
    String name,email,role,userId;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        fullName = findViewById(R.id.fullname_text);
        userEmail = findViewById(R.id.email_text);
        Role = findViewById(R.id.role_text);
        Uid = findViewById(R.id.uid_text);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());


        fStore = FirebaseFirestore.getInstance();

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("FullName");
                    Log.d(TAG, "Name: " + name);
                    fullName.setText(name);
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: ", e);
            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("UserEmail");
                    Log.d(TAG, "Email: " + email);
                    userEmail.setText(email);

                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: ", e);
            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String role = documentSnapshot.getString("Role");
                    Log.d(TAG, "role: " + role);
                    Role.setText(role);
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: ", e);
            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String uid = documentSnapshot.getString("Uid");
                    Log.d(TAG, "uid: " + uid);
                    Uid.setText(uid);
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: ", e);
            }
        });


        saveUserBtn = findViewById(R.id.save_user_btn);
        saveUserBtn.setOnClickListener(v->saveUser(Uid.getText().toString()));


    }

    void saveUser(String s){
        String fullname = fullName.getText().toString();
        String useremail = userEmail.getText().toString();
        String role = Role.getText().toString();

        if(fullname ==null || fullname.isEmpty()){
            fullName.setError("FullName is required!!");
            return;
        }

        if(useremail ==null || useremail.isEmpty()){
            userEmail.setError("User Email is required!!");
            return;
        }

        if(role == null || role.isEmpty()){
            Role.setError("Role is required!!");
            return;
        }

        User user = new User();
        user.setFullName(fullname);
        user.setUserEmail(useremail);
        user.setRole(role);

        // get current user
        Toast.makeText(UserProfile.this,"User Detail Updated",Toast.LENGTH_SHORT).show();
        // put current user uid
        //DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        DocumentReference df = fStore.collection("Users").document(s);
        // store data
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("FullName",user.FullName);
        userInfo.put("UserEmail",user.UserEmail);
        // specify if the user's role

        userInfo.put("Role",Role.getText().toString());
        userInfo.put("Uid",s);

        df.set(userInfo);

        // go back to login activity
        startActivity(new Intent(getApplicationContext(),ChooseFunction.class));
        finish();


    }


}
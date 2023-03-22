package fyp.test.fyp_proto;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainNote extends AppCompatActivity {

    public static String role1,role2;
    Button note1Btn,note2Btn,note3Btn,logoutBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        note1Btn = findViewById(R.id.note1_btn);
        note2Btn = findViewById(R.id.note2_btn);
        note3Btn = findViewById(R.id.note3_btn);
        logoutBtn = findViewById(R.id.ch_logout_btn);

        CollectionReference collectionRef = fStore.collection("Users");
        DocumentReference documentRef = collectionRef.document(user.getUid());

        // note 1 access
        note1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Save data to variable
                            role1 = documentSnapshot.getString("Role1");
                            Log.d(TAG, "Role1 is : " +role1);

                            role2 = documentSnapshot.getString("Role2");
                            Log.d(TAG, "Role2 is : " +role2);

                            if(role1.equals("1")||role2.equals("1")){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(MainNote.this, "Access failed!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting data", e);
                    }
                });

            }
        });

        // note 2 access
        note2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Save data to variable
                            role1 = documentSnapshot.getString("Role1");
                            Log.d(TAG, "Role1 is : " +role1);

                            role2 = documentSnapshot.getString("Role2");
                            Log.d(TAG, "Role2 is : " +role2);

                            if(role1.equals("2")||role2.equals("2")){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(MainNote.this, "Access failed!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting data", e);
                    }
                });

            }
        });

        // note 3 access
        note3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Save data to variable
                            role1 = documentSnapshot.getString("Role1");
                            Log.d(TAG, "Role1 is : " +role1);

                            role2 = documentSnapshot.getString("Role2");
                            Log.d(TAG, "Role2 is : " +role2);

                            if(role1.equals("3")||role2.equals("3")){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(MainNote.this, "Access failed!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting data", e);
                    }
                });

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });




    }


}
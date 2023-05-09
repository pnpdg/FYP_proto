package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PasswordResetMenu extends AppCompatActivity {
    ImageView backBtn;
    TextView resetGallery, resetNotes;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    boolean existGallery, existNotes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_menu);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        backBtn = findViewById(R.id.left_icon);
        resetGallery = findViewById(R.id.resetGalleryOption);
        resetNotes = findViewById(R.id.resetNotesOption);

        checkGalleryPassExist();
        checkNotesPassExist();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChooseFunction.class));
                finish();
            }
        });

        resetGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(existGallery == true){
                    startActivity(new Intent(getApplicationContext(), ResetPassGalleryFolder.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "No password set for gallery folder", Toast.LENGTH_SHORT).show();
                }

            }
        });

        resetNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(existNotes == true){
                    startActivity(new Intent(getApplicationContext(), ResetPassNotesFolder.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "No password set for notes folder", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(PasswordResetMenu.this,ChooseFunction.class));
    }

    public void checkGalleryPassExist(){
        db.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    existGallery = true;
                }
                else{
                    existGallery = false;
                }
            }
        });
    }

    public void checkNotesPassExist(){
        db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    existNotes = true;
                }
                else{
                    existNotes = false;
                }
            }
        });
    }
}
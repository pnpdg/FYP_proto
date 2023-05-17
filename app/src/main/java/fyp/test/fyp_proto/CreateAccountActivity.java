package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    EditText fullNameEditText,emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullNameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullNameEditText);
                checkField(emailEditText);
                checkField(passwordEditText);
                checkField(confirmPasswordEditText);

                validatedData(emailEditText.getText().toString(),passwordEditText.getText().toString(),confirmPasswordEditText.getText().toString());

                if(valid){
                    // start the user registration
                    fAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // get current user
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(CreateAccountActivity.this,"Account Created",Toast.LENGTH_SHORT).show();
                            // put current user uid
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            // store data
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullNameEditText.getText().toString());
                            userInfo.put("UserEmail",emailEditText.getText().toString());
                            // specify if the user's role

                            userInfo.put("Role","1");
                            userInfo.put("Uid",user.getUid());

                            df.set(userInfo);

                            // go back to login activity
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateAccountActivity.this,"Failed to Create Account",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        loginBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }

        return valid;
    }

    boolean validatedData(String email,String password,String confirmPassword){
        // validate the data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            valid = false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            valid =  false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not matched");
            valid = false;
        }

        return valid;
    }
}
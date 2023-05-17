package fyp.test.fyp_proto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ResetPassGalleryFile extends AppCompatActivity {
    EditText qns1, qns2, qns3;
    MaterialButton resetBtn;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String ans1, ans2, ans3;
    String documentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_folder);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        qns1 = findViewById(R.id.firstQns_text);
        qns2 = findViewById(R.id.secQns_text);
        qns3 = findViewById(R.id.thirdQns_text);
        resetBtn = findViewById(R.id.resetPass_btn);

        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            documentId = bundle.getString("docId");
        }

        getAnsData();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAns1 = qns1.getText().toString();
                String userAns2 = qns2.getText().toString();
                String userAns3 = qns3.getText().toString();
                if(validateAns(userAns1, userAns2, userAns3)){
                    resetPass();
                }
            }
        });

    }

    public void getAnsData(){
        db.collection("Recovery").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    ans1 = decrypt(documentSnapshot.get("Qns 1").toString(), currentUser.getUid());
                    ans2 = decrypt(documentSnapshot.get("Qns 2").toString(), currentUser.getUid());
                    ans3 = decrypt(documentSnapshot.get("Qns 3").toString(), currentUser.getUid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]  bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    public String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public boolean validateAns(String qns1, String qns2, String qns3){
        if(qns1.trim().equals(ans1.trim()) && qns2.trim().equals(ans2.trim()) && qns3.trim().equals(ans3.trim())){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong Answers", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(ResetPassGalleryFile.this,Gallery.class));
    }

    public void resetPass() {
        Map<String, Object> map = new HashMap<>();
        String pass = "";
        map.put("pass", pass);
        db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(documentId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(ResetPassGalleryFile.this,Gallery.class));
                Toast.makeText(getApplicationContext(), "Password reset", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

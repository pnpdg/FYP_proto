package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AskSecurityQns extends AppCompatActivity {
    EditText qns1, qns2, qns3;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    MaterialButton cfmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_security_qns);

        qns1 = findViewById(R.id.firstQns_text);
        qns2 = findViewById(R.id.secQns_text);
        qns3 = findViewById(R.id.thirdQns_text);
        cfmBtn = findViewById(R.id.cfmAns_btn);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        cfmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans1 = qns1.getText().toString();
                String ans2 = qns2.getText().toString();
                String ans3 = qns3.getText().toString();

                if(checkField(ans1, ans2, ans3)){
                    try {
                        storeAns(ans1, ans2, ans3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void storeAns(String q1, String q2, String q3) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String encryptQ1 = encrypt(q1, currentUser.getUid());
        String encryptQ2 = encrypt(q2, currentUser.getUid());
        String encryptQ3 = encrypt(q3, currentUser.getUid());
        map.put("Qns 1", encryptQ1);
        map.put("Qns 2", encryptQ2);
        map.put("Qns 3", encryptQ3);
        map.put("Uid", currentUser.getUid());
        db.collection("Recovery").document(currentUser.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(getApplicationContext(),ChooseFunction.class));
            }
        });
    }

    public boolean checkField(String text1, String text2, String text3){
        if(!text1.isEmpty() && !text2.isEmpty() && !text3.isEmpty()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(AskSecurityQns.this,LoginActivity.class));
    }

    public SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]  bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    public String encrypt (String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedvalue;
    }
}
package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
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

public class setImagePass extends AppCompatActivity {

    EditText imgPassText;
    MaterialButton setImgPassBtn;
    String documentId;
    FirebaseFirestore fr;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image_pass);

        fr = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        imgPassText = findViewById(R.id.img_setPass_text);
        setImgPassBtn = findViewById(R.id.img_setPass_btn);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            documentId = bundle.getString("docId");
        }

        setImgPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = imgPassText.getText().toString();
                if(checkField(password)){
                    try {
                        setPass(password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public void setPass(String pass) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String encryptPass = encrypt(pass, currentUser.getUid());
        map.put("pass", encryptPass);
        fr.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(documentId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Password created/updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Gallery.class));
            }
        });
    }

    public boolean checkField(String text){
        if(!text.isEmpty()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
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
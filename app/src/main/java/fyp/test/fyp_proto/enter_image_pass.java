package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class enter_image_pass extends AppCompatActivity {

    MaterialButton enterPassBtn;
    EditText enterPassText;
    FirebaseUser currentUser;
    String encryptedPass;
    String decryptedPass;
    String imgUri;
    String documentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_image_pass);

        enterPassBtn = findViewById(R.id.enterImage_btn);
        enterPassText = findViewById(R.id.enter_image_pass_text);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            encryptedPass = bundle.getString("password");
            imgUri = bundle.getString("image");
            documentId = bundle.getString("docId");
        }

        try {
            decryptedPass = decrypt(encryptedPass, currentUser.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        enterPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = enterPassText.getText().toString();
                if(checkField(password)){
                    if(verifyPass(password, decryptedPass) == true){
                        Intent intent = new Intent(enter_image_pass.this, FullImage.class);
                        intent.putExtra("docId", documentId);
                        intent.putExtra("image", imgUri);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully enter", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public boolean verifyPass(String pass, String decrypted){
        //String decryptedPass = fromFireStoreDecrypt();
        if(pass.equals(decrypted)){
            return true;
        }
        else{
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

    public String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
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
}
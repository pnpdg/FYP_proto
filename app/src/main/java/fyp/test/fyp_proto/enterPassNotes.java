package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class enterPassNotes extends BaseActivity {

    EditText notesPassText;
    Button enterBtn;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    String unhidePass;
    String hiddenPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pass_notes);

        db = FirebaseFirestore.getInstance();
        notesPassText =  findViewById(R.id.enter_notes_pass_text);
        enterBtn =  findViewById(R.id.enterNotes_btn);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        unhidePass = fromFireStoreDecrypt();

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = notesPassText.getText().toString();
                if(checkField(password)){
                    if(verifyPass(password, unhidePass) == true) {
                        startActivity(new Intent(enterPassNotes.this, MainActivity.class));
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

    public String fromFireStoreDecrypt() {
        db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").document(documentID).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        hiddenPass = documentSnapshot.getString("pass");
                                        try {
                                            unhidePass= decrypt(hiddenPass, currentUser.getUid());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Log.i("Show decrypted pass", unhidePass);
                                    }
                                }
                            });
                }
            }
        });
        return unhidePass;
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

    public SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]  bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
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
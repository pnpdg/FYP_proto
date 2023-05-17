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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class setPassGallery extends BaseActivity {

    EditText galleryPassText;
    MaterialButton createBtn;
    FirebaseFirestore fr;
    FirebaseUser currentUser;
    long num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass_gallery);

        galleryPassText = findViewById(R.id.gallery_pass_text);
        createBtn = findViewById(R.id.create_btn);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fr = FirebaseFirestore.getInstance();
        numOfPass();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkField(galleryPassText) == true){
                    String password = galleryPassText.getText().toString();
                    if(num < 1){
                        try {
                            waitTask wt = new waitTask();
                            wt.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            updateFireStore(password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private void toFireStore(String pass) throws Exception {
        UploadPass model;
        String encryptedPass = encrypt(pass, currentUser.getUid());
        model = new UploadPass(encryptedPass);
        UploadPass finalModel = model;
        fr.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finalModel.setDocId(documentReference.getId());
                fr.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").document(finalModel.getDocId())
                        .set(finalModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Password created/updated", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create Password", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void updateFireStore(String pass) throws Exception {
        fr.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    fr.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").document(documentID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    try {
                                        waitTask wt2 = new waitTask();
                                        wt2.execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("deletion of old pass","Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("deletion of old pass","Failure");
                                }
                            });
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

    public String encrypt (String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedvalue;
    }
    public boolean checkField(EditText text){
        if(!text.getText().toString().isEmpty()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void numOfPass(){
        fr.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if(task.isSuccessful()){
                            AggregateQuerySnapshot snapshot = task.getResult();
                            num = snapshot.getCount();
                            Log.i("Show number of entries in gallery pass", String.valueOf(num));
                        }
                    }
                });
    }

    class waitTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(setPassGallery.this);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                toFireStore(galleryPassText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void unused){
            startActivity(new Intent(getApplicationContext(),ChooseFunction.class));
            super.onPostExecute(unused);
            pd.dismiss();
        }
    }
}
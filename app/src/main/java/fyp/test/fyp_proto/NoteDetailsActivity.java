package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText,passwordEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,password,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;
    FirebaseAuth fAuth;
    String AES = "AES";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        fAuth = FirebaseAuth.getInstance();

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        passwordEditText = findViewById(R.id.notes_password_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        //receive
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        password = getIntent().getStringExtra("password");
        docId = getIntent().getStringExtra("docId");

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        // get uid to dec
        FirebaseUser user = fAuth.getCurrentUser();
        String decryptionKey = user.getUid();
        String decContent = null;

        titleEditText.setText(title);

        try {
            decContent = decrypt(content,decryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentEditText.setText(decContent);

        passwordEditText.setText(password);

        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(v->saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)->deleteNoteFromFirebase());

    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String notePassword = passwordEditText.getText().toString();
        // encrypt the noteContents
        String encContent = null;

        // get user current user id and use it as key

        FirebaseUser user = fAuth.getCurrentUser();
        String encryptionKey = user.getUid();


        try{
            encContent = encrypt(noteContent, encryptionKey);
        }catch (Exception e){
            e.printStackTrace();
        }


        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();

        note.setTitle(noteTitle);
        note.setContent(encContent);
        note.setPassword(notePassword);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();

    }

    String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    String encrypt (String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedvalue;
    }

    SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]  bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        }else{
            //create note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding note");

                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting note");

                }
            }
        });

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
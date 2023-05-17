package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class FullImage extends AppCompatActivity {

    ImageView fullImage;
    String imgUri;
    String documentId;
    ImageButton imgOptions;
    String password;
    FirebaseUser currentUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        imgOptions = findViewById(R.id.image_menu_btn);
        fullImage = findViewById(R.id.fullImageView);
        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            imgUri = bundle.getString("image");
            documentId = bundle.getString("docId");
            password = bundle.getString("password");
        }
        Picasso.get()
                .load(imgUri)
                .into(fullImage);


        imgOptions.setOnClickListener(view -> {
            PopupMenu pm = new PopupMenu(FullImage.this, imgOptions);
            pm.getMenu().add("Set/Update Image Password");
            if(!password.equals("")){
                pm.getMenu().add("Delete Image Password");
            }
            pm.show();

            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getTitle() == "Set/Update Image Password"){
                        Intent intent = new Intent(FullImage.this, setImagePass.class);
                        intent.putExtra("docId", documentId);
                        startActivity(intent);
                        return true;
                    }
                    if(menuItem.getTitle() == "Delete Image Password"){
                        deletePass();
                        return true;
                    }
                    return false;
                }
            });

        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(FullImage.this,Gallery.class));
    }

    public void deletePass() {
        Map<String, Object> map = new HashMap<>();
        String pass = "";
        map.put("pass", pass);
        db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(documentId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Password deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseFunction extends AppCompatActivity {
    private CardView noteBtn;
    private CardView  galleryBtn;

    Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_function);

        noteBtn = findViewById(R.id.Note_card);
        galleryBtn = findViewById(R.id.Gallery_card);
       logoutBtn = findViewById(R.id.ch_logout_btn);

        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
               // finish();
            }
        });

        // redirect
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Gallery.class));
                //finish();
            }
        });

       logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }
    // homepage menu **
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_homepagemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_Addbtn:
                Toast.makeText(this, "Add selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_CreateFolder:
                Toast.makeText(this, "Create Folders selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_AddFiles:
                Toast.makeText(this, "Add Files selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_Customise:
                Toast.makeText(this, "Customise selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_Settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}


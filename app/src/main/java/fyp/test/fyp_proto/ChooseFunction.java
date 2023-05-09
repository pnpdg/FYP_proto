package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


import androidx.cardview.widget.CardView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseFunction extends BaseActivity{
    private CardView noteBtn;
    private CardView  galleryBtn;
    private CardView profileBtn;
    // private Menu settingsBtn;

    Button logoutBtn;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    long numGallery;
    long numNotes;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_function);

        noteBtn = findViewById(R.id.Note_card);
        galleryBtn = findViewById(R.id.Gallery_card);
        profileBtn = findViewById(R.id.Profile_card);
      //  settingsBtn = findViewById(R.id.menu_Settings);
       logoutBtn = findViewById(R.id.ch_logout_btn);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        waitTask wt = new waitTask();
        wt.execute();

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numNotes < 1){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),enterPassNotes.class));
                }
                // finish();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numGallery < 1){
                    startActivity(new Intent(getApplicationContext(),Gallery.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),enterPassGallery.class));
                }
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
            int id = item.getItemId();

        if (id == R.id.menu_Settings) {
            Intent intent1 = new Intent(ChooseFunction.this,Setting.class);
            this.startActivity(intent1);
            return true;
        }

        if (id == R.id.menu_faq) {
            Intent intent1 = new Intent(ChooseFunction.this,FAQ.class);
            this.startActivity(intent1);
            return true;
        }

       // switch (item.getItemId()) {

           // case R.id.menu_Settings:
              //  Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
              //  return true;
            //default:
                return super.onOptionsItemSelected(item);
       // }

    }


    public void numOfPassGallery(){
        db.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if(task.isSuccessful()){
                            AggregateQuerySnapshot snapshot = task.getResult();
                            numGallery = snapshot.getCount();
                            Log.i("Show number of entries in gallery pass", String.valueOf(numGallery));
                        }
                    }
                });
    }

    public void numOfPassNotes(){
        db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if(task.isSuccessful()){
                            AggregateQuerySnapshot snapshot = task.getResult();
                            numNotes = snapshot.getCount();
                            Log.i("Show number of entries in notes pass", String.valueOf(numNotes));
                        }
                    }
                });
    }


    class waitTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(ChooseFunction.this);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            numOfPassGallery();
            numOfPassNotes();
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void unused){
            super.onPostExecute(unused);
            pd.dismiss();
        }
    }


}


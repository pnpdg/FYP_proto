package fyp.test.fyp_proto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NoteAdapter.OnItemClickListener{

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    long num;
    ArrayList<Note> notesarrayList = new ArrayList<Note>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener(v-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)));
        menuBtn.setOnClickListener(v->showMenu());
        setupRecyclerView();
        numOfPass();
    }

    void showMenu(){
        //display menu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Main Page");
        popupMenu.getMenu().add("Set or Update password");
        if(num > 0){
            popupMenu.getMenu().add("Delete password");
        }
        popupMenu.getMenu().add("Logout");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }

                if(menuItem.getTitle() == "Set or Update password"){
                    startActivity(new Intent(MainActivity.this,setPassNotes.class));
                    return true;
                }

                if(menuItem.getTitle() == "Delete password"){
                    deletePass();
                    return true;
                }

                if(menuItem.getTitle()=="Main Page"){
                    startActivity(new Intent(MainActivity.this,ChooseFunction.class));
                    finish();
                    return true;
                }

                return false;
            }
        });
    }

    public void numOfPass(){
        db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").count()
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

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("notes").document(currentUser.getUid()).collection("my_notes").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    Note obj = d.toObject(Note.class);
                    notesarrayList.add(obj);
                }
            }
        });

        noteAdapter = new NoteAdapter(options, this, notesarrayList);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setOnItemClickListener(MainActivity.this);

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(MainActivity.this,ChooseFunction.class));
    }

    @Override
    protected void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        noteAdapter.notifyDataSetChanged();
    }

    public void deletePass(){
        db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("Password").document(currentUser.getUid()).collection("Notes Pass").document(documentID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Password Deleted", Toast.LENGTH_SHORT).show();
                                    Log.i("Deletion of password","Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to delete password", Toast.LENGTH_SHORT).show();
                                    Log.i("Deletion of password","Failure");
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onResetNotesPassClick(int position) {
        Note selectedItem = notesarrayList.get(position);
        Intent intent = new Intent(MainActivity.this, ResetPassNotesFile.class);
        intent.putExtra("docId", selectedItem.getID());
        startActivity(intent);
    }

    @Override
    public void onNoteItemClick(int position) {
    }
}
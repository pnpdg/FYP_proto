package fyp.test.fyp_proto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends BaseActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener(v-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)));
        menuBtn.setOnClickListener(v->showMenu());

        setupRecyclerView();


    }

    void showMenu(){
        //display menu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Main Page");
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

                if(menuItem.getTitle()=="Main Page"){
                    startActivity(new Intent(MainActivity.this,ChooseFunction.class));
                    finish();
                    return true;
                }

                return false;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_item,menu);
//
//        MenuItem item = menu.findItem(R.id.searchId);
//
//        SearchView searchView = (SearchView) item.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // 돋보기 클릭시 행동
//                Log.d("TAG","the str is : "+str);
//                Intent intent = new Intent(context,MainActivity.class);;
//                intent.putExtra("str",str);
//
//
//                context.startActivity(intent);
////
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 텍스트 입력시 작동
//                Log.d("TAG","the entered message is : "+newText);
//                str = newText;
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

    void searchRecyclerView(String newText){
        // 이부분 etCollectionReferenceForNotes() 수정하면 될듯

        Log.d("TAG","onSuccess search for : "+newText);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseFirestore.getInstance().collection("notes").
                document(currentUser.getUid()).collection("my_notes").whereEqualTo("title",newText);


        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
        Log.d("TAG","why it is not working");

    }

    void setupRecyclerView(){
        // 이부분 etCollectionReferenceForNotes() 수정하면 될듯
        //Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Query query = FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes").whereEqualTo("title",newText);

        //Log.d("TAG","onSuccess : "+newText);
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);

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
}
package fyp.test.fyp_proto;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    private OnItemClickListener mListener;
    ArrayList<Note> notearrayList;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context, ArrayList<Note> arrayList) {
        super(options);
        this.context = context;
        this.notearrayList = arrayList;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener(v->{
                if (note.password.equals("")){
                    Intent intent = new Intent(context,NoteDetailsActivity.class);
                    intent.putExtra("title",note.title);
                    intent.putExtra("content",note.content);
                    intent.putExtra("password",note.password);
                    String docId = this.getSnapshots().getSnapshot(position).getId();
                    intent.putExtra("docId",docId);

                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context,PasscodeActivity.class);;
                    intent.putExtra("title",note.title);
                    intent.putExtra("content",note.content);
                    intent.putExtra("password",note.password);
                    String docId = this.getSnapshots().getSnapshot(position).getId();
                    intent.putExtra("docId",docId);

                    context.startActivity(intent);
                }

        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView titleTextView,contentTextView,timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notes_title_text_view);
            contentTextView = itemView.findViewById(R.id.notes_content_text_view);
            timestampTextView = itemView.findViewById(R.id.notes_timestamp_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onNoteItemClick(position);
                }
            }
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch(menuItem.getItemId()){
                        case 1 :
                            mListener.onResetNotesPassClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int position = getAdapterPosition();
            Note noteCurrent = notearrayList.get(position);
            if (noteCurrent.getPassword()!= ""){
                MenuItem resetPass = contextMenu.add(Menu.NONE, 1, 1, "Reset password");
                resetPass.setOnMenuItemClickListener(this);
            }


        }

    }

    public interface OnItemClickListener{
        void onResetNotesPassClick(int position);

        void onNoteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}

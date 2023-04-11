package fyp.test.fyp_proto;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RetrieveAdp extends RecyclerView.Adapter<RetrieveAdp.ViewHolder> {
    //Initialize variable
    //ArrayList<UploadGallery> newarrayList;
    ArrayList<UploadGallery> newarrayList;
    private Context mContext;
    private OnItemClickListener mListener;

    //Create constructor
    public RetrieveAdp(Context context,ArrayList<UploadGallery> arrayList){
        this.mContext = context;
        this.newarrayList = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_gallery_items, parent, false);
        //Pass holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)  {
        //Print image using uri
        UploadGallery uploadCurrent = newarrayList.get(position);
        Picasso.with(mContext)
                        .load(uploadCurrent.getImageUrl())
                        .into(holder.ivImage);
        //Glide.with(mContext).load(Uri.parse(newarrayList.get(position))).into(holder.ivImage);

        //holder.ivImage.setImageURI(Uri.parse(newarrayList.get(position)));
        holder.textView.setText("Image No." + position);

    }

    @Override
    public int getItemCount() {
        //Pass list size
        return newarrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        //Initialize variable
        ImageView ivImage;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            ivImage = itemView.findViewById(R.id.iv_image);
            textView = itemView.findViewById(R.id.imageTxt);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1, "Delete only");
            MenuItem delUnhide = contextMenu.add(Menu.NONE, 2, 2, "Delete and unhide");

            delete.setOnMenuItemClickListener(this);
            delUnhide.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch(menuItem.getItemId()){
                        case 1 :
                            mListener.onDeleteClick(position);
                            return true;
                        case 2 :
                            mListener.onDelUnhideClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onDeleteClick(int position);

        void onDelUnhideClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}


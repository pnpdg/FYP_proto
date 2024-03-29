package fyp.test.fyp_proto;

import android.content.Context;
import android.graphics.RenderEffect;
import android.graphics.Shader;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class RetrieveAdp extends RecyclerView.Adapter<RetrieveAdp.ViewHolder> {
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
        if(uploadCurrent.getPass()!= ""){
            Picasso.get()
                    .load(uploadCurrent.getImageUrl())
                    .transform(new BlurTransformation(mContext, 140, 8))
                    .into(holder.ivImage);
        }else{
            Picasso.get()
                    .load(uploadCurrent.getImageUrl())
                    .into(holder.ivImage);
        }

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
            int position = getAdapterPosition();
            UploadGallery uploadCurrent = newarrayList.get(position);
            if(uploadCurrent.getPass()!= ""){
                MenuItem resetPass = contextMenu.add(Menu.NONE, 3, 3, "Reset password");
                resetPass.setOnMenuItemClickListener(this);
            }

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
                        case 3 :
                            mListener.onResetPassClick(position);
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

        void onResetPassClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}


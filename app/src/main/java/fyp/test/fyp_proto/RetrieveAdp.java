package fyp.test.fyp_proto;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RetrieveAdp extends RecyclerView.Adapter<RetrieveAdp.ViewHolder> {
    //Initialize variable
    ArrayList<String> newarrayList;

    //Create constructor
    public RetrieveAdp(ArrayList<String> arrayList){
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Print image using uri
        holder.ivImage.setImageURI(Uri.parse(newarrayList.get(position)));
        holder.textView.setText("Image No." + position);
    }

    @Override
    public int getItemCount() {
        //Pass list size
        return newarrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Initialize variable
        ImageView ivImage;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            ivImage = itemView.findViewById(R.id.iv_image);
            textView = itemView.findViewById(R.id.imageTxt);
        }
    }

}


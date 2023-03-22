package fyp.test.fyp_proto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.users,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);

        holder.fullName.setText(user.FullName);
        holder.userEmail.setText(user.UserEmail);
        holder.Role1.setText(user.Role1);
        holder.Role2.setText(user.Role2);
        holder.Uid.setText(user.Uid);

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context,user_detail_activity.class);
            intent.putExtra("FullName",user.FullName);
            intent.putExtra("UserEmail",user.UserEmail);
            intent.putExtra("Role1",user.Role1);
            intent.putExtra("Role2",user.Role2);
            intent.putExtra("Uid",user.Uid);
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, userEmail, Role1,Role2, Uid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullname);
            userEmail = itemView.findViewById(R.id.email);
            Role1 = itemView.findViewById(R.id.role1);
            Role2 = itemView.findViewById(R.id.role2);
            Uid = itemView.findViewById(R.id.uid);
        }
    }
}

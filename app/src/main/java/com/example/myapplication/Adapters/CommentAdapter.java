package com.example.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.Comment;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context myContext;
    private List<Comment> myComments;

    public CommentAdapter(Context myContext, List<Comment> myComments) {
        this.myContext = myContext;
        this.myComments = myComments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(myContext).inflate(R.layout.row_comment, parent, false);

        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        final String picUrl = myComments.get(position).getUimg();
        if(!picUrl.equals("")) {
            Picasso.get().load(picUrl).into(holder.userImg);
        }
        holder.username.setText(myComments.get(position).getUname());
        holder.content.setText(myComments.get(position).getContent());
        holder.dateInp.setText(timeStampToString((long) myComments.get(position).getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return myComments.size();
    }

    private String timeStampToString(long timeStamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date = DateFormat.format("MMM dd, yyyy | hh:mm", calendar).toString();
        return date;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView userImg;
        TextView username, content, dateInp;

        public CommentViewHolder(View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.comment_user_img);
            username = itemView.findViewById(R.id.comment_username);
            content = itemView.findViewById(R.id.comment_content);
            dateInp = itemView.findViewById(R.id.comment_date);
        }
    }
}

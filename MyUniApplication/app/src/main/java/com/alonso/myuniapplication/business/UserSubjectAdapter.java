package com.alonso.myuniapplication.business;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alonso.myuniapplication.R;

import java.util.List;

public class UserSubjectAdapter extends RecyclerView.Adapter<UserSubjectAdapter.MyViewHolder> {

    private List<Subject> subjects;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    public UserSubjectAdapter(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.title.setText(subject.getName()  );
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
}

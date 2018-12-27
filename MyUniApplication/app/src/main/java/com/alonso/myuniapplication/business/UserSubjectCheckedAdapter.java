package com.alonso.myuniapplication.business;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.alonso.myuniapplication.R;

import java.util.List;

public class UserSubjectCheckedAdapter extends RecyclerView.Adapter<UserSubjectCheckedAdapter.MyViewHolder>  {

    private List<Subject> subjects;
    private final OnItemClickListener listener;
    View itemView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CheckedTextView checkedSubject;

        public MyViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.title);
            checkedSubject = view.findViewById(R.id.checkedTextView);
        }

        public void bind(final Subject sub, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(checkedSubject.isChecked()){
//                        checkedSubject.setChecked(false);
                        checkedSubject.setCheckMarkDrawable(0);
                        checkedSubject.setChecked(false);
                    } else {
//                        checkedSubject.setChecked(true);
                        checkedSubject.setCheckMarkDrawable(R.drawable.ic_check_circle_black_24dp);
                        checkedSubject.setChecked(true);
                    }
                    listener.onItemClick(sub);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Subject item);
    }


    public UserSubjectCheckedAdapter(List<Subject> subjects, OnItemClickListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subjects_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Subject subject = subjects.get(position);
//        holder.title.setText(subject.getName());
        holder.checkedSubject.setText(subject.getName());
//        holder.checkedSubject.setChecked(true);

        holder.bind(subjects.get(position), listener);
    }



    @Override
    public int getItemCount() {
        return subjects.size();
    }

}
package com.alonso.myuniapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.Subject;

import java.util.HashMap;
import java.util.List;

public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.MyViewHolder>{

    private List<Subject> subjects;
    HashMap<Integer, Boolean> tutoringSubjects;
    Context context;

    public TutorsAdapter(Context context, List<Subject> subjects, HashMap<Integer, Boolean> tutoringSubjects) {
        this.subjects = subjects;
        this.context = context;
        this.tutoringSubjects = tutoringSubjects;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView subjectCheckedTextView;

        public MyViewHolder(View view) {
            super(view);
            subjectCheckedTextView = (CheckedTextView) view.findViewById(R.id.simpleCheckedTextView);
        }
    }

    @NonNull
    @Override
    public TutorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row_item, parent, false);
        return new TutorsAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = position;
        Subject subject = subjects.get(position);
        holder.subjectCheckedTextView.setText(subject.getCode() + " - " + subject.getName());

        if(tutoringSubjects.get(subject.getCode()) != null && tutoringSubjects.get(subject.getCode())){
            holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.ic_school_black_24dp);
            holder.subjectCheckedTextView.setChecked(true);
        }
        setSubjectOnClickListener(holder, pos);
    }
    private void setSubjectOnClickListener(final MyViewHolder holder, final int pos) {
        holder.subjectCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.subjectCheckedTextView.isChecked();
                Subject subject = subjects.get(pos);

                if (value) {
                    tutoringSubjects.remove(subject.getCode());
                    // set check mark drawable and set checked property to false
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check_ic);
                    holder.subjectCheckedTextView.setChecked(false);
                    Toast.makeText(context, "un-Checked", Toast.LENGTH_SHORT).show();
                } else {
                    tutoringSubjects.put(subject.getCode(), true);
                    // set check mark drawable and set checked property to true
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.ic_school_black_24dp);
                    holder.subjectCheckedTextView.setChecked(true);
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

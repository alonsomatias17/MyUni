package com.alonso.myuniapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.alonso.myuniapplication.R;
import com.alonso.myuniapplication.business.Subject;

import java.util.List;


public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.MyViewHolder> {

    //    private String[] singersName;
    private List<Subject> subjects;
    Context context;


    public SingerAdapter(Context context, List<Subject> subjects) {
        this.subjects = subjects;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView subjectCheckedTextView;

        public MyViewHolder(View view) {
            super(view);
            subjectCheckedTextView = (CheckedTextView) view.findViewById(R.id.simpleCheckedTextView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectCheckedTextView.setText(subject.getName());
        // perform on Click Event Listener on CheckedTextView
        holder.subjectCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.subjectCheckedTextView.isChecked();
                if (value) {
                    // set check mark drawable and set checked property to false
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check_ic);
                    holder.subjectCheckedTextView.setChecked(false);
                    Toast.makeText(context, "un-Checked", Toast.LENGTH_SHORT).show();
                } else {
                    // set check mark drawable and set checked property to true
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check);
                    holder.subjectCheckedTextView.setChecked(true);
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
}


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ApprovedSubjectsAdapter extends RecyclerView.Adapter<ApprovedSubjectsAdapter.MyViewHolder> {

    private List<Subject> subjects;
    Context context;
    String userEmail;


    public ApprovedSubjectsAdapter(Context context, List<Subject> subjects, String userEmail) {
        this.subjects = subjects;
        this.context = context;
        this.userEmail = userEmail;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    //Itera al setear la lista por cada uno de los elementos
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final int pos = position;
        Subject subject = subjects.get(position);
        holder.subjectCheckedTextView.setText(subject.getCode() + " - " + subject.getName());
        if(subject.isApproved()) {
            holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check);
            holder.subjectCheckedTextView.setChecked(true);
        }else if(subject.isOnGoing()) {
            holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.ic_create_black_24dp);
            holder.subjectCheckedTextView.setChecked(false);
        }

        // perform on Click Event Listener on CheckedTextView
        setSubjectOnClickListener(holder, pos);
    }

    private void setSubjectOnClickListener(final MyViewHolder holder, final int pos) {
        holder.subjectCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.subjectCheckedTextView.isChecked();
                Subject subject = subjects.get(pos);
                if (value) {
                    subject.setState(Subject.NOT_APPROVED);
                    // set check mark drawable and set checked property to false
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check_ic);
                    holder.subjectCheckedTextView.setChecked(false);
                    Toast.makeText(context, "un-Checked", Toast.LENGTH_SHORT).show();
                } else {
                    if(subject.isOnGoing()){
                        removeOnGoingSubject(subject.getCode());
                    }
                    subject.setState(Subject.APPROVED);
                    // set check mark drawable and set checked property to true
                    holder.subjectCheckedTextView.setCheckMarkDrawable(R.drawable.check);
                    holder.subjectCheckedTextView.setChecked(true);
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeOnGoingSubject(int subjectCode) {
        DatabaseReference onGoingSubjectsRef = FirebaseDatabase.getInstance().getReference().child("OnGoingSubjects");
        onGoingSubjectsRef.child(Integer.toString(subjectCode))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String email= "";
                            email = (String)snapshot.getValue();
                            if(email.equals(userEmail))
                                snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
}


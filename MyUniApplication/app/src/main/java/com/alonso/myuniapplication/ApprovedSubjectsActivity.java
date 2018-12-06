package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserSubjectAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApprovedSubjectsActivity extends AppCompatActivity {

    private User user;
    private List<Subject> subjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserSubjectAdapter usAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_subjects);

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("UserToApSubject");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        List<Subject> mockedSubjets = new ArrayList<>();
        mockedSubjets.add(user.getCareer().getSubjects().get(0));
        mockedSubjets.add(user.getCareer().getSubjects().get(1));

//        mockedSubjets.add(new Subject(100, "Análisis I", "Análisis I", 1));

        usAdapter = new UserSubjectAdapter(mockedSubjets);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(usAdapter);

        usAdapter.notifyDataSetChanged();
    }
}

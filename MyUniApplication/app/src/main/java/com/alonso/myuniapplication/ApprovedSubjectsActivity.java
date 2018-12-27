package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alonso.myuniapplication.adapters.SingerAdapter;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserSubjectCheckedAdapter;

public class ApprovedSubjectsActivity extends AppCompatActivity {

    private User user = new User();
    private RecyclerView recyclerView;
//    private UserSubjectAdapter usAdapter;
    private UserSubjectCheckedAdapter usAdapter;

    private SingerAdapter mAdapter;

    String[] singersName = {"Mohammad Rafi", "Lata Mangeshkar", "Sonu Nigam", "Kishore Kumar",
            "Sreya Ghoshal ","Asha Bhosle","Udit Narayan","Alka Yagnik"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_subjects);

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("UserToApSubject");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (user.getUserName().equals("")) {
                }
            }
        }).start();


//        usAdapter = new UserSubjectAdapter(user.getApprovedSubjects());
//        usAdapter = new UserSubjectCheckedAdapter(user.getApprovedSubjects());

        /*recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(usAdapter);



        recyclerView.setAdapter(new UserSubjectCheckedAdapter(user.getApprovedSubjects(), new UserSubjectCheckedAdapter.OnItemClickListener() {
            @Override public void onItemClick(Subject item) {
                Toast.makeText(ApprovedSubjectsActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                item.changeState();
            }
        }));*/

//        usAdapter.notifyDataSetChanged();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new SingerAdapter(getApplicationContext(), user.getApprovedSubjects());

        // vertical RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}

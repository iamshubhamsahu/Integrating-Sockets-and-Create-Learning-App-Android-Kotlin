package com.example.integratingsocketsinandroidkotlin.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integratingsocketsinandroidkotlin.R;
import com.example.integratingsocketsinandroidkotlin.adapter.Member;
import com.example.integratingsocketsinandroidkotlin.adapter.videoAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowVideoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    videoAdapter adapter;

    DatabaseReference mbase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video2);

        // its reference
        mbase = FirebaseDatabase.getInstance().getReference("Video");

        recyclerView = findViewById(R.id.recyclerview_ShowVideo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Member> options = new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(mbase, Member.class)
                .build();

        adapter = new videoAdapter(options,this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
package com.example.seminarlp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.seminarlp.adapters.RecyclerViewAdapter;
import com.example.seminarlp.data.Question;
import com.example.seminarlp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HighScore extends AppCompatActivity {

    ArrayList<User> userArrayList = new ArrayList<User>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        recyclerView = findViewById(R.id.recyclerView);

        String json = getIntent().getStringExtra("highScores");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {}.getType();

        userArrayList = gson.fromJson(json, type);
        initRecyclerview();
    }


    void initRecyclerview(){
        RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(userArrayList);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
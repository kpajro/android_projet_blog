package com.example.projectandroid.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectandroid.Adapter.PostAdapter;
import com.example.projectandroid.R;
import com.example.projectandroid.ViewModel.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private PostViewModel postViewModel;
    private PostAdapter postAdapter;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAddPost = findViewById(R.id.btnAddPost);
        Button btnProfileEdit = findViewById(R.id.btnEditProfile);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        postViewModel.getPosts().observe(this, posts -> {
            postAdapter.updateData(posts);
        });

        btnProfileEdit.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, ProfileEditActivity.class);
            startActivity(intent);
        });

        btnAddPost.setOnClickListener(v ->{
            Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
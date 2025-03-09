package com.example.projectandroid.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectandroid.Model.Post;
import com.example.projectandroid.Model.User;
import com.example.projectandroid.R;
import com.example.projectandroid.ViewModel.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity {
    private EditText editTextContent;
    private Button btnSubmitPost;

    private DatabaseReference dbRef;
    public FirebaseUser uid;


    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        editTextContent = findViewById(R.id.editTextContent);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        dbRef = FirebaseDatabase.getInstance().getReference("posts");
        uid = FirebaseAuth.getInstance().getCurrentUser();

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        btnSubmitPost.setOnClickListener(view -> {
            String content = editTextContent.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                addPost(content);
            } else {
                Toast.makeText(AddPostActivity.this, "Veuillez entrer un message", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void addPost(String content) {
        User user = new User(uid.getDisplayName(), uid.getUid());
        Post post = new Post(content, user);

        postViewModel.addPost(post);
    }
}
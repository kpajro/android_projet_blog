package com.example.projectandroid.Repository;

import android.util.Log;

import com.example.projectandroid.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostRepository {
    private DatabaseReference dbRef;
    private FirebaseDatabase mAuth;

    private FirebaseUser uid;

    public PostRepository() {
        mAuth = FirebaseDatabase.getInstance("https://fbcalcul-30ff8-default-rtdb.europe-west1.firebasedatabase.app");
        dbRef = mAuth.getReference("db");
        uid = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void savePost(Post post){
        if (uid != null) {
            dbRef.child("posts")
                    .push()
                    .setValue(post)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "sauvegarde faite"))
                    .addOnFailureListener(e -> Log.e("Firebase", "sauvegarde echoué"));
        } else {
            Log.e("Firebase", "utilisateur non connecté");
            System.out.println("save posts");
        }
    }

    public void fetchPosts(ValueEventListener listener){
        dbRef.child("posts")
                .addValueEventListener(listener);
    }

}
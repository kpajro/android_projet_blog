package com.example.projectandroid.ViewModel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.projectandroid.Model.Post;
import com.example.projectandroid.Repository.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private final PostRepository postRepository;

    public PostViewModel() {
        postRepository = new PostRepository();
        loadPosts();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        List<Post> currentPosts = posts.getValue();
        if (currentPosts != null) {
            currentPosts.add(post);
            posts.setValue(currentPosts);
        }
        postRepository.savePost(post);
    }

    // charge les posts depuis la bdd Firebase
    public void loadPosts() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            postRepository.fetchPosts(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Post> postList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);
                        if (post != null) {
                            postList.add(post);
                        }
                    }
                    posts.setValue(postList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "erreur lors du chargement des posts", error.toException());
                }
            });
        }
    }

    // met à jour les posts de l'utilisateur avec un nouveau nom d'utilisateur
    public void updateUserPosts(String userId, String newUsername) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("db/posts");

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DataSnapshot userSnapshot = postSnapshot.child("user");
                    String postUserId = userSnapshot.child("userId").getValue(String.class);

                    if (postUserId != null && postUserId.equals(userId)) {
                        postSnapshot.getRef().child("user/username").setValue(newUsername);
                    }
                }
                Log.d("Firebase", "les posts des users mise à jour");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "mise à jours des posts echoué: " + error.getMessage());
            }
        });
    }
}
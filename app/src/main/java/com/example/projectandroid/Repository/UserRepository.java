package com.example.projectandroid.Repository;

import android.util.Log;

import com.example.projectandroid.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private DatabaseReference dbRef;
    private FirebaseDatabase mAuth;
    private FirebaseUser uid;

    public UserRepository() {
        mAuth = FirebaseDatabase.getInstance("https://fbcalcul-30ff8-default-rtdb.europe-west1.firebasedatabase.app");
        dbRef = mAuth.getReference("users");
        uid = FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirebaseUser getFirebaseAuth(){
        return uid;
    }

    public void setFirebaseAuth(FirebaseUser user){
        uid = user;
    }

    public interface OnUserSavedListener {
        void onUserSaved();
    }

    public void saveUser(User user, OnUserSavedListener listener){
        if (uid != null) {
            dbRef.child(uid.getUid())
                    .setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "sauvegarde faite pour le user");
                        if (listener != null){
                            listener.onUserSaved();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "sauvegarde echoué"));
        } else {
            Log.e("Firebase", "utilisateur non connecté");
            System.out.println("save users");
        }
    }

}

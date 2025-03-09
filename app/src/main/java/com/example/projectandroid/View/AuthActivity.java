package com.example.projectandroid.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectandroid.Model.User;
import com.example.projectandroid.R;
import com.example.projectandroid.Repository.UserRepository;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        System.out.println(userRepository.getFirebaseAuth());
        System.out.println(FirebaseAuth.getInstance().getCurrentUser());
        startAuthFlow();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // gere l'activité de connexion
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid() + " this one!");
                    checkFirstLogin();
                } else {
                    IdpResponse response = IdpResponse.fromResultIntent(result.getData());
                    if (response == null) {
                        finish();
                    }
                }
            }
    );

    // flux d'authentification avec Firebase UI
    private void startAuthFlow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_ProjectAndroid)
                .setIsSmartLockEnabled(false)
                .build();

        signInLauncher.launch(intent);
    }

    // vérifie si c'est la première connexion de l'utilisateur
    private void checkFirstLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRepository.setFirebaseAuth(user);
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid())
                    .child("username");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()){
                        askForUsername();
                    } else {
                        goToHome();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Erreur de récupération du username", error.toException());
                }
            });
        }
    }

    // demande à l'utilisateur de choisir un nom username
    private void askForUsername() {
        EditText input = new EditText(this);
        input.setHint("Entrez un username");

        new AlertDialog.Builder(this)
                .setTitle("Bienvenue!")
                .setMessage("Choisissez un username pour votre compte")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) ->{
                    String username = input.getText().toString().trim();
                    if (!username.isEmpty()){
                        saveUsername(username);
                    } else {
                        askForUsername();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void saveUsername(String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            User userObj = new User(user.getDisplayName());
            userRepository.saveUser(userObj, this::goToHome);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates);
        }
    }

    public void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
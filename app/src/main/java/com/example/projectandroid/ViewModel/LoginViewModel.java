package com.example.projectandroid.ViewModel;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    public void goToAuthenticationScreen(LoginNavigator navigator) {
        navigator.navigateToAuth();
    }

    public interface LoginNavigator {
        void navigateToAuth();
    }
}

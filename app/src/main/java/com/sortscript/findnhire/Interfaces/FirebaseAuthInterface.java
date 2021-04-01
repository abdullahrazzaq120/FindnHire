package com.sortscript.findnhire.Interfaces;

public interface FirebaseAuthInterface {
    void signIn(String emailSignIn, String passwordSignIn);

    void signUp(String uNameSignUp, String emailSignUp, String passwordSignUp, String addressSignUp,
                String dobSignUp, String genderSignUp);
}

package com.example.onlinequizapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginUnitTest {

    private FirebaseAuth mAuth;

    @Before
    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testLogin() {
        String email = "testuser@example.com";
        String password = "password";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Assert.assertNotNull(user);
                        Assert.assertEquals(user.getEmail(), email);
                    } else {
                        Assert.fail();
                    }
                });
    }
}
package com.example.project_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String email = user.getEmail();

        TextView welcomeText =  findViewById(R.id.welcomeText);
        welcomeText.setText("Ingresaste : " + email);

        Button logoutButton =  findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });


    }
    public void escribirBDD(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://androidfirebase-2d884-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("nuevos2/message");
        myRef.setValue("I'm IronMan");
    }
}
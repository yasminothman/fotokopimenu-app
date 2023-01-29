package com.example.fotokopimenuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutPage extends AppCompatActivity {

    Button github;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        github = findViewById(R.id.github);

        github.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goLink("https://github.com/yasminothman/fotokopimenu-app.git");
            }
        });
    }

    private void goLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}
package com.example.fotokopimenuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button admin, customer, aboutPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin= (Button) findViewById(R.id.adminButton);
        customer = (Button) findViewById(R.id.custButton);
        aboutPage = (Button) findViewById(R.id.buttonAbout);

        aboutPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutPage.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, loginPage.class));

            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuList.class));
            }
        });


    }

    public void admin(View view) {
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}
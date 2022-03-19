package com.example.calculatordb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLinear_eqilibrium(View view) {
        Intent i = new Intent(MainActivity.this, Linear_eqilibrium.class);
        startActivity(i);
    }

    public void onClickSimplify(View view) {
        Intent i = new Intent(MainActivity.this, Simplify.class);
        startActivity(i);
    }
}
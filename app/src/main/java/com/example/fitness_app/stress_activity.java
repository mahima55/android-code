package com.example.fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class stress_activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_activity);
        CardView yoga_data = (CardView) findViewById(R.id.yoga_data);


        yoga_data.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        Intent i;

        switch (v.getId()){
            case R.id.yoga_data :
                i = new Intent(this,YogaInfo.class);
                startActivity(i);
                break;


        }
    }

}
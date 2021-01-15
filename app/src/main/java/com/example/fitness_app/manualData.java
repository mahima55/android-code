package com.example.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class manualData extends AppCompatActivity {
   EditText bloodglucose,bloodpressure,oxylevel,heartrate,resplevel;
   FirebaseDatabase rootNode;
   DatabaseReference reference;
   Button save_button;
   Button show_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data);

        bloodglucose = findViewById(R.id.text1);
        bloodpressure = findViewById(R.id.text2);
        oxylevel = findViewById(R.id.text3);
        heartrate = findViewById(R.id.text4);
        resplevel = findViewById(R.id.text5);
        save_button = findViewById(R.id.save_data);
        show_data = findViewById(R.id.show_data);
        show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitynext();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("null");

                String bg = bloodglucose.getText().toString();
                String bp = bloodpressure.getText().toString();
                String ol = oxylevel.getText().toString();
                String hr = heartrate.getText().toString();
                String rl = resplevel.getText().toString();


                UserHelperClass helperClass =new UserHelperClass();
               reference.child(String.valueOf(oxylevel)).setValue(helperClass);
            }
        });



    }

    public void openActivitynext(){
        Intent intent = new Intent(this,ViewManual.class);
        startActivity(intent);
    }
}

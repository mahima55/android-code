package com.example.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Screen_1 extends AppCompatActivity implements View.OnClickListener {
    private TextView mtextresult;
    private RequestQueue mQueue;
    private DatabaseReference databaseReference;
    private byte encryptionKey[] = {5,115,51,86,105,4,-31,-23,-60,88,17,20,3,-105,119,-53};
    private Cipher cipher,decipher;
    private SecretKeySpec secretKeySpec;
    private String datatype,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_1);
        CardView stress_module = (CardView) findViewById(R.id.stress_module);
        CardView manual_data = (CardView) findViewById(R.id.manual_data);
        mtextresult = findViewById(R.id.text_result);
        Button track_button = findViewById(R.id.track_button);
        Button save_button = findViewById(R.id.save_button);

        mQueue = Volley.newRequestQueue(this);


        track_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }

        });

        databaseReference = FirebaseDatabase.getInstance().getReference("null");

        try {
            cipher = Cipher.getInstance("AES");
            decipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        secretKeySpec = new SecretKeySpec(encryptionKey,"AES");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datatype = snapshot.getValue().toString();
                value = snapshot.getValue().toString();

                String[] msg1Array = datatype.split(",");
                String[] msg2Array = value.split(",");

                String[] stringFinal1 = new String[msg1Array.length*2];
                String[] stringFinal2 = new String[msg2Array.length*2];

                for(int i=0;i<msg1Array.length;i++){
                    String[] stringKeyValue= msg1Array[i].split("",2);
                    stringFinal1[2*i] =(String) android.text.format.DateFormat.format("dd-MM-YYYY hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                    try {
                        stringFinal1[2*i+1] = AESDecryptionMethod(stringKeyValue[1]);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                
                for(int i=0;i<msg2Array.length;i++){
                    String[] stringKeyValue1= msg2Array[i].split("",2);
                    stringFinal2[2*i] =(String) android.text.format.DateFormat.format("dd-MM-YYYY hh:mm:ss", Long.parseLong(stringKeyValue1[0]));
                    try {
                        stringFinal2[2*i+1] = AESDecryptionMethod(stringKeyValue1[1]);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        stress_module.setOnClickListener(this);
        manual_data.setOnClickListener(this);
    }

    private void jsonParse() {
        String url = "http://localhost:8080/getDatasource";
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Datasource");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject datasource = jsonArray.getJSONObject(i);

                                 datatype = datasource.getString("dataType");
                                 value = datasource.getString("value");
                                mtextresult.append(datatype+" "+value+"\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v){
        Intent i;

        switch (v.getId()){
            case R.id.stress_module :
                i = new Intent(this,stress_activity.class);
                startActivity(i);
                break;

            case R.id.manual_data:
                i=new Intent(this,manualData.class);
                startActivity(i);
                break;

        }
    }


    public  void sendButton(View v1){
        Date date =new Date();

            databaseReference.child(String.valueOf(date.getTime())).setValue(AESEncryptionMethod(mtextresult.getText().toString()));

    }
    private String AESEncryptionMethod(String string)  {
        byte[] stringByte = string.getBytes();
        byte[] encryptedbyte = new byte[stringByte.length];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedbyte = cipher.doFinal(stringByte);
        }catch (InvalidKeyException e){
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        String returnString = null;
        try {
             returnString = new String(encryptedbyte , "150-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }
    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {

        byte[] EncryptedByte = string.getBytes("150-8859-1");

        String decryptedString = string;

        byte[] decryption;
        try {
            decipher.init(cipher.DECRYPT_MODE,secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString =new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}
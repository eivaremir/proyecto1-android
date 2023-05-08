package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    public static final String USER = "com.example.proyecto_n1.USER";
    public static final String ID = "com.example.proyecto_n1.ID";
    final String field = "name";
    String text;
    int jury_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.btn_login);
        EditText user = findViewById(R.id.username);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openMainActivity();
                text = user.getText().toString();
                DynamoClient.list(field, text, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // System.out.println(timeline);

                        JSONObject id = null;

                        if (response.length() != 0){
                            try {
                                id = response.getJSONObject(0);
                                String[] jurys_id = id.getString("PK").split("#");
                                jury_id = Integer.parseInt(jurys_id[1]);
                                System.out.println("Jury ID:" + jurys_id[1]);
                                //jury_id = id.getString("PK");
                                /*for (String a : jurys_id)
                                    System.out.println("Jury ID:" + a);*/
                                openMainActivity(jury_id);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        else {
                            System.out.println("Usuario Incorrecto");
                            Toast.makeText(Login.this, "Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                        }

                        //txt.setText(P.getString(field));
                        System.out.println(response);



                        //System.out.println(id.getString(field));

                    }
                });
            }
        });

    }

    public void openMainActivity(int id){
        EditText user = findViewById(R.id.username);
        String text = user.getText().toString();

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(USER,text);
        intent.putExtra(ID,id);
        startActivity(intent);
    }

}
package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    Button loginButton;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_login);
        EditText user = findViewById(R.id.username);

        progress= findViewById(R.id.progressLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSpinner(true);
                text = user.getText().toString();
                DynamoClient.list(field, text, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        JSONObject id = null;
                        if (response.length() != 0){
                            try {
                                id = response.getJSONObject(0);
                                String[] jurys_id = id.getString("PK").split("#");
                                jury_id = Integer.parseInt(jurys_id[1]);
                                System.out.println("Jury ID:" + jurys_id[1]);
                                openMainActivity(jury_id);

                            } catch (JSONException e) {
                                toggleSpinner(false);
                                throw new RuntimeException(e);

                            }
                        }
                        else {
                            toggleSpinner(false);
                            System.out.println("Usuario Incorrecto");
                            Toast.makeText(Login.this, "Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                        }
                        System.out.println(response);
                    }
                });
            }
        });
    }

    public void toggleSpinner(Boolean display){
        if (display){
            loginButton.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }
        else{
            loginButton.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
        }
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
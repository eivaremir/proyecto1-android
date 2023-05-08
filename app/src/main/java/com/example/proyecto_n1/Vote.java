package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Vote extends AppCompatActivity {
    private Integer proyeccion_pts = 0;
    private Integer contenido_pts = 0;
    private Integer lenguaje_pts = 0;

    private EditText proyeccion;
    private EditText contenido ;
    private EditText lenguaje ;

    private Integer completedFields = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ProgressBar progressBar = findViewById(R.id.progress);

        // set the maximum value of the progress bar to 100
        progressBar.setMax(3);
        progressBar.setProgress(0);
        Intent intent = getIntent();
        Integer participant = intent.getIntExtra(MainActivity.PARTICIPANT,0);
        String participantName = intent.getStringExtra(MainActivity.PARTICIPANT_NAME);

        ((TextView)findViewById(R.id.vote_for_title)).setText("Votar por "+participantName);

        proyeccion = findViewById(R.id.proyeccion);
        contenido = findViewById(R.id.contenido);
        lenguaje = findViewById(R.id.lenguaje);

        TextWatcher tw = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                completedFields+=1;
                progressBar.setProgress(completedFields);
                System.out.println(s);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        };
        proyeccion.addTextChangedListener(tw);
        contenido.addTextChangedListener(tw);
        lenguaje.addTextChangedListener(tw);

        Button voteBtn = findViewById(R.id.vote);
        ProgressBar voting = findViewById(R.id.voting);
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteBtn.setVisibility(View.INVISIBLE);
                voting.setVisibility(View.VISIBLE);
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
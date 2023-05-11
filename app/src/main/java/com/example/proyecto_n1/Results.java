package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.*;

import cz.msebera.android.httpclient.Header;

import java.util.Arrays;
import java.util.Comparator;

public class Results extends AppCompatActivity {

    public int cantidad_parti;
    TextView pos1, pos2, pos3, pts1, pts2, pts3;
    TextView empate;

    String [][] ganadores =  new String[3][2];

    int[][] pts;
    JSONArray parts;


    /* ------------------------------------------------------------
    TextView textView = findViewById(R.id.myTextView);
    String variable1 = "Hello";
    int variable2 = 123;
    String text = variable1 + " " + variable2;
    textView.setText(text);
    ------------------------------------------------------------ */

    JsonHttpResponseHandler participantsRequestHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            System.out.println("RESPONSE(RESULTS)(RESULTS) " + response);
            //cantidad_parti = response.length();
            parts = response;

            //System.out.println("CANTIDAD DE PARTICIPANTES(RESULTS)" + cantidad_parti);
        //setFinish();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //resp1 = findViewById(R.id.resp1);

        pos1 = findViewById(R.id.pos1);
        pos2 = findViewById(R.id.pos2);
        pos3 = findViewById(R.id.pos3);

        pts1 = findViewById(R.id.pts1);
        pts2 = findViewById(R.id.pts2);
        pts3 = findViewById(R.id.pts3);

        LinearLayout layout = findViewById(R.id.empates);

        //empate = findViewById(R.id.empate);



        DynamoClient.list("PK", "PARTICIPANT",participantsRequestHandler);



        DynamoClient.list("PK", "VOTACION", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                pts = ResultsTableArray.computeResults(response,parts,null);
                /*System.out.println("ARREGLO DE PTS: ");
                for (int i = 0; i < pts.length; i++) { //part_puntaje.length
                    for (int j = 0; j < pts[1].length; j++) {
                        System.out.print(pts[i][j] + "\t\t");
                    }
                    System.out.println();
                }*/

                int n = 0;
                int [] repeat_part = new int[3];
                for(int i = 1; i < pts.length; i++){
                    if(pts[i][5] == pts[i-1][5]){
                        repeat_part[n] += 1;
                    }
                    else {
                        n+= 1;
                    }
                }

                for(int i = 0; i < repeat_part.length; i++){
                    System.out.println("REPETIR " + repeat_part[i]);
                }

                DynamoClient.list("PK", "PARTICIPANT", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        String [][] name_pts =  new String[parts.length()][2];
                        try {
                            for (int i = 0; i < parts.length(); i++){
                                JSONObject participant = (JSONObject) response.get(i);
                                //System.out.println("PARTICIPANT: " + participant);
                                name_pts[i][0] = (participant.getString("PK")).split("#")[1];
                                //System.out.println("ID: " + name_pts[i][0]);
                                name_pts[i][1] = participant.getString("name");
                                //System.out.println("NAME: " + name_pts[i][1]);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        System.out.println("ARREGLO PARTICIPANTES: ");
                        System.out.println("ID\t\tNAME");
                        for (int i = 0; i < name_pts.length; i++) {
                            for (int j = 0; j < name_pts[0].length; j++) {
                                System.out.print(name_pts[i][j] + " ");
                            }
                            System.out.println();
                        }

                        //System.out.println("PTS.LENGTH: "+ pts.length);
                        int ind = pts.length;

                        String [][] primeros_puestos =  new String[ind][4];
                        for (int i = 0; i < ind; i++){
                            for (int j = 0; j < ind; j++){
                                if((pts[i][0] == Integer.parseInt(name_pts[j][0]))){ //&& (pts[i][4] > 0)
                                    primeros_puestos[i][0] = name_pts[j][0];
                                    primeros_puestos[i][1] = name_pts[j][1];
                                    primeros_puestos[i][2] = Integer.toString(pts[i][5]);
                                    primeros_puestos[i][3] = Integer.toString(pts[i][4]);
                                    /*
                                    int myInt = 123;
                                    String myString = Integer.toString(myInt);
                                    */
                                }
                            }
                        }

                        System.out.println("PRIMEROS_PUESTOS: ");
                        System.out.println("ID\t\tNAME");
                        for (int i = 0; i < primeros_puestos.length; i++) {
                            for (int j = 0; j < primeros_puestos[0].length; j++) {
                                System.out.print(primeros_puestos[i][j] + " ");
                            }
                            System.out.println();
                        }



                        int pos;
                        String txt, tempo;
                        int n = 0, h = 0;
                        /*
                        if (repeat_part.length != 0) {
                        for
                        else
                        ganadores = primeros_puestos
                        }
                        */
                        for( int i = 0; i < repeat_part.length; i++){
                            pos = repeat_part[i] + 1;
                            txt = "";
                            tempo = "";
                            int lugar = 0;
                            h = 0;
                            //System.out.println("POS: " + pos);
                            System.out.println("TEMPO: " + tempo);
                            for(int j = 0; j < pos; j++){
                                tempo = primeros_puestos[n][1];
                                txt += j == pos-1? tempo : tempo + ", ";
                                System.out.println("IF CONCATENATED: " + txt);
                                System.out.println("TEMPO: " + primeros_puestos[i][3]);
                                lugar = Integer.parseInt(primeros_puestos[n][2]);
                                n +=  1;
                                h += 1;
                            }
                            ganadores[i][0] = txt;
                            ganadores[i][1] = primeros_puestos[i][3];

                            if (h > 1) {
                                TextView textView = new TextView(Results.this);
                                textView.setText("\nHa habido un empate en la posición "  + lugar);
                                layout.addView(textView); // add the TextView to the LinearLayout
                                textView.setTextSize(20);
                            }

                        }


                        /*----------------------------------SEGUIR CODE AQUÍ----------------------------------*/
                        //TEXT VIEWS

                        /*
                        EditText pos1, pos2, pos3, pts1, pts2, pts3;
                        TextView empate;

                        */

                        //System.out.println("ganadores[0][0]" + ganadores[0][0]);
                        pos1.setText(ganadores[0][0]);
                        pos2.setText(ganadores[1][0]);
                        pos3.setText(ganadores[2][0]);

                        pts1.setText("Obtuvo " + ganadores[0][1] + " puntos");
                        pts2.setText("Obtuvo " + ganadores[1][1] + " puntos");
                        pts3.setText("Obtuvo " + ganadores[2][1] + " puntos");


                        /*if (repeat_part.length == 0) {
                            empate.setText("Ha habido un empate");
                        }

                        System.out.println("REPEAT.LENGTH: " + repeat_part.length);*/


                    }
                });

                /*System.out.println("GANADORES[][]");
                for (int i = 0; i < ganadores.length; i++) {
                    for (int j = 0; j < ganadores[i].length; j++) {
                        System.out.print(ganadores[i][j] + " ");
                    }
                    System.out.println();
                }*/
            }
        });

        ((Button) findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                openLoginActivity();
            }
        });

        ((Button) findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Toast.makeText(this, "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
    }

}


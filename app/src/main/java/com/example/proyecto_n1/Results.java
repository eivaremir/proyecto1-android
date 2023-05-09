package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.*;

import cz.msebera.android.httpclient.Header;

import java.util.Arrays;
import java.util.Comparator;

public class Results extends AppCompatActivity {

    public int cantidad_parti;

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

        DynamoClient.list("PK", "PARTICIPANT",participantsRequestHandler);



        DynamoClient.list("PK", "VOTACION", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                pts = ResultsTableArray.computeResults(response,parts,null);
                for (int i = 0; i < pts.length; i++) { //part_puntaje.length
                    for (int j = 0; j < pts[1].length; j++) {
                        System.out.print(pts[i][j] + "\t\t");
                    }
                    System.out.println();
                }

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

                /*for(int i = 0; i < repeat_part.length; i++){
                    System.out.println("REPETIR " + repeat_part[i]);
                }*/

                DynamoClient.list("PK", "PARTICIPANT", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        String [][] name_pts =  new String[parts.length()][2];
                        try {
                            for (int i = 0; i < parts.length(); i++){
                                JSONObject participant = (JSONObject) response.get(i);
                                System.out.println("PARTICIPANT: " + participant);
                                name_pts[i][0] = (participant.getString("PK")).split("#")[1];
                                System.out.println("NAME: " + name_pts[i][0]);
                                name_pts[i][1] = participant.getString("name");
                                System.out.println("NAME: " + name_pts[i][1]);
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

                        /*if (repeat_part.length != 0){

                        }*/

                        String [][] primeros_puestos =  new String[ind][4];
                        for (int i = 0; i < ind; i++){
                            for (int j = 0; j < ind; j++){
                                if(Integer.parseInt(name_pts[i][0]) == pts[j][0]){
                                    primeros_puestos[i][0] = name_pts[i][0];
                                    primeros_puestos[i][1] = name_pts[i][1];
                                    primeros_puestos[i][2] = Integer.toString(pts[j][5]);
                                    primeros_puestos[i][3] = Integer.toString(pts[j][4]);
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

                        /*----------------------------------SEGUIR CODE AQUÍ----------------------------------*/

                        String [][] ganadores =  new String[3][2];
                        int pos;
                        String txt, tempo;
                        /*
                        if (repeat_part.length != 0) {
                        for
                        else
                        ganadores = primeros_puestos
                        }
                        */
                        for( int i = 0; i < repeat_part.length; i++){
                            pos = repeat_part.length;
                            System.out.println("POS: " + pos);
                            txt = null;
                            for( int j = 0; j < pos; j++){
                                tempo = primeros_puestos[i+j][1];
                                txt += j == pos-1? tempo : tempo + ", ";
                                System.out.println("IF CONCATENATED: " + txt);
                            }
                            //ganadores[i][0] = txt;
                            //ganadores[i][1] = primeros_puestos[i][2];
                        }


                    }
                });
            }
        });

        ((Button) findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
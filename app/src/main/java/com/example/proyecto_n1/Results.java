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
    public int id_participante, proyeccion, lenguaje, contenido, puntos;

    int[][]part_puntaje = new int[3][2];
    int[][]ganadores = new int[3][2];

    int[][] pts ;
    String[] names;
    JSONArray parts ;
    public class ParticipantsResponseHandler extends JsonHttpResponseHandler {
        public ParticipantsResponseHandler() {
            super();
            System.out.println("init Http handler ");
            finish = false;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            System.out.println("RESPONSE(RESULTS)" + response);
            cantidad_parti = response.length();
            parts = response;
            System.out.println("CANTIDAD DE PARTICIPANTES(RESULTS)" + cantidad_parti);
            setFinish();
        }
        Boolean finish ;
        public synchronized void setFinish(){
            finish = true;
        }

        public synchronized boolean isFinished(){
            return finish;
        }
    }
    JsonHttpResponseHandler participantsRequestHandler =new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            System.out.println("RESPONSE(RESULTS)" + response);
            cantidad_parti = response.length();
            parts = response;
            System.out.println("CANTIDAD DE PARTICIPANTES(RESULTS)" + cantidad_parti);
        //    setFinish();
        }


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        DynamoClient.list("PK", "PARTICIPANT",participantsRequestHandler);


        //cantidad_parti = CantidadParticipantes();
        //System.out.println("CANTIDAD DE PARTICIPANTES (RESULTS)" + cantidad_parti);

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

            }
        });

        ((Button) findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*public int CantidadParticipantes() {
        int[] amount = new int[1];
        DynamoClient.list("PK", "PARTICIPANT", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("RESPONSE (FUNCION) (RESULTS)" + response);
                amount[0] = response.length();
                System.out.println("CANTIDAD DE PARTICIPANTES (FUNCION) (RESULTS)" + amount[0]);
            }
        });
        return amount[0];
    }*/

}
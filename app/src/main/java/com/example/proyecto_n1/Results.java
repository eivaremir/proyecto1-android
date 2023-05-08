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
                pts = computeResults(response);
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
    public int[][] computeResults(JSONArray response){
        System.out.println("RESPONSE" + response);
        JSONObject p = null;
        int[][] pts ;

        try {
            pts = new int[ parts.length()][6];

            // por cada participante
            for(int i =0; i< parts.length();i++) {
                Integer pk = Integer.parseInt((String) parts.getJSONObject(i).getString("PK").split("#")[1]);

                // pts i es un participante
                // pts 0 es el id, 1 es el puntaje sumado y 2 es la posicion del finalista
                pts[i][0]=pk; // id del participante
                System.out.println(pk);

                // por cada votacion
                for(int v  =0;v<response.length();v++){
                    JSONObject votacion = response.getJSONObject(v);
                    Integer id_participant = Integer.parseInt(votacion.getString("SK").split("#")[3]);

                    proyeccion = (Integer.parseInt(votacion.getString("proyeccion")));
                    lenguaje = (Integer.parseInt(votacion.getString("lenguaje")));
                    contenido = (Integer.parseInt(votacion.getString("contenido")));

                    // si la votacion v corresponde al participante i
                    if (id_participant == pk){
                        System.out.println("Participant "+id_participant);
                        pts[i][1]+= (proyeccion);
                        pts[i][2]+= (lenguaje);
                        pts[i][3]+= (contenido);
                        pts[i][4]+= (proyeccion+lenguaje+contenido);
                    }
                }
            }
            System.out.println("Column 1\tColumn 2");

            Arrays.sort(pts, new Comparator<int[]>() {
                @Override
                public int compare(int[] a, int[] b) {
                    return Integer.compare(b[1], a[1]);
                }
            });



            Integer max_index = 0;
            Integer posicion = 1;
            pts[0][5]=posicion;
            for(int i=1; i<pts.length;i++){
                int current = pts[i][1];
                int previous = pts[i-1][1];

                if (current==previous){
                    max_index +=1;

                }
                else{
                    max_index +=1;
                    posicion+=1;
                }
                pts[i][5] = posicion;
            }
            for (int i = 0; i < pts.length; i++) { //part_puntaje.length
                for (int j = 0; j < pts[1].length; j++) {
                    System.out.print(pts[i][j] + "\t\t");
                }
                System.out.println();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return pts;
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
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        DynamoClient.list("PK", "PARTICIPANT", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("RESPONSE(RESULTS)" + response);
                cantidad_parti = response.length();
                parts = response;
                System.out.println("CANTIDAD DE PARTICIPANTES(RESULTS)" + cantidad_parti);
            }
        });

        //cantidad_parti = CantidadParticipantes();
        //System.out.println("CANTIDAD DE PARTICIPANTES (RESULTS)" + cantidad_parti);

        DynamoClient.list("PK", "VOTACION", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("RESPONSE" + response);
                JSONObject p = null;


                    try {
                        pts = new int[ parts.length()][3];

                        for(int i =0; i< parts.length();i++) {
                            Integer pk = Integer.parseInt((String) parts.getJSONObject(i).getString("PK").split("#")[1]);

                            // pts i es un participante
                            // pts 0 es el id, 1 es el puntaje sumado y 2 es la posicion del finalista
                            pts[i][0]=pk; // id del participante
                            System.out.println(pk);
                            for(int v  =0;v<response.length();v++){
                                Integer id_participant = Integer.parseInt( response.getJSONObject(v).getString("SK").split("#")[3]);
                                if (id_participant == pk){
                                    System.out.println("Participant "+id_participant);

                                }
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                try {
                    for (int i = 0; i < response.length(); i++) {
                        p = response.getJSONObject(i);
                        //System.out.println("RESPONSE LENGTH() " + response.length());
                        System.out.println("RESPONSE OBJECT " + p);
                        id_participante = (Integer.parseInt(p.getString("SK").split("#")[3]));
                        System.out.println("ID PARTICIPANTE " + id_participante);
                        proyeccion = (Integer.parseInt(p.getString("proyeccion")));
                        lenguaje = (Integer.parseInt(p.getString("lenguaje")));
                        contenido = (Integer.parseInt(p.getString("contenido")));
                        puntos = proyeccion + lenguaje + contenido;
                        System.out.println("PUNTOS " + puntos);
                        for (int j = 0; j < cantidad_parti; j++) {
                            //for (int j = 0; j < cantidad_parti; j++) {

                            //String txt = p.getString("PK");
                            if (part_puntaje[j][0]  == id_participante) {
                                part_puntaje[j][1]  = part_puntaje[j][1]  + puntos;
                                System.out.println("PUNTOS AGREGADOS A PARTICIPANTE EXIXTENTE " + puntos);
                                System.out.println("PUNTOS AGREGADOS A PARTICIPANTE EXIXTENTE (TOTAL) " + part_puntaje[j][1]);
                                break;
                            } else if (part_puntaje[j][0]== 0) {
                                part_puntaje[j][0] = id_participante;
                                part_puntaje[j][1] = puntos;
                                System.out.println("NUEVO PARTICIPANTE EN ARREGLO " + puntos);
                                break;
                            }

                        }
                    }

                    Arrays.sort(part_puntaje, new Comparator<int[]>() {
                        @Override
                        public int compare(int[] a, int[] b) {
                            return Integer.compare(b[0], a[0]);
                        }
                    });

                    /*for (int i = 0; i < part_puntaje.length; i++) {
                        for (int j = 0; j < part_puntaje[i].length; j++) {
                            ganadores[i][j] = part_puntaje[i][j];
                        }
                    }

                    for (int i = 0; i < ganadores.length; i++) {
                        for (int j = 0; j < ganadores[i].length; j++) {
                            System.out.print(ganadores[i][j] + " ");
                        }
                        System.out.println();
                    }*/

                    System.out.println("Column 1\tColumn 2");
                    for (int i = 0; i < cantidad_parti; i++) { //part_puntaje.length
                        for (int j = 0; j < 2; j++) {
                            System.out.print(part_puntaje[i][j] + "\t\t");
                        }
                        System.out.println();
                    }

            } catch (JSONException e) {
                throw new RuntimeException(e);
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
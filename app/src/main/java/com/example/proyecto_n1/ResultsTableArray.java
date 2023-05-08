package com.example.proyecto_n1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Comparator;

public class ResultsTableArray {
    public static int[][] computeResults(JSONArray response,JSONArray parts, Integer jury_id ){
        System.out.println("RESPONSE" + response);
        JSONObject p = null;
        int[][] pts ;
        int id_participante, proyeccion, lenguaje, contenido, puntos;
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
                    Integer id_jury = Integer.parseInt(votacion.getString("SK").split("#")[1]);

                    proyeccion = (Integer.parseInt(votacion.getString("proyeccion")));
                    lenguaje = (Integer.parseInt(votacion.getString("lenguaje")));
                    contenido = (Integer.parseInt(votacion.getString("contenido")));

                    // si la votacion v corresponde al participante i

                    if (((jury_id!=null) && (jury_id==id_jury) && (id_participant == pk) )|| ((jury_id==null)  && (id_participant == pk))){
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

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return pts;
    }
}

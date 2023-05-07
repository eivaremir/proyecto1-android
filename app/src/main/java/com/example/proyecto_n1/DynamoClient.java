package com.example.proyecto_n1;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DynamoClient {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static JSONArray res;
    public static void put(int J, int P, int proyeccion, int lenguaje, int contenido, AsyncHttpResponseHandler responseHandler) {
        String url = String.format("https://bsomlyl7kivajbpbxyt6i7aiku0zkjqp.lambda-url.us-east-1.on.aws/?jurado=%i&participant=%i&proyeccion=%i&lenguaje=%i&contenido=%i", J, P, proyeccion, lenguaje, contenido);
        client.get(url, responseHandler);
    }

    public static void list(String field, String value ,  AsyncHttpResponseHandler responseHandler){
        //final JSONArray res = new JSONArray();
        String url = String.format("https://o2bmssj4wi2c7yegtqe3cdrmii0spsqy.lambda-url.us-east-1.on.aws/?attr=%s&value=%s", field, value);
        client.get(url,responseHandler);


    }
}

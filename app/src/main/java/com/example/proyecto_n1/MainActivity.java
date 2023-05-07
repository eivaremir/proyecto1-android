package com.example.proyecto_n1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.loopj.android.http.*;
import org.json.*;
import cz.msebera.android.httpclient.Header;



public class MainActivity extends AppCompatActivity {
    private TextView txt;
    private static AsyncHttpClient client1 = new AsyncHttpClient();
    private static AsyncHttpClient client2 = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);

        /*client1.get("https://bsomlyl7kivajbpbxyt6i7aiku0zkjqp.lambda-url.us-east-1.on.aws/", new JsonHttpResponseHandler() { //proyecto-android-put

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);

            }

        });*/

        client2.get("https://o2bmssj4wi2c7yegtqe3cdrmii0spsqy.lambda-url.us-east-1.on.aws/?attr=PK&value=PARTICIPANT",new JsonHttpResponseHandler() { //proyecto-android-list
            @Override
            public void onStart() {
                // called before request is started
                System.out.println("HOLA2");

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // System.out.println(timeline);

                JSONObject firstEvent = null;
                try {
                    firstEvent = timeline.getJSONObject(0);
                    txt.setText(firstEvent.getString("PK"));
                    System.out.println(firstEvent.getString("PK"));


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            //@Override
            public void onFailure(int statusCode, Header[] headers,JSONObject errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR");
            }



        });
        System.out.println("HOLA");
    }

}
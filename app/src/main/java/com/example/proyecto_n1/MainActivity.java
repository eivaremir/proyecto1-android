package com.example.proyecto_n1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.*;
import org.json.*;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private TextView txt;
    public static final String PARTICIPANT = "com.example.proyecto_n1.PARTICIPANT";
    public static final String PARTICIPANT_NAME = "com.example.proyecto_n1.PARTICIPANT_NAME";
    public static final String ID_JURY = "com.example.proyecto_n1.ID_JURY";
    final String field = "PK";
    final String value = "PARTICIPANT";

    public String[] participants = {"A","B"};
    public EditText[] pButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String user = intent.getStringExtra(Login.USER);
        int jury_id = intent.getIntExtra(Login.ID,0);
        TextView jury_name = findViewById(R.id.jury_username);


        // https://stackoverflow.com/questions/6661261/adding-content-to-a-linear-layout-dynamically
        // LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
        //LinearLayout a = new LinearLayout(this);
        //a.setOrientation(LinearLayout.HORIZONTAL);
        //a.addView(view1);
        //a.addView(view2);
        //a.addView(view3);
        //myRoot.addView(a);
        LinearLayout pcontainer = (LinearLayout) findViewById(R.id.participants_container);

        for(int i = 0; i<participants.length;i++){
            TextView tv = new TextView(this);
            tv.setText(participants[i]);
            tv.setTag(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //System.out.println("clicked %i"+ (int )view.getTag());
                    openVoteActivity((int)view.getTag(), jury_id);
                }
            });
            pcontainer.addView(tv);
        }

        //DynamoClient api = new DynamoClient();

        DynamoClient.list(field, value, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // System.out.println(timeline);
                JSONObject P = null;

                try {
                    P = response.getJSONObject(0);

                    System.out.println(P.getString(field));

                    //txt.setText(P.getString("PK"));
                    System.out.println(P.getString("PK"));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        jury_name.setText(user);

        /*client1.get("https://bsomlyl7kivajbpbxyt6i7aiku0zkjqp.lambda-url.us-east-1.on.aws/", new JsonHttpResponseHandler() { //proyecto-android-put

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);

            }

        });*/

        /*client2.get("https://o2bmssj4wi2c7yegtqe3cdrmii0spsqy.lambda-url.us-east-1.on.aws/?attr=PK&value=PARTICIPANT",new JsonHttpResponseHandler() { //proyecto-android-list
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // System.out.println(timeline);

                JSONObject firstEvent = null;
                try {
                    firstEvent = timeline.getJSONObject(0);
                    //txt.setText(firstEvent.getString("PK"));
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
        });*/

        //System.out.println("ID JURADO: " + jury_id);
    }

    public void openVoteActivity(Integer participant, int jury_id){
        Intent intent = new Intent(this,Vote.class);
        intent.putExtra(PARTICIPANT,participant);
        intent.putExtra(PARTICIPANT_NAME,participants[participant]);
        intent.putExtra(ID_JURY ,jury_id);
        startActivity(intent);
    }

}



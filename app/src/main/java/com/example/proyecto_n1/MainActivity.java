package com.example.proyecto_n1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.*;
import org.json.*;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    //private TextView txt;
    public static final String PARTICIPANT = "com.example.proyecto_n1.PARTICIPANT";
    public static final String PARTICIPANT_NAME = "com.example.proyecto_n1.PARTICIPANT_NAME";
    public static final String ID_JURY = "com.example.proyecto_n1.ID_JURY";
    final String field = "PK";
    final String value = "PARTICIPANT";

    public JSONArray participants;
    public EditText[] pButtons;
    LinearLayout pcontainer;
    int jury_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String user = intent.getStringExtra(Login.USER);
         jury_id = intent.getIntExtra(Login.ID,0);
        TextView jury_name = findViewById(R.id.jury_username);

         pcontainer = (LinearLayout) findViewById(R.id.participants_container);

         jury_name.setText(user);
        fetchParticipants();
        ((Button) findViewById(R.id.results)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("clicked %i"+ (int )view.getTag());
                openResultsActivity();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK) {
            // refresh data in the parent activity here
            // check if i need to refresh data or not
            fetchParticipants();
        }
    }
    public void fetchParticipants(){
        DynamoClient.list(field, value, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                setParticipantList(response,jury_id);
            }
        });
    }
    public void setParticipantList(JSONArray participants,Integer jury_id){
        pcontainer.removeAllViews();
        for(int i = 0; i < participants.length(); i++){
            TextView tv = new TextView(this);
            try {
                JSONObject p =participants.getJSONObject(i);
                String txt = p.getString("name");
                tv.setText(p.getString("name"));
                tv.setTag(Integer.parseInt(p.getString("PK").split("#")[1]));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //System.out.println("clicked %i"+ (int )view.getTag());
                        openVoteActivity((int)view.getTag(), jury_id, txt);
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            pcontainer.addView(tv);
        }
    }
    public void openVoteActivity(Integer participant, int jury_id, String txt){
        Intent intent = new Intent(this,Vote.class);
        intent.putExtra(PARTICIPANT,participant);
        intent.putExtra(PARTICIPANT_NAME, txt);
        intent.putExtra(ID_JURY ,jury_id);
        startActivityForResult(intent,1);
    }
    public void openResultsActivity(){
        Intent intent = new Intent(this,Results.class);
        startActivity(intent);
    }

}



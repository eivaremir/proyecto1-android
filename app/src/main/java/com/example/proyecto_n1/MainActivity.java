package com.example.proyecto_n1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    TableLayout table ;
    TableRow[] rows;
    int jury_id;
    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String user = intent.getStringExtra(Login.USER);
         jury_id = intent.getIntExtra(Login.ID,0);
        TextView jury_name = findViewById(R.id.jury_username);
        Button refreshBtn = findViewById(R.id.refresh);
        spinner = findViewById(R.id.participantsProgress);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchParticipants();
            }
        });
         pcontainer = (LinearLayout) findViewById(R.id.participants_container);
         table = findViewById((R.id.table));
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
        spinner.setVisibility(View.VISIBLE);
        table.setVisibility(View.GONE);
        try {
            // Sleep for 5 seconds
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Handle the exception if necessary
        }
        DynamoClient.list(field, value, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray participants) {
                DynamoClient.list("PK", "VOTACION", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        int[][] pts = ResultsTableArray.computeResults(  response,participants,jury_id);

                        setParticipantList(participants,pts,jury_id);
                    }
                });


            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
                table.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public int getResultsIndex(int id,int [][] pts){
        int idx = 0 ;
        for (int p=0;p<pts.length;p++){
            if(pts[p][0]==id) {
                idx = p;
                break;
            }
        }
        return idx;
    }
    public void setParticipantList(JSONArray participants,int[][] pts, Integer jury_id){
        //pcontainer.removeAllViews();
        for (int i = 0; i < pts.length; i++) { //part_puntaje.length
            for (int j = 0; j < pts[1].length; j++) {
                System.out.print(pts[i][j] + "\t\t");
            }
            System.out.println();
        }
        if(rows!=null) {
            for (int r = 0; r < rows.length; r++) {
                table.removeView(rows[r]);
            }
        }
        rows = new TableRow[participants.length()];


        for(int i = 0; i < participants.length(); i++){

            TableRow row= new TableRow((this));
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            int[] result;
            Integer participant_id;
            Integer results_idx;
            try {
                JSONObject p =participants.getJSONObject(i);

                participant_id = Integer.parseInt( p.getString("PK").split("#")[1]);
                results_idx = getResultsIndex(participant_id,pts);
                result = pts[results_idx];

                String txt = p.getString("name");
                tv.setPadding(5,5,5,5);
                //tv.setBackground(Drawable.createFromPath("../res/drawable/table_border.xml"));
                tv.setText(p.getString("name"));
                tv.setTag(Integer.parseInt(p.getString("PK").split("#")[1]));

                if(result[4]==0) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //System.out.println("clicked %i"+ (int )view.getTag());
                            openVoteActivity((int) view.getTag(), jury_id, txt);
                        }
                    });
                }else{
                    // toast de error aqui "Ya has votado por el participante {name}
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            row.addView(tv);
            int sum = 0;
            for (int r = 1; r < result.length-1; r++) {
                TextView pt = new TextView(this);
                pt.setPadding(5,10,5,10);
                //tv.setBackground(Drawable.createFromPath("../res/drawable/table_border.xml"));
                pt.setText(((Integer)result[r]).toString());
                sum+=(Integer)result[r];
                pt.setGravity(Gravity.CENTER_HORIZONTAL);
                row.addView(pt);
            }

            table.addView((row));
            //pcontainer.addView(tabl);
            rows[i] = row;
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



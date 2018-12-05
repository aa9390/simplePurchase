package studyone.ksy.study;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MysqlTestActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_THING_NAME = "name";
    private static final String TAG_THING_COST = "cost";
    private static final String TAG_RESULTS = "result";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> thingsList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mysql_test );

        listView = findViewById( R.id.listView );
        thingsList = new ArrayList<>();
        getDataFromServer("http://10.131.160.65/phpConnection.php");

    }

    public void getDataFromServer(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String uri = strings[0];


                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder stringBuilder = new StringBuilder(  );

                    bufferedReader = new BufferedReader( new InputStreamReader( con.getInputStream() ) );

                    String json;
                    while((json = bufferedReader.readLine()) != null) {
                        stringBuilder.append( json + "\n" );
                    }

                    return stringBuilder.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                myJSON = s;
                showList();
                super.onPostExecute( s );
            }
        }

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute( url );
    }

    public void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_THING_NAME);
                String cost = c.getString(TAG_THING_COST);

                HashMap<String, String> thing = new HashMap<>();

                thing.put(TAG_THING_NAME, name);
                thing.put(TAG_THING_COST, cost);

                thingsList.add(thing);

                Log.d("@@@@@@@@@@@DB Size:",thingsList.size() + "" );
            }

            ListAdapter adapter = new SimpleAdapter(
                    this, thingsList, R.layout.item_list,
                    new String[]{TAG_THING_NAME, TAG_THING_COST},
                    new int[]{R.id.thingName, R.id.thingCost}
            );

            listView.setAdapter( adapter );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

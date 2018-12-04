package studyone.ksy.study;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_RESULTS = "result";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mysql_test );

        listView = findViewById( R.id.recyclerView );
        personList = new ArrayList<>();
        getDataFromServer("http://192.168.0.218/phpConnection.php");
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
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);

                HashMap<String, String> persons = new HashMap<>();

                persons.put(TAG_ID, id);
                persons.put(TAG_NAME, name);

                personList.add(persons);

//                Toast.makeText( getApplicationContext(), id, Toast.LENGTH_SHORT ).show();
            }

            ListAdapter adapter = new SimpleAdapter(
                    MysqlTestActivity.this, personList, R.layout.item_list,
                    new String[]{TAG_ID, TAG_NAME},
                    new int[]{R.id.idText, R.id.nameText}
            );

            listView.setAdapter( adapter );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package studyone.ksy.study;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import studyone.ksy.study.adapter.MyAdapter;
import studyone.ksy.study.model.Memo;
import studyone.ksy.study.model.Thing;

public class BuyThingsFragment extends Fragment {

    // 인증 객체
    private FirebaseAuth firebaseAuth;
    // 유저 객체
    private FirebaseUser firebaseUser;
    // 데이터베이스
    private static FirebaseDatabase firebaseDatabase;
    // 로컬 영속성
    static {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled( true );
    }

    String myJSON;

    private static final String TAG_THING_NAME = "name";
    private static final String TAG_THING_COST = "cost";
    private static final String TAG_RESULTS = "result";

    JSONArray peoples = null;
    ArrayList<HashMap<String, Integer>> thingsList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private NavigationView navigationView;
    private String selectedMemoKey;

    public BuyThingsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buythings, container, false);

        recyclerView = rootView.findViewById( R.id.recyclerView );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( rootView.getContext() );
        recyclerView.setLayoutManager( layoutManager );

        ArrayList<Thing> thingsList = new ArrayList<>(  );
        thingsList.add( new Thing( "A", 550 ) );
        thingsList.add( new Thing( "B", 1000 ) );
        thingsList.add( new Thing( "C", 1200 ) );
        thingsList.add( new Thing( "D", 1300 ) );
        thingsList.add( new Thing( "E", 1500 ) );
        thingsList.add( new Thing( "F", 1800 ) );
        thingsList.add( new Thing( "G", 2000 ) );
        thingsList.add( new Thing( "H", 4000 ) );
        thingsList.add( new Thing( "I", 10000 ) );
        thingsList.add( new Thing( "J", 25000 ) );
        thingsList.add( new Thing( "K", 5001 ) );
        thingsList.add( new Thing( "L", 6012 ) );
        thingsList.add( new Thing( "M", 42003 ) );
        thingsList.add( new Thing( "N", 51004 ) );
        thingsList.add( new Thing( "O", 100005 ) );
        thingsList.add( new Thing( "P", 130006 ) );
        thingsList.add( new Thing( "P", 200007 ) );
        thingsList.add( new Thing( "Q", 250008 ) );
        thingsList.add( new Thing( "R", 310009 ) );
        thingsList.add( new Thing( "S", 420009 ) );

        MyAdapter myAdapter = new MyAdapter( thingsList );
        recyclerView.setAdapter( myAdapter );

//        getDataFromServer("http://10.131.160.65/phpConnection.php");
        navigationView = rootView.findViewById( R.id.nav_view );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // 저장된 메모 가져오기
//        getMemosFromDatabase();

        return rootView;
    }

    private void updateProfile() {
        // 현재 로그인한 사용자의 프로필을 네비게이션뷰에 출력
        View view = navigationView.getHeaderView( 0 );
        TextView emailView = view.findViewById( R.id.userEmail );
        emailView.setText( firebaseUser.getEmail() );

        TextView nameView = view.findViewById( R.id.userName );
        nameView.setText( firebaseUser.getDisplayName() );

        ImageView imageView = view.findViewById( R.id.userImage );
        Glide.with( this ).load( firebaseUser.getPhotoUrl() ).into( imageView );
    }

    private void getMemosFromDatabase() {
        firebaseDatabase.getReference( "memos/" + firebaseUser.getUid() )
                .addChildEventListener( new ChildEventListener() {
                    // Data의 CRUD의 경우 각각 처리
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Memo memo = dataSnapshot.getValue( Memo.class );
                        memo.setKey( dataSnapshot.getKey() );

                        // 메모가 수정되었을 때 메뉴에서 제목 갱신
                        for(int i=0; i<navigationView.getMenu().size(); i++) {
                            MenuItem menuItem = navigationView.getMenu().getItem( i );
                            if(memo.getKey().equals( ((Memo)menuItem.getActionView().getTag() ).getKey())) {
                                menuItem.getActionView().setTag( memo );
                                menuItem.setTitle( memo.getTitle() );
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

//    public void getDataFromServer(String url) {
//        @SuppressLint("StaticFieldLeak")
//        class GetDataJSON extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected String doInBackground(String... strings) {
//                String uri = strings[0];
//
//
//                BufferedReader bufferedReader = null;
//                try {
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder stringBuilder = new StringBuilder(  );
//
//                    bufferedReader = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
//
//                    String json;
//                    while((json = bufferedReader.readLine()) != null) {
//                        stringBuilder.append( json + "\n" );
//                    }
//
//                    return stringBuilder.toString().trim();
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                myJSON = s;
//                showList();
//                super.onPostExecute( s );
//            }
//        }
//
//        GetDataJSON getDataJSON = new GetDataJSON();
//        getDataJSON.execute( url );
//    }

//    public void showList() {
//        try {
//            JSONObject jsonObj = new JSONObject(myJSON);
//            peoples = jsonObj.getJSONArray(TAG_RESULTS);
//
//            for (int i = 0; i < peoples.length(); i++) {
//                JSONObject c = peoples.getJSONObject(i);
//                String name = c.getString(TAG_THING_NAME);
//                String cost = c.getString(TAG_THING_COST);
//
//                HashMap<String, String> persons = new HashMap<>();
//
//                persons.put(TAG_THING_NAME, name);
//                persons.put(TAG_THING_COST, cost);
//
////                personList.add(persons);
//
//                Toast.makeText( getContext(), name, Toast.LENGTH_SHORT ).show();
//            }
//
//            ListAdapter adapter = new SimpleAdapter(
//                    getContext(), personList, R.layout.item_list,
//                    new String[]{TAG_THING_NAME, TAG_THING_COST},
//                    new int[]{R.id.thingName, R.id.thingCost}
//            );
//
//            listView.setAdapter( adapter );
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    private void displayMemoList(Memo memo) {
        Menu leftMenu = navigationView.getMenu();
        MenuItem item = leftMenu.add( memo.getTitle() );
        View view = new View(getContext());
        view.setTag( memo );
        item.setActionView( view );
    }

}

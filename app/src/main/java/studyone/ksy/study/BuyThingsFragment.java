package studyone.ksy.study;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

    // 상품들
    public ArrayList<Thing> thingsList = new ArrayList<>(  );
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton buyBtn;

    private NavigationView navigationView;


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
        navigationView = rootView.findViewById( R.id.nav_view );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        buyBtn = rootView.findViewById( R.id.buyBtn );

        buyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );

        return rootView;
    }

    private void displayMemoList(Memo memo) {
        Menu leftMenu = navigationView.getMenu();
        MenuItem item = leftMenu.add( memo.getTitle() );
        View view = new View(getContext());
        view.setTag( memo );
        item.setActionView( view );
    }

}

package studyone.ksy.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    // 인증 객체
    private FirebaseAuth firebaseAuth;
    // 유저 객체
    private FirebaseUser firebaseUser;
    // 데이터베이스
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    private NavigationView navigationView;
    private View view;
    private TextView emailView, nameView, userGradeView, userTypeView, userSavingCostView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child( "users/" ).child( firebaseUser.getUid() );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        view = navigationView.getHeaderView( 0 );
        emailView = view.findViewById( R.id.userEmail );
        nameView = view.findViewById( R.id.userName );
        imageView = view.findViewById( R.id.userImage );

        emailView.setText( firebaseUser.getEmail() );
        nameView.setText( firebaseUser.getDisplayName() );
        Glide.with( this ).load( firebaseUser.getPhotoUrl() ).into( imageView );

        userGradeView = view.findViewById( R.id.userGrade );
        userTypeView = view.findViewById( R.id.userType );
        userSavingCostView = view.findViewById( R.id.userSavingMoney );

//        // 인증 못받아오면 다시 AuthActivity로 이동
        if(firebaseUser == null) {
            startActivity( new Intent( this, InitActivity.class ) );
            finish();
            return;
        }

        // 프로필 설정
        updateProfile();

//        getUserFromDatabase();

        //////////////// 데이터베이스 검색
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()) {
//                    String key = postSnapShot.getKey();
//                    User gettedUser = postSnapShot.getValue( User.class );
//                    gettedUser.key = key;
//                    userGradeView.setText( gettedUser.userGrade );
//                    userTypeView.setText( gettedUser.getUserType() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        Query updateUserInfo = databaseReference;
        updateUserInfo.addListenerForSingleValueEvent( postListener );
        ////////////////////////

    }

    private void updateProfile() {
        // 현재 로그인한 사용자의 프로필을 네비게이션뷰에 출력
        View view = navigationView.getHeaderView( 0 );
        TextView emailView = view.findViewById( R.id.userEmail );
        TextView nameView = view.findViewById( R.id.userName );
        ImageView imageView = view.findViewById( R.id.userImage );

        emailView.setText( firebaseUser.getEmail() );
        nameView.setText( firebaseUser.getDisplayName() );
        Glide.with( this ).load( firebaseUser.getPhotoUrl() ).into( imageView );
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menuLogout) {
            logout();
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (id == R.id.toSetting) {
            /// 내 정보 변경 팝업 등장

        } else if(id == R.id.toMemoRoom) {
            fragment = new BuyThingsFragment();
            title = "상품구매";
        } else if(id == R.id.toChatRoom) {
            fragment = new CancelFragment();
            title = "구매취소";
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        return true;
    }

    private void logout() {
        Snackbar.make( getCurrentFocus(), "로그아웃 하시겠습니까?", Snackbar.LENGTH_SHORT ).setAction( "로그아웃", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity( new Intent(getApplicationContext(), InitActivity.class) );
                finish();
            }
        } ).show();
    }

}

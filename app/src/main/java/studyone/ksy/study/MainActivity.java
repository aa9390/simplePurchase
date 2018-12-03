package studyone.ksy.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import studyone.ksy.study.model.Memo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    // 인증 객체
    private FirebaseAuth firebaseAuth;
    // 유저 객체
    private FirebaseUser firebaseUser;
    // 데이터베이스
    private FirebaseDatabase firebaseDatabase;

    private NavigationView navigationView;
    private EditText memoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        context = getBaseContext();

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        FloatingActionButton saveMemoBtn = (FloatingActionButton) findViewById( R.id.saveMemoBtn );

        // 메모 저장 버튼 클릭 시
        saveMemoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saveMemo();
            }
        } );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        memoText = (EditText) findViewById( R.id.contentMemo );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // 인증 못받아오면 다시 AuthActivity로 이동
        if(firebaseUser == null) {
            startActivity( new Intent( this, AuthActivity.class ) );
            finish();
            return;
        }

        // 프로필 설정
        updateProfile();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    public void saveMemo() {
        if(memoText.getText().toString().isEmpty()) {
            Snackbar.make( memoText, "메모를 입력해 주세요.", Snackbar.LENGTH_SHORT ).show();
        }
        else {
            // Model 생성
            Memo memo = new Memo();
            memo.setTxt( memoText.getText().toString() );
            memo.setCreateDate( new Date() );   // 현재 날짜

            // setValue의 인자로 DB에 들어갈 데이터 형식의 Object가 들어감
            firebaseDatabase.getReference( "memos/" + firebaseUser.getUid() ).push().setValue( memo )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // 메모 저장 성공 시
                            Snackbar.make( memoText, "메모가 저장되었습니다.", Snackbar.LENGTH_SHORT ).show();
                            // 새로운 메모를 받기 위해 입력한 메모 초기화
                            initMemo();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 메모 저장 실패 시
                            Snackbar.make( memoText, "네트워크 설정을 확인해 주세요.", Snackbar.LENGTH_SHORT ).show();
                        }
                    } );
        }
    }

    public void initMemo() {
        memoText.setText( null );
    }

    private void updateProfile() {
        // 현재 로그인한 사용자의 프로필을 네비게이션뷰에 출력
        View view = navigationView.getHeaderView( 0 );
        TextView emailView = view.findViewById( R.id.userEmail );
        emailView.setText( firebaseUser.getEmail() );

        TextView nameView = view.findViewById( R.id.userName );
        nameView.setText( firebaseUser.getDisplayName() );

        ImageView imageView = view.findViewById( R.id.userImage );
        imageView.setImageURI( firebaseUser.getPhotoUrl() );
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

    private void displayMemoList(Memo memo) {
        Menu leftMenu = navigationView.getMenu();
        leftMenu.add( memo.getTitle() );
    }
}

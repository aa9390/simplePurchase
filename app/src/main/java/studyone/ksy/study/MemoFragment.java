package studyone.ksy.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import studyone.ksy.study.model.Memo;

public class MemoFragment extends Fragment {

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

    private NavigationView navigationView;
    private EditText memoText;
    private String selectedMemoKey;
    private FloatingActionButton saveMemoBtn;

    public MemoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        saveMemoBtn = (FloatingActionButton) getView().findViewById( R.id.saveMemoBtn );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // 저장된 메모 가져오기
        getMemosFromDatabase();

        // 메모 저장 버튼 클릭 시
        saveMemoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메뉴에서 메모를 선택한 상태이면 -> selectedMemoKey로 불러온 것
                // 따라서 selectedMemoKey가 null이면 그대로 save, 아니면 update
                if(selectedMemoKey == null) {
                    saveMemo();
                }
                else {
                    updateMemo();
                }

            }
        } );
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_memo, container, false );
    }

    public void initMemo() {
        selectedMemoKey = null; // update 시 혼선 막기 위함
        memoText.setText( "" );
    }

    public void saveMemo() {
        if(memoText.getText().toString().isEmpty()) {
            Snackbar.make( memoText, "메모를 입력해 주세요.", Snackbar.LENGTH_SHORT ).show();
        }
        else {
            // Model 생성
            Memo memo = new Memo();
            memo.setTxt( memoText.getText().toString() );
            memo.setCreateDate( new Date().getTime() );   // 현재 날짜

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

    public void updateMemo() {
        if (memoText.getText().toString().isEmpty()) {
            Snackbar.make( memoText, "메모를 입력해 주세요.", Snackbar.LENGTH_SHORT ).show();
            return;
        } else {
            // Model 생성
            Memo memo = new Memo();
            memo.setTxt( memoText.getText().toString() );
            memo.setCreateDate( new Date().getTime());   // 현재 날짜
            firebaseDatabase.getReference( "memos/" + firebaseUser.getUid() )
                    .setValue( memo )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make( memoText, "메모가 정상적으로 수정되었습니다.", Snackbar.LENGTH_SHORT ).show();
                        }
                    } );
        }
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

    private void deleteMemo() {
        if(selectedMemoKey == null) {
            return;
        }
        Snackbar.make( memoText, "메모를 삭제하시겠습니까?", Snackbar.LENGTH_SHORT ).setAction( "삭제", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.getReference( "memos/" + firebaseUser.getUid() )
                        .removeValue( new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Snackbar.make( memoText, "삭제가 완료되었습니다.", Snackbar.LENGTH_SHORT ).show();
                            }
                        } );
            }
        } ).show();

    }

    private void logout() {
        Snackbar.make( memoText, "로그아웃 하시겠습니까?", Snackbar.LENGTH_SHORT ).setAction( "로그아웃", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity( new Intent(getActivity(), InitActivity.class) );
            }
        } ).show();
    }

    private void getMemosFromDatabase() {
        firebaseDatabase.getReference( "memos/" + firebaseUser.getUid() )
                .addChildEventListener( new ChildEventListener() {
                    // Data의 CRUD의 경우 각각 처리
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        Memo memo = dataSnapshot.getValue( Memo.class );
//                        Log.d("@@@@dataSnapshotValue:", dataSnapshot.getValue( Memo.class ).toString());
//                        memo.setKey( dataSnapshot.getKey() );
//                        displayMemoList( memo );
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

    private void displayMemoList(Memo memo) {
        Menu leftMenu = navigationView.getMenu();
        MenuItem item = leftMenu.add( memo.getTitle() );
        View view = new View(getContext());
        view.setTag( memo );
        item.setActionView( view );
    }



}

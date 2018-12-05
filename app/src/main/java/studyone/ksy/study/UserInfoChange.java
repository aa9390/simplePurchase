package studyone.ksy.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import studyone.ksy.study.model.User;

public class UserInfoChange extends AppCompatActivity {

    // 인증 객체
    private FirebaseAuth firebaseAuth;
    // 유저 객체
    private FirebaseUser firebaseUser;
    // 데이터베이스
    private static FirebaseDatabase firebaseDatabase;

    private RadioGroup radioUserType;
    private RadioButton radioGeneral;
    private RadioButton radioStaff;
    private RadioButton radioVeterans;
    private Button userInfoSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_info_change );

        // Intance 얻어오기
        firebaseAuth = FirebaseAuth.getInstance();  // Singleton이기 때문에 Instance 유지됨
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        radioUserType = findViewById( R.id.radioUserType );
        radioGeneral = findViewById( R.id.radioGeneral );
        radioStaff = findViewById( R.id.radioStaff );
        radioVeterans = findViewById( R.id.radioVeterans );
        userInfoSaveBtn = findViewById( R.id.userInfosaveBtn );

        userInfoSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioUserType.getCheckedRadioButtonId();
                RadioButton rb = findViewById( id );

                User user = new User( "sliver", rb.getText() + "", 0.0, 0.0, 0  );
                user.setUserSavingRateOfType( user.getUserType() );
                user.setUserSavingRateOfGrade( user.getUserGrade() );

                // setValue의 인자로 DB에 들어갈 데이터 형식의 Object가 들어감
                firebaseDatabase.getReference( "users/" + firebaseUser.getUid()).push().setValue( user )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 정보 저장 성공 시
                        Snackbar.make( radioUserType, "정보가 저장되었습니다.", Snackbar.LENGTH_SHORT ).show();
                        startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                        finish();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 정보 저장 실패 시
                        Snackbar.make( radioUserType, "네트워크 설정을 확인해 주세요.", Snackbar.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );

    }
}

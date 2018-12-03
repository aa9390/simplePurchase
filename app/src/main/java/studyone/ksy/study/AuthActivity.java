package studyone.ksy.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        signInButton = findViewById( R.id.signInButton );

        googleSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestIdToken( getString(R.string.default_web_client_id ))    // 서버의 클라이언트 id를 requestIdToken에 전달
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder( this )
                .enableAutoManage( this, this )
                .addApi( Auth.GOOGLE_SIGN_IN_API , googleSignInOptions)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();

        googleSignInClient = GoogleSignIn.getClient( this, googleSignInOptions );
        signInButton.setSize( SignInButton.SIZE_STANDARD ); // 기본 사이즈
        signInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent( googleApiClient );
                startActivityForResult( intent, 100 );  // 인증 성공 시 code 반환
            }
        } );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode == 100) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            GoogleSignInAccount account = result.getSignInAccount();

            if(result.isSuccess()) {
                firebaseAuthWithGoogle( account );
            }
            else {
                Toast.makeText( getApplicationContext(), "네트워크 설정을 확인해 주세요.", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // 자격증명 생성.
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential( credential );

        // resultTask가 모두 완료된 시점에 성공여부 확인 가능
        authResultTask.addOnSuccessListener( new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = authResult.getUser();
                String email = firebaseUser.getEmail();
                Toast.makeText( getApplicationContext(), email+"님 안녕하세요", Toast.LENGTH_SHORT ).show();

                Intent intent = new Intent( getApplicationContext(), MainActivity.class);
                startActivity( intent );
                finish();
            }
        } );
    }

}

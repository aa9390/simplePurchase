package studyone.ksy.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class AuthActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        signInButton = findViewById( R.id.signInButton );
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN )
                .requestIdToken( getString(R.string.default_web_client_id ))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder( this )
                .enableAutoManage( this, this )
                .addApi( Auth.GOOGLE_SIGN_IN_API , googleSignInOptions)
                .build();

        signInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = Auth.GoogleSignInApi.getSignInIntent( googleApiClient );
//                startActivityForResult( intent, 100 );

                Toast.makeText( getApplicationContext(), "aaaa", Toast.LENGTH_SHORT).show();
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode == 100) {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

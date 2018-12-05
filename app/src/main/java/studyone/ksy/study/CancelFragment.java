package studyone.ksy.study;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import studyone.ksy.study.model.Thing;

public class CancelFragment extends Fragment {

    // 인증 객체
    private FirebaseAuth firebaseAuth;
    // 유저 객체
    private FirebaseUser firebaseUser;
    // 데이터베이스
    private static FirebaseDatabase firebaseDatabase;

    // 상품들
    public ArrayList<Thing> thingsList;

    public CancelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_chat, container, false );
    }
}

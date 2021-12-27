package com.hafize.chatapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {

   private EditText email, password;
   private Button login;
   private TextView signUp;
   private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth+"nediiiiiiir");
        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null){

            ChatFragment fragment = new ChatFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment, "profile").addToBackStack("profile")
                    .commit();
          /*  Intent intent = new Intent(getApplicationContext(), ChatFragment.class);
            startActivity(intent);*/
        }


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up, container, false);

        email = view.findViewById(R.id.user_email_edit_text);
        password = view.findViewById(R.id.password_edit_text);
        login = view.findViewById(R.id.btn_login);
        signUp = view.findViewById(R.id.tv_sign_up);
        signUp.setOnClickListener(view1 -> signUp());
        login.setOnClickListener(view12 -> signIn());
        return view;
    }

    public void signUp(){

        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userEmail = user.getEmail().toString();
                    System.out.println(userEmail+ "emaili");

                    ChatFragment fragment = new ChatFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment, "profile").addToBackStack("profile")
                            .commit();
              /*      Intent intent = new Intent(getApplicationContext(), ChatFragment.class);
                    startActivity(intent);
*/
                }
                else{
                    Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signIn(){

        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   /* Intent intent = new Intent(getApplicationContext(), ChatFragment.class);
                    startActivity(intent);*/

                    ChatFragment fragment = new ChatFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment, "profile").addToBackStack("profile")
                            .commit();
                }
                else{
                    Toast.makeText(getActivity(),"Failed to Sign in",Toast.LENGTH_LONG).show();
                }

            }
        });

    }




}

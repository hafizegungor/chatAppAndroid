package com.hafize.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView btnSend;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<String> messageList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_profile) {

            ProfileFragment fragment = new ProfileFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment, "profile").addToBackStack("profile")
                    .commit();
        } else if (item.getItemId() == R.id.option_out) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        messageList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        editText = view.findViewById(R.id.text_content);
        btnSend = view.findViewById(R.id.btn_send);
        recyclerViewAdapter = new RecyclerViewAdapter(messageList);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);
        btnSend.setOnClickListener(view1 -> sendMessage(view1));

        getData();
        return view;
    }

    public void sendMessage(View view) {

        String messageToSend = editText.getText().toString();

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail().toString();

        databaseReference.child("Chats").child(uuidString).child("userMessage").setValue(messageToSend);
        databaseReference.child("Chats").child(uuidString).child("userEmail").setValue(userEmail);
        databaseReference.child("Chats").child(uuidString).child("userMessageTime").setValue(ServerValue.TIMESTAMP);
        editText.setText("");
        //databaseReference.child("Chats").child("Chat Test").child("Test 2").setValue(messageToSend);
    }

    public void getData() {

        DatabaseReference newReference = database.getReference("Chats");
        Query query = newReference.orderByChild("userMessageTime");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String userEmail = hashMap.get("userEmail");
                    String userMessage = hashMap.get("userMessage");

                    messageList.add(userMessage);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
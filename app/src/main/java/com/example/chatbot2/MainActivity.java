package com.example.chatbot2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private EditText userMsgET;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV = findViewById(R.id.idRVChats);
        userMsgET = findViewById(R.id.idETMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);
        chatsModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMsgET.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter you msg", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgET.getText().toString());
                userMsgET.setText("");
            }
        });
    }
    private void getResponse(String msg){
        chatsModalArrayList.add(new ChatsModal(msg, USER_KEY));
        String url = "http://192.168.0.5:5000/?msg="+msg;   // API url setting (http://api/~~~/get?bid=~~~&key=~~~&uid=[uid]&msg)
        //Log.v("checkurl",url);
        String BASE_URL = "http://192.168.0.5:5000/";   //  (http://api/~~~/)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Log.v("checkretrofit", String.valueOf(retrofit));
        //Log.v("checkretrofitbaseurl", String.valueOf(retrofit.baseUrl()));
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        //Log.v("checkretrofitapi", String.valueOf(retrofitAPI));
        Call<MsgModal> call = retrofitAPI.getMessage(url);
        //Log.v("checkcall", String.valueOf(retrofitAPI.getMessage(url)));
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {

                if(response.isSuccessful()){
                    MsgModal modal = response.body();
                    Log.v("checkresponce", String.valueOf(modal));
                    chatsModalArrayList.add(new ChatsModal(modal.getAnswer(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatsModalArrayList.add(new ChatsModal("failure",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }
}
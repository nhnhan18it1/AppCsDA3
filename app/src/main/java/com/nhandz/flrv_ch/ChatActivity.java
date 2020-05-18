package com.nhandz.flrv_ch;

import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nhandz.flrv_ch.DT.*;

import java.io.IOException;
import java.util.ArrayList;

import io.socket.emitter.Emitter;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.beardedhen.androidbootstrap.*;
import com.nhandz.flrv_ch.Adapters.*;

import com.nhandz.flrv_ch.ApiResuorce.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {
    public account AccOnChat;
    private TextView txtName;
    private BootstrapCircleThumbnail imgAvt;
    private BootstrapButton btnBack;
    private BootstrapButton btnCall;
    private BootstrapButton btnVideoCall;
    private BootstrapButton btnInfor;
    private BootstrapEditText txtMessContent;
    private BootstrapButton btnSend;
    private RecyclerView listContentMess;
    private adapter_ContentChat adt;
    private ArrayList<Chats> listChats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Intent intent=getIntent();
        listfriend lf = (listfriend) intent.getSerializableExtra("dataUserForChat");
        AX();
        event();
        new GetInforForChat(lf).execute();

        MainActivity.mSocket.on("Server-send-messagePP", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                ArrayList<NodeMess> nodeMesses=new ArrayList<>();
//                Gson gson =new Gson();
//                NodeMess[] messes=gson.fromJson((JsonArray)args[0],NodeMess[].class);
//                for (int i=0;i<messes.length;i++){
//                    nodeMesses.add(messes[i]);
//                }
                JSONObject jsonObject= (JSONObject)args[0];
                try {
                    listChats.add(new Chats("x",jsonObject.getString("ID"),jsonObject.getString("IDR"),jsonObject.getString("Content"),"0","",""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adt.notifyDataSetChanged();
                        listContentMess.scrollToPosition(listChats.size()-1);
                    }
                });
                Log.e("mess re", "doInBackground: "+ args[0]);


            }
        });
//        initChat();
    }

    public void AX(){
        txtName=findViewById(R.id.chatAc_Name);
        imgAvt=findViewById(R.id.chatAc_Avt);
        btnBack=findViewById(R.id.chatAc_btnBack);
        btnCall=findViewById(R.id.chatAc_btncall);
        btnVideoCall=findViewById(R.id.chatAc_btnVideocall);
        btnInfor=findViewById(R.id.chatAc_btnInfor);
        txtMessContent=findViewById(R.id.chatAc_inputMess);
        btnSend=findViewById(R.id.chatAc_btnSendMess);
        listContentMess=findViewById(R.id.chatAc_listMess);
    }

    public void event(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtMessContent.getText().toString().equals("")){
                    JSONObject jsonObject;
                    try {
                        jsonObject=new JSONObject("{'ID':'"+MainActivity.OnAccount.getID()+"','IDR':'"+AccOnChat.getID()+"','Name':'"+MainActivity.OnAccount.getName()+"','Content':'"+txtMessContent.getText().toString()+"'}");
                        MainActivity.mSocket.emit("Client-send-messagePP",jsonObject);
                        new  MessSengerApi.SendMess(adt,listChats,
                                String.valueOf(AccOnChat.getID()),
                                String.valueOf(MainActivity.OnAccount.getID()) ,
                                 txtMessContent.getText().toString(),
                                getApplicationContext()).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public void initChat(){
        listContentMess.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        listContentMess.setLayoutManager(linearLayoutManager);
        listChats=new ArrayList<>();
//        listChats.add(new Chats("1","1","2","asdasd","asdas","saad","asdsa"));
//        listChats.add(new Chats("12","2","1","asdasd","asdas","saad","asdsa"));
        adt=new adapter_ContentChat(listChats,getApplicationContext(),AccOnChat);
        listContentMess.setAdapter(adt);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public class GetContentMess extends AsyncTask<Void,Void,String>{
        private String ID1;
        private String ID2;
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        public GetContentMess(String ID1, String ID2) {
            this.ID1 = ID1;
            this.ID2 = ID2;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .addFormDataPart("ID1",ID1)
                    .addFormDataPart("ID2",ID2)
                    .setType(MultipartBody.FORM)
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent", MainActivity.User_Agent)
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/loadMess")
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals(null)){
                Gson gson=new Gson();
                Chats[] messes=gson.fromJson(s,Chats[].class);
                for (int i=0;i<messes.length;i++){
                    listChats.add(messes[i]);
                }
                adt.notifyDataSetChanged();
                listContentMess.scrollToPosition(listChats.size()-1);
            }
        }
    }

    public class GetInforForChat extends AsyncTask<Void,Void,String>{
        private listfriend lf;
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        public GetInforForChat(listfriend lf) {
            this.lf = lf;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String ID="";
            if (lf.getID1().equals(String.valueOf(MainActivity.OnAccount.getID()))){
                ID=lf.getID2();
            }
            else {
                ID=lf.getID1();
            }
            RequestBody requestBody=new MultipartBody.Builder()
                    .addFormDataPart("ID",ID)
                    .setType(MultipartBody.FORM)
                    .build();
            Request request=new Request.Builder()
                    .addHeader("User-Agent", MainActivity.User_Agent)
                    .addHeader("Cookie", MainActivity.cookies)
                    .url(MainActivity.server+"/api/loadinforuser1")
                    .post(requestBody)
                    .build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("loadifUs", "onPostExecute: "+s );
            super.onPostExecute(s);
            if (!s.equals(null)){

                Gson gson=new Gson();
                account[] acc=gson.fromJson(s,account[].class);
                AccOnChat=acc[0];
                txtName.setText(acc[0].getName());
                Glide.with(getApplicationContext())
                        .load(MainActivity.serverImg+""+acc[0].getAvt())
                        .error(R.drawable.icons_search)
                        .into(imgAvt);
                initChat();
                adt.UpdateAvt();
                new GetContentMess(lf.getID1(),lf.getID2()).execute();
            }
        }
    }
    public class WaitForMess extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            MainActivity.mSocket.on("Server-send-messagePP", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    ArrayList<NodeMess> nodeMesses=new ArrayList<>();
                    Gson gson =new Gson();
                    NodeMess[] messes=gson.fromJson((JsonArray)args[0],NodeMess[].class);
                    for (int i=0;i<messes.length;i++){
                        nodeMesses.add(messes[i]);
                    }
                    Log.e("mess re", "doInBackground: "+ nodeMesses.get(0).getContent());


                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}

package com.nhandz.flrv_ch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhandz.flrv_ch.DT.NodeFriend;
import com.nhandz.flrv_ch.DT.TextviewFont;
import com.nhandz.flrv_ch.DT.account;
import com.nhandz.flrv_ch.Service.Autosend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.nhandz.flrv_ch.Alert.*;


public class MainActivity extends AppCompatActivity {

    public static String server="https://fffcv.herokuapp.com"; //;
    public static String serverImg="https://fffcv.herokuapp.com";//;"http://192.168.61.50:8888"
    public static String cookies="";//""__test=afd4ff1e1b7f9ade68ae2dc16420678d; expires=Thu, 31-Dec-38 23:55:55 GMT; path=/";
    public static String User_Agent="";//""Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ";
    public static String XSRF_TOKEN=""; //"eyJpdiI6InNYRzRBSW9acWRJSW1sd0hDbUQzaWc9PSIsInZhbHVlIjoiRzlUVW5vQ293ZGJOQ2ZIVjdBRVlLZDRHbFBmaXk1MzcwVDdhaDJ2aEpBb09cL3NyRUR1VVMzKytKdkYxRVdHc2UiLCJtYWMiOiI4ZmExOWQ4ZWUwNWEwZjUyNDg4ZGU5MDEzZDQzYWY2NWYxN2Y4NGQ5NmY4ZWI5YWVjMTVhZGI2Nzk0ODM0YWEwIn0%3D";
    public static account OnAccount=null;
    public static String Nodeserver="https://nhandz.herokuapp.com/";//;"http://192.168.61.50:3000"
    public static Socket mSocket;
    private static SharedPreferences sharedPreferences;
    public static float Screen_width;
    public static float Screen_height;
    private static final int ALL_PERMISSIONS_CODE = 1 ;
    Dialog alert_login;
    Button btnnext;
    TextviewFont textviewFont;
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS_CODE
                && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // all permissions granted
            //start();
        } else {
            //finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Screen_height = displayMetrics.heightPixels;
        Screen_width = displayMetrics.widthPixels;
        askPermissions();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE}, ALL_PERMISSIONS_CODE);
        } else {
            // all permissions already granted
            //start();
        }
        alert_login=new Dialog(this);
        alert_login.setContentView(R.layout.alert_readylogin);
        alert_login.setCanceledOnTouchOutside(false);
        alert_login.show();
        if (OnAccount!=null){
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }
        try {
            mSocket= IO.socket(Nodeserver);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        sharedPreferences=getSharedPreferences("clientConfig",MODE_PRIVATE);
        String s = sharedPreferences.getString("ID","");

        if (s==""){
            Log.e("MainAc", "onCreate: Not login" );
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        else {
            Log.e("MainAc", "onCreate:lohined "+s );
            new LoadFullInfor(s).execute();
        }
//        MainActivity.mSocket.on("server-send-listus", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                ArrayList<NodeFriend> nodeFriends=new ArrayList<>();
//                JSONArray jsonArray=(JSONArray)args[0];
//                try {
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject=(JSONObject) jsonArray.get(i);
//                        nodeFriends.add(new NodeFriend(
//                                jsonObject.getString("IDND"),
//                                jsonObject.getString("Name"),
//                                jsonObject.getString("Avt"),
//                                jsonObject.getString("IDN")
//                        ));
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.e("l-lít", "call: "+nodeFriends.size() );
//
//            }
//        });

        btnnext=findViewById(R.id.btnNext);
        //btnnext.setVisibility(View.INVISIBLE);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        textviewFont=findViewById(R.id.main_ipconfig);
        textviewFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ConfigAutoSendActivity.class);
                startActivity(intent);

            }
        });
        if (OnAccount!=null){
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }

    }
    public class LoadFullInfor extends AsyncTask<Void,String,String>{
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        private String ID;

        public LoadFullInfor(String ID) {
            this.ID = ID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ID",ID)
                    .build();
            Request request=new Request.Builder()
//                    .addHeader("cookies",cookies)
//                    .addHeader("User_Agent",User_Agent)
                    .url(MainActivity.server+"/api/loadInforUser_full")
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
            Log.e("MainAc", "onPostExecute: "+s );
            if (s!=null){
                try {
                    JSONArray jsonArray=new JSONArray(s);
                    Gson gson=new Gson();
                    account[] accounts=gson.fromJson(s,account[].class);
                    MainActivity.OnAccount=accounts[0];
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("IDND",accounts[0].getID());
                    jsonObject.put("Name",accounts[0].getName());
                    jsonObject.put("Avt",accounts[0].getAvt());
                    mSocket.emit("client-send-ID",jsonObject);
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Lỗi đăng nhập tự động", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }


            }
            else if (s==null||s=="[]"){
                alert_login.dismiss();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}

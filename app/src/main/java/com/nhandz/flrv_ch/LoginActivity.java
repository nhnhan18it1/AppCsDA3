package com.nhandz.flrv_ch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhandz.flrv_ch.DT.account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    EditText txtUser;
    EditText pwfPass;
    BootstrapButton btnLogin;
    ImageButton btnBack;
    ProgressBar progressBar;
    BlurView blurView;
    public static String ID="1";
    public static String Avt="/FLRV-CH/local/public/assets/img/avatar/avt_Nguyen Hai Nhan_56.png";
    public static String Name="Nguyen Hai Nhan";
    String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Anhxa();
        if (MainActivity.OnAccount!=null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
        progressBar.setVisibility(View.INVISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                Login(MainActivity.server+"/api/login");
                //new  LoginX().execute();


            }
        });
        if (MainActivity.OnAccount!=null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
        float radius = 10f;

        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

    }

    public void Anhxa(){
        txtUser=findViewById(R.id.txtTaiKhoan);
        pwfPass=findViewById(R.id.pwfMatkhau);
        btnLogin=findViewById(R.id.btnLogin);
        progressBar=(ProgressBar) findViewById(R.id.loadLogin);
        btnBack=findViewById(R.id.btn_Login_Back);
        blurView=findViewById(R.id.blurView);
    }

    public void Login(String url){
        //Log.e("url", "Login: "+url );
        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               if (response.trim()!="fail"){
                   json=response;
                   //Log.e("loginJ", "onResponse: "+response );
                   progressBar.setVisibility(View.INVISIBLE);
                   btnLogin.setEnabled(false);
                   new GLogin().execute(response);

               }
               else {
                   progressBar.setVisibility(View.INVISIBLE);
                   btnLogin.setEnabled(true);
                   json="";
                   Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
               }
                Log.e("res", "onResponse: "+json+"--" );
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrC", "onErrorResponse: "+error.toString() );
                progressBar.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();

                params.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
                params.put("Cookie", MainActivity.cookies);
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("username",txtUser.getText().toString().trim());
                params.put("password",pwfPass.getText().toString().trim());
                return params;
            }
        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void Login2(String url){
        OkHttpClient okHttpClient =new OkHttpClient.Builder()
                .build();

        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username",txtUser.getText().toString().trim())
                .addFormDataPart("password",pwfPass.getText().toString().trim())
                .build();

        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url)
                .method("post",requestBody)
                .build();
        try {
            okhttp3.Response response=okHttpClient.newCall(request).execute();

            new GLogin().execute(response.body().string());
            //return response.body().string();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("getnews", "doInBackground: "+e );
        }
    }

    public class ReadJsonAcc extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {


            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array=new JSONArray(s);
                for (int i=0;i<array.length();i++){
                    JSONObject jsonObject=array.getJSONObject(i);
                    LoginActivity.ID=jsonObject.getString("ID");
                    LoginActivity.Avt=jsonObject.getString("Avt");
                    LoginActivity.Name=jsonObject.getString("name");

                }
                Log.e("kq", "onPostExecute: "+LoginActivity.ID );
                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class LoginX extends AsyncTask<Void,Void,String>{

        OkHttpClient okHttpClient =new OkHttpClient.Builder()
                .build();
        private  String user = txtUser.getText().toString().trim();
        private  String pass = pwfPass.getText().toString().trim();
        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username",user)
                    .addFormDataPart("password",pass)
                    .build();

            okhttp3.Request request=new okhttp3.Request.Builder()
                    .url(MainActivity.server+"/api/login")
                    .method("post",requestBody)
                    .build();
            try {
                okhttp3.Response response=okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e("getnews", "doInBackground: "+e );
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                new GLogin().execute(s);
            }

        }
    }

    private class GLogin extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("login", "onPostExecute: "+s );
            //Toast.makeText(LoginActivity.this, account.toString(), Toast.LENGTH_SHORT).show();
            Gson gson=new Gson();
            account[] accounts=gson.fromJson(s,account[].class);
            LoginActivity.ID=String.valueOf(accounts[0].getID()) ;
            LoginActivity.Avt=accounts[0].getAvt();
            LoginActivity.Name=accounts[0].getName();
            MainActivity.OnAccount=accounts[0];

            String ttac="{IDND : "+accounts[0].getID()+" , Name : ' "+accounts[0].getName()+" ' , Avt : ' "+accounts[0].getAvt()+" ' }";
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(ttac);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.mSocket.emit("client-send-ID",jsonObject);
            SharedPreferences.Editor editor= MainActivity.getSharedPreferences().edit();
            editor.putString("ID",String.valueOf(accounts[0].getID()));
            editor.commit();
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }
}

package com.nhandz.flrv_ch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class baivietmoiActivity extends AppCompatActivity {

    private Button btnPost;
    private ImageView imgVP;
    private String path="";
    private TextView txtName;
    private int REQUEST_CODE_IMGCHOICE=123;
    private BootstrapCircleThumbnail avt;
    private BootstrapEditText txtcontent;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baivietmoi);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn

        anhxa();
        imgVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_IMGCHOICE);
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path!=""&&!txtcontent.getText().toString().equals("")){
                    new PostF().execute(MainActivity.server+"/api/postfile");
                }
                else {
                    Toast.makeText(baivietmoiActivity.this, "Vui lòng điền dầy đủ thông tin bài viết", Toast.LENGTH_SHORT).show();
                }

            }
        });
        GlideUrl url2=new GlideUrl(MainActivity.serverImg+""+MainActivity.OnAccount.getAvt(),
                new LazyHeaders.Builder()
                        .addHeader("Cookie",MainActivity.cookies)
                        .build()
        );
        Glide.with(getApplicationContext())
                .load(url2)
                .timeout(3000)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avt);
        txtName.setText(MainActivity.OnAccount.getName());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMGCHOICE && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            path=getRealPathFromURI(uri);
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .fitCenter()
                        .timeout(5000)
                        .skipMemoryCache(false)
                        .fitCenter()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((int)MainActivity.Screen_width,700)
                        .error(R.drawable.logo)
                        .into(imgVP);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {


        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj,
                null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void anhxa() {
        btnPost = findViewById(R.id.home_btnPost);
        imgVP=findViewById(R.id.home_imgP);
        avt=findViewById(R.id.bvm_avt);
        txtcontent=findViewById(R.id.bvm_edtcontent);
        txtName=findViewById(R.id.bvm_name);
        dialog=new Dialog(this);
    }



    private class PostF extends AsyncTask<String,Void,String> {

        String content="";
        String IDND="";

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .callTimeout(50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content=txtcontent.getText().toString();
            IDND=String.valueOf(MainActivity.OnAccount.getID());

            dialog.setContentView(R.layout.alert_readylogin);
            dialog.setTitle("Uploading");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            File file=new File(path);
            String content_type=getType(file.getPath());
            String file_path=file.getAbsolutePath();
            RequestBody file_Body=RequestBody.create(MediaType.parse(content_type),file);

            RequestBody requestBody=new MultipartBody.Builder()
                    .addFormDataPart("fileimg",file_path.substring(file_path.lastIndexOf("/")+1),file_Body)
                    //.addFormDataPart("b64_img",encodedImage)
                    .addFormDataPart("Content",content)
                    .addFormDataPart("IDND",IDND)
                    .setType(MultipartBody.FORM)
                    .build();
            Request request=new Request.Builder()
                    //.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ")
                    //.addHeader("Cookie", MainActivity.cookies)
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            }
            catch (IOException e) {
                Log.e("Fpost", "doInBackground: "+e );
                //e.printStackTrace();
                return "0";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("fpost", "onPostExecute: "+s );
            if (!s.equals("0")){
                Toast.makeText(baivietmoiActivity.this, s, Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(baivietmoiActivity.this, "Loi upload", Toast.LENGTH_SHORT).show();

            }

        }

        private String getType(String paths) {
            String extendt= MimeTypeMap.getFileExtensionFromUrl(paths);
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extendt);
        }
    }

}

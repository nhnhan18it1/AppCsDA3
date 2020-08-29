package com.nhandz.flrv_ch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nhandz.flrv_ch.VideoCall.SendCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import at.markushi.ui.CircleButton;

public class ReceiveCallActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    CircleButton btnAcCall;
    CircleButton btnCanCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_call);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIÊU ĐỀ ACTIVITY"); //Thiết lập tiêu đề nếu muốn
        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
        actionBar.hide(); //Ẩn ActionBar nếu muốn
        Anhxa();
        BG_animation();
        CallAnimation();
        CancelAnimation();

        Intent intent=getIntent();
        String rs = intent.getStringExtra("data");
        if (rs!=null && rs!="{}"){
            try {
                JSONObject jsonObject=new JSONObject(rs);
                btnAcCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject jo=new JSONObject();
                        try {
                            jo.put("isAccept",true);
                            MainActivity.mSocket.emit("c_an_request_to",jo);
                            MainActivity.mSocket.emit("join",jsonObject.getString("IDS")+MainActivity.OnAccount.getID());
                            Intent intent1=new Intent(ReceiveCallActivity.this, SendCallActivity.class);
                            intent1.putExtra("Type","R");
                            intent1.putExtra("data",jsonObject.toString());
                            startActivity(intent1);

                            //finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                btnCanCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject jo=new JSONObject();
                        try {
                            jo.put("isAccept",false);
                            MainActivity.mSocket.emit("c_an_request_to",jo);

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void BG_animation(){
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    public void Anhxa(){
        btnAcCall=findViewById(R.id.rec_acCall);
        btnCanCall=findViewById(R.id.rec_caCall);
        constraintLayout=findViewById(R.id.rec_layout);
    }

    public void CallAnimation(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(btnAcCall, PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
    }

    public void CancelAnimation(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(btnCanCall, PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
    }
}
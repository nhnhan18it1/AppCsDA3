package com.nhandz.flrv_ch.Alert;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.nhandz.flrv_ch.Adapters.adapter_comment;
import com.nhandz.flrv_ch.ApiResuorce.Utils2;
import com.nhandz.flrv_ch.DT.comments;
import com.nhandz.flrv_ch.HomeActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class comments_bottom_sheet extends BottomSheetDialogFragment {

    private String TAG=this.getClass().getSimpleName();

    private View view;
    private ImageButton btnExit;
    private BootstrapEditText txtInput;
    private BootstrapButton btnSend;
    private RecyclerView rcv_lcmt;
    private ArrayList<comments> commentsArrayList;
    private adapter_comment adt;
    private String rs;
    private String OnIDBV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_comments,container,false);
        Anhxa();
        InitRe();
        rs = getArguments().getString("comments");
        OnIDBV = getArguments().getString("IDBV");
        Log.e(TAG, "onCreateView: "+rs );
        Gson gson=new Gson();
        comments[] cmts = gson.fromJson(rs,comments[].class);
        for (comments cmt: cmts){
            commentsArrayList.add(cmt);
        }
        adt.notifyDataSetChanged();
        btnExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtInput.getText().toString().equals(""))return;
                if (adapter_comment.data_comments!=null){
                    adapter_comment.data_comments.add(new comments(Integer.parseInt(OnIDBV),
                            MainActivity.OnAccount.getID(),
                            MainActivity.OnAccount.getName(),
                            MainActivity.OnAccount.getAvt(),
                            txtInput.getText().toString(),
                            "",
                            ""));
                }
                adt.notifyDataSetChanged();
                String s=txtInput.getText().toString();
                txtInput.setText("");
                Utils2.getInstance().getRetrofitInstance().sendComments(
                        String.valueOf(OnIDBV),
                        String.valueOf(MainActivity.OnAccount.getID()),
                        MainActivity.OnAccount.getAvt(),
                        MainActivity.OnAccount.getName(),
                        s,
                        "Cũng đã bình luận về bài viết"
                        ).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "onResponse: "+response.body() );
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
            }
        });
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                View parent = (View) view.getParent();
//                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
//                CoordinatorLayout.Behavior behavior = params.getBehavior();
//                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
//                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
//                ((View)bottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT)
//            }
//        });
        return view;
    }

    public void InitRe(){
        rcv_lcmt.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcv_lcmt.setLayoutManager(linearLayoutManager);
        commentsArrayList=new ArrayList<>();
        adt=new adapter_comment(commentsArrayList,getContext());
        rcv_lcmt.setAdapter(adt);

    }

    private void Anhxa(){
        btnExit = view.findViewById(R.id.bts_close);
        txtInput = view.findViewById(R.id.bts_ipcmt);
        rcv_lcmt = view.findViewById(R.id.bts_list_cmt);
        btnSend = view.findViewById(R.id.bts_sendcmt);
    }

    @Override
    public void onStart() {
        super.onStart();
        view.getLayoutParams().height=-1;
    }
}

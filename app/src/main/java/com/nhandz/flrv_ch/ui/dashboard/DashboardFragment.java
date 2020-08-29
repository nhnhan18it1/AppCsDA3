package com.nhandz.flrv_ch.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.nhandz.flrv_ch.LoginActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.ProfileActivity;
import com.nhandz.flrv_ch.R;

import static com.nhandz.flrv_ch.MainActivity.getSharedPreferences;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private View view;
    private LinearLayout btnProfile;
    private BootstrapCircleThumbnail Avt;
    private BootstrapButton btnLogOut;
    private TextView txtname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.page_menu_fragment, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        view=root;
        anhxa();
        return root;
    }

    private void anhxa() {
        btnProfile=view.findViewById(R.id.btn_profile);
        Avt = view.findViewById(R.id.menu_Avt);
        btnLogOut = view.findViewById(R.id.menu_btnLogOut);
        txtname = view.findViewById(R.id.menu_name);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        Glide.with(getContext())
                .load(MainActivity.serverImg+MainActivity.OnAccount.getAvt())
                .into(Avt);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = MainActivity.getSharedPreferences().edit();
                editor.putString("ID","");
                editor.apply();
                MainActivity.OnAccount=null;
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        txtname.setText(MainActivity.OnAccount.getName());
    }
}

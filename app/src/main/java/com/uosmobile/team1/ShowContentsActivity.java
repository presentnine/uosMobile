package com.uosmobile.team1;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.uosmobile.team1.support.PermissionSupport;

public class ShowContentsActivity extends AppCompatActivity {

    private PermissionSupport permission;

    ContentsTextFragment contentsTextFragment;
    ContentsDrawingFragment contentsDrawingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contents);

        contentsTextFragment = new ContentsTextFragment();
        contentsDrawingFragment = new ContentsDrawingFragment();

        TabLayout tabLayout = findViewById(R.id.ShowContentsTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeFragment(pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        permissionCheck();

        getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, contentsTextFragment).commit();
    }

    private void changeFragment(int pos) {
        if(pos ==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, contentsTextFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, contentsDrawingFragment).commit();
        }
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT>=23){
            permission = new PermissionSupport(this, this);

            if(!permission.checkPermission()){
                permission.requestPermisssion();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermisssion();
        }
    }
}
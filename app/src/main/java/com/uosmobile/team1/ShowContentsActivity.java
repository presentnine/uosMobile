package com.uosmobile.team1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.uosmobile.team1.support.PermissionSupport;

public class ShowContentsActivity extends AppCompatActivity {

    private PermissionSupport permission;
    String contentsName;

    static ContentsTextFragment contentsTextFragment= new ContentsTextFragment();
    static ContentsDrawingFragment contentsDrawingFragment= new ContentsDrawingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contents);

        //메인에서 넘어온 컨텐츠 이름 받기
        Intent intent = getIntent();
        contentsName = intent.getStringExtra("contentsName");
        Bundle bundle = new Bundle();
        bundle.putString("contentsName", contentsName);
        contentsTextFragment.setArguments(bundle);

        //상단탭 리스너 설정
        TabLayout tabLayout = findViewById(R.id.ShowContentsTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        //권한 체크
        permissionCheck();

        //텍스트 프래그먼트 우선 배치
        getSupportFragmentManager().beginTransaction().replace(R.id.ContentsContainer, contentsTextFragment).commit();
    }

    private void changeFragment(int pos) {//프래그먼트 교체
        if(pos ==0){
            Bundle bundle = new Bundle();
            bundle.putString("contentsName", contentsName);
            contentsTextFragment.setArguments(bundle);
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
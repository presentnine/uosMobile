package com.uosmobile.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uosmobile.team1.support.PermissionSupport;

public class MainActivity extends AppCompatActivity {

    private PermissionSupport permission;
    Button button;
    Button tempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();

        //컨텐츠 넘어가는 버튼
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowContentsActivity.class);
                intent.putExtra("contentsName","개구리 왕자");
                startActivity(intent);
            }
        });

        //예외용 버튼
        tempButton = findViewById(R.id.tempButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowContentsActivity.class);
                intent.putExtra("contentsName","임시 텍스트");
                startActivity(intent);
            }
        });
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
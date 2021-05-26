package com.uosmobile.team1;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.IOException;

import static com.uosmobile.team1.ShowContentsActivity.contentsDrawingFragment;

public class ContentsTextFragment extends Fragment {

    TextView ConTentsTextTextView;
    Button ContentsTextPrevButton,ContentsTextNextButton;
    int page;//이후 db나 메모리에 저장된 마지막 페이지를 불러온다.
    int contentsLastPage=18;
    int readLastPage;
    String sysDir; //번들로 컨텐츠 이름 받아온다
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contents_text, container, false);
        ConTentsTextTextView = v.findViewById(R.id.ContentsTextTextView);
        ContentsTextPrevButton = v.findViewById(R.id.ContentsTextPrevButton);
        ContentsTextNextButton = v.findViewById(R.id.ContentsTextNextButton);
        page =1;

        //상단 액티비티에서 콘텐츠 이름 받아옴
        Bundle bundle = getArguments();
        String contentsName = bundle.getString("contentsName");
        sysDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" +contentsName;
        readContentsPage();

        //이전 버튼
        ContentsTextPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --page;
                if(page==0){
                    Toast.makeText(getContext(), "첫 페이지입니다.",Toast.LENGTH_SHORT).show();
                    page = 1;
                }else{
                    readContentsPage();
                }
            }
        });

        //다음 버튼
        ContentsTextNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++page;
                if(page>contentsLastPage){
                    Toast.makeText(getContext(), "마지막 페이지입니다.",Toast.LENGTH_SHORT).show();
                    page = 18;
                }else{
                    readContentsPage();
                }
            }
        });

        return v;
    }

    private void readContentsPage() {//페이지 읽기
        try {
            FileInputStream inFs = new FileInputStream(sysDir + "/" + page + ".txt");
            byte[] txt = new byte[inFs.available()];
            inFs.read(txt);
            ConTentsTextTextView.setText(new String(txt));
            inFs.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "READ 오류",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {//탭 이동 간 페이지 데이터 공유
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putString("nowPage",String.valueOf(page));

        contentsDrawingFragment.setArguments(bundle);
    }
}
package com.uosmobile.team1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.uosmobile.team1.DB.DBHelper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import static com.uosmobile.team1.ShowContentsActivity.contentsDrawingFragment;

public class ContentsTextFragment extends Fragment {

    TextView ConTentsTextTextView;
    Button ContentsTextPrevButton,ContentsTextNextButton;
    int page;//이후 sqlite를 통해 컨텐츠 별 마지막 페이지 저장 후 이를 불러온다.
    int contentsTotalPage; //이 또한 db를 통해 마지막을 읽어옴
    String sysDir; //번들로 컨텐츠 이름 받아온다
    MediaPlayer mediaPlayer;
    Button goToQuizButton;
    DBHelper db;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contents_text, container, false);
        ConTentsTextTextView = v.findViewById(R.id.ContentsTextTextView);
        ContentsTextPrevButton = v.findViewById(R.id.ContentsTextPrevButton);
        ContentsTextNextButton = v.findViewById(R.id.ContentsTextNextButton);
        goToQuizButton = v.findViewById(R.id.goToQuizButton);
        db = new DBHelper(getContext());

        //상단 액티비티에서 콘텐츠 이름 받아옴
        Bundle bundle = getArguments();
        String contentsName = bundle.getString("contentsName");
        
        //콘텐츠 이름 바탕 필요 변수 설정
        sysDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Contents/" +contentsName;
        //page = 1; //db를 통해 마지막 페이지 읽어와야 하는 부분
        //contentsTotalPage =  18;//db를 통해 콘텐츠의 끝 페이지를 읽어와야 하는 부분
        contentsTotalPage=checkTotalPage(sysDir);
        
        //초기화
        readContentsPage();
        playSoundTrack();

        //이전 버튼
        ContentsTextPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --page;
                mediaPlayer.stop();
                if(page==0){
                    Toast.makeText(getContext(), "첫 페이지입니다.",Toast.LENGTH_SHORT).show();
                    page = 1;
                } else if(page == contentsTotalPage -1){
                    goToQuizButton.setVisibility(View.GONE);
                    readContentsPage();
                    playSoundTrack();
                }else{
                    readContentsPage();
                    playSoundTrack();
                }
            }
        });

        //다음 버튼
        ContentsTextNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++page;
                mediaPlayer.stop();
                if(page> contentsTotalPage){
                    Toast.makeText(getContext(), "마지막 페이지입니다.",Toast.LENGTH_SHORT).show();
                    page = contentsTotalPage;

                }else if(page == contentsTotalPage){
                    goToQuizButton.setVisibility(View.VISIBLE);
                    readContentsPage();
                    playSoundTrack();
                }else{
                    readContentsPage();
                    playSoundTrack();
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

    private void playSoundTrack(){
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(sysDir + "/" + page + ".mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch(IOException e){
        }
    }

    @Override
    public void onPause() {//탭 이동 간 페이지 데이터 공유
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putString("nowPage",String.valueOf(page));

        contentsDrawingFragment.setArguments(bundle);
    }

    private int checkTotalPage(String path){
        File f = new File(path);
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".txt");
            }
        });
        System.out.println("files = " + files.length);
        return files.length;
    }
}
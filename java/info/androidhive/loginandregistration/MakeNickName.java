package info.androidhive.loginandregistration;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import info.androidhive.loginandregistration.manager.ParentActivity;

public class MakeNickName extends  ParentActivity implements OnItemSelectedListener {
    String str1;//이메일
    String str2;//비밀번호
    String str3;//꽃이름
    int num = 0;//스피너에서 누른 포지션

    ImageView imView3;
    public Context mContext3 = null;

    //꽃에 해당하는 이미지
    int img[] ={
            R.mipmap.default_flower,
            R.mipmap.rose,
            R.mipmap.lily,
            R.mipmap.babybreath,
            R.mipmap.narcissus,
            R.mipmap.daisy,
            R.mipmap.sunflower
    };
    //꽃 이름
    String flowerName[] = {
            "[꽃이름]",
            "장미",
            "백합",
            "안개꽃",
            "수선화",
            "데이지",
            "해바라기"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_nickname);

        //앞페이지에서 intent통해 가지고온 정보를 함수로
        getData();

        //닉네임 만들기 두번째 페이지로 이동
        mContext3 = this;
        imView3 = (ImageView)findViewById(R.id.btn_next2);
        imView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num != 0){//꽃이름을 선택하였으면
                    Intent intent2 = new Intent(mContext3, MakeNickName2.class);
                    intent2.putExtra("Email", str1);//이메일
                    intent2.putExtra("Password", str2);//비밀번호
                    intent2.putExtra("FlowerName", str3);//꽃이름
                    intent2.putExtra("FlowerNum",num);//꽃 위치
                    startActivity(intent2);
                }else{//꽃이름을 선택하지 않았으면
                    //꽃이름을 입력해주세요 Toast메시지 출력
                    Toast toast = Toast.makeText(getApplicationContext(), "꽃을 선택해주세요",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_make_nickname, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Spinner
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        //어댑터 생성
        //리스트 추가 및 리스트 불러오기
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this,R.array.flower,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //스피터와 어댑터 연결
        spinner1.setAdapter(adapter1);

        //이벤트를 일으킨 위젯과 리스너 연결
        spinner1.setOnItemSelectedListener(this);

    }//onCreate()



    private void getData(){
        //EmailJoin으로부터 값 가지고 오기
        Intent intent = getIntent();

        str1 = intent.getExtras().getString("Email");
        str2 = intent.getExtras().getString("Password");
        /* 가지온 값을 해당 xml id정보와 일치하는 부분에 출력하기
        TextView txt1 = (TextView)findViewById(R.id.txt1);
        TextView txt2 = (TextView)findViewById(R.id.txt2);

        txt1.append(str1);
        txt2.append(str2);
        */

    }
    //스피너와 연결되서 이미지 변경함수
    public void changeImg(int position){
        ImageView im = (ImageView)findViewById(R.id.flower_img);
        im.setImageResource(img[position]);
    }
    //스피너와 연결되서 꽃이름 반환 함수
    public String returnFlowerName(int position){

        return flowerName[position];
    }

    //콜백메서드
    //이벤트 등이 발생하면 시스템에 의해 호출되는 메서드
    //아이템을 하나 선택했을 시에 호출
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position !=0){
            str3 = returnFlowerName(position);
            num = position;
            Toast.makeText(this, "[" + str3 + "]"+"선택하였습니다!" , Toast.LENGTH_SHORT).show();
            changeImg(position);
        }else{
            num = position;
            changeImg(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);//누르게되면 아예 첫화면으로 가게되버림
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}


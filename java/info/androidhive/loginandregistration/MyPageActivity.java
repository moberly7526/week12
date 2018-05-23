package info.androidhive.loginandregistration;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.HashMap;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.manager.ParentActivity;

public class MyPageActivity extends ParentActivity implements OnClickListener {

    //AlertDialog관련 변수 생성
    private AlertDialog mDialog = null;
    //설정 버튼 변수
    ImageView setting_intent;
    TextView nickname;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        setLayout();//AlertDialog 관련해서 넣었었음

        //설정 버튼 관련
        setting_intent = (ImageView)findViewById(R.id.setting_img);
        setting_intent.setOnClickListener(this);

        nickname = (TextView)findViewById(R.id.nickname);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // sqllite에서 사용자 정보 가지고 오기
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        // Displaying the user details on the screen
        nickname.setText(name);//sqllite db에서 가져온 name 내용으로 set

        //탭바 구현
        TabHost tab_host = (TabHost) findViewById(R.id.tabhost);
        tab_host.setup();

       TabHost.TabSpec ts1 = tab_host.newTabSpec("tab1");
        ts1.setIndicator("위시리스트");
        ts1.setContent(R.id.tab1);
        tab_host.addTab(ts1);

        TabHost.TabSpec ts2 = tab_host.newTabSpec("tab2");
        ts2.setIndicator("최근 본 향수");
        ts2.setContent(R.id.tab2);
        tab_host.addTab(ts2);

        TabHost.TabSpec ts3 = tab_host.newTabSpec("tab3");
        ts3.setIndicator("내가 쓴 리뷰");
        ts3.setContent(R.id.tab3);
        tab_host.addTab(ts3);
        tab_host.setCurrentTab(0);


        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_mypage, null),
                new android.support.v7.app.ActionBar.LayoutParams(
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }//onCreate()

    //종료하시겠습니까?(AlertDialog)관련 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_delete1:
                mDialog = createDialog();
                mDialog.show();
                break;
            //이런식으로 버튼이 추가되면 case에 넣어주는 듯
            case R.id.my_delete2:
                mDialog = createDialog();
                mDialog.show();
                break;
            case R.id.setting_img:
                // TODO Auto-generated method stub
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                //finish();
                break;
            default:
                break;
        }
    }
    /**
     * base 다이얼로그
     * @return ab
     */
    private AlertDialog createDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("삭제");
        ab.setMessage("정말로 삭제하시겠습니까?");
        ab.setCancelable(false);
        ab.setIcon(getResources().getDrawable(R.drawable.cancel_small));

        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(mDialog);
            }
        });

        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(mDialog);
            }
        });

        return ab.create();
    }

    /**
     * 다이얼로그 종료
     * @param dialog
     */
    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
    /*
     * Layout
     */
    //private Button baseButton = null;
    private ImageView deleteImage = null;
    private ImageView deleteImage2 = null;
    private void setLayout(){
        //baseButton = (Button) findViewById(R.id.my_delete2);
        deleteImage = (ImageView) findViewById(R.id.my_delete1);
        deleteImage2 = (ImageView) findViewById(R.id.my_delete2);

       // baseButton.setOnClickListener(this);
        deleteImage.setOnClickListener(this);
        deleteImage2.setOnClickListener(this);
    }//여기까지 AlertDialog관련 메소드

    //뒤로가기 메소드
    @Override
    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
    //?뒤로가기는 되지만 뒤로가기 후 메인화면에서 뒤로가기를 누르면 종료되지않고 다시 이 페이지로 돌아오는 버그
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);//누르게되면 아예 첫화면으로 가게되버림
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };


}//Activity

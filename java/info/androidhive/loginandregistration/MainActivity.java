package info.androidhive.loginandregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import info.androidhive.loginandregistration.manager.ParentActivity;


public class MainActivity extends ParentActivity {
	/* private SharedPreferences mPref;
        private TextView updateNofiti, notifiSound;*/
	ImageView mypage, dictionary,search,suggest,note;

	///뒤로가기 버튼 이벤트
	private BackPressCloseSystem backPressCloseSystem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//뒤로가기 버튼 이벤트
		backPressCloseSystem= new BackPressCloseSystem(this);
		//스플래시
		// startActivity(new Intent(MainActivity.this, SplashActivity.class));

		//액션바(21이하 버전이라 getActionBar하면 crash현상)
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		//중간정렬을 위한 부분
		actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_main, null),
				new android.support.v7.app.ActionBar.LayoutParams(
						android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
						android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
						Gravity.CENTER
				)
		);
		//홈버튼은 안보이고 뒤로가기 보이게
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		mypage = (ImageView) findViewById(R.id.mypage);
		dictionary = (ImageView) findViewById(R.id.dictionary);
		search = (ImageView) findViewById(R.id.search);
		suggest = (ImageView) findViewById(R.id.recommend);
		note = (ImageView) findViewById(R.id.note);

		//검색페이지
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i4 = new Intent(MainActivity.this, Search.class);
				startActivity(i4);
			}
		});
		//마이페이지
		mypage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2 = new Intent(MainActivity.this, MyWish.class);
				startActivity(i2);
			}
		});

	  //향 백과사전
        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(MainActivity.this,Dictionary.class);
                startActivity(i3);
            }
        });

		//향수추천
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(MainActivity.this,Suggest.class);
                startActivity(i5);
            }
        });

		//시향 기록
		note.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i6 = new Intent(MainActivity.this,Record.class);
				startActivity(i6);

			}
		});


	}//onCreate();


	//뒤로가기 버튼 이벤트
	@Override
	public void onBackPressed(){
		backPressCloseSystem.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}

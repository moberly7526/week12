package info.androidhive.loginandregistration;

import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.manager.ActivityManager;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.androidhive.loginandregistration.manager.ParentActivity;

public class LoginInfoActivity extends ParentActivity {

	private TextView txtName;//사용자 닉네임
	private TextView txtEmail;//이메일
	private Button btnLogout;//로그아웃 버튼

	private ActivityManager am = ActivityManager.getInstance();

	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_info);

		//액션바
		//홈버튼은 안보이고 뒤로가기 보이게
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// sqllite에서 사용자 정보 가지고 오기
		HashMap<String, String> user = db.getUserDetails();


		String name = user.get("name");
		String email = user.get("email");


		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}//onCreate();

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// sqlite에 있는 사용자 디비정보 삭제후, LoginIntro액티비트로
		Intent intent = new Intent(LoginInfoActivity.this, LoginIntro.class);
		startActivity(intent);
		am.finishAllActivity();	// 모든 액티비티 종료.
		//finish();
	}

    //액션바 뒤로가기 구현
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// NavUtils.navigateUpFromSameTask(this);//누르게되면 아예 첫화면으로 가게되버림
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	};
}

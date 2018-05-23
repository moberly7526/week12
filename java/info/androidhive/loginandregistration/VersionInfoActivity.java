package info.androidhive.loginandregistration;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.widget.Button;


public class VersionInfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_info);

		//홈버튼은 안보이고 뒤로가기 보이게
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		//버튼 이벤트
		findViewById(R.id.updateButton).setOnClickListener(

				new Button.OnClickListener(){
					public void onClick(View v){
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/apps")));
					}
				});

	}
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

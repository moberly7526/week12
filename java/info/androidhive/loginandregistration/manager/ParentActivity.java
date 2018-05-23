package info.androidhive.loginandregistration.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class ParentActivity extends ActionBarActivity {

    private ActivityManager am = ActivityManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        am.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        am.removeActivity(this);
    }

    /*
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // 뒤로가기 버튼 클릭시
        Toast.makeText(getBaseContext(), "모든 액티비티 종료.", Toast.LENGTH_SHORT).show();
        am.finishAllActivity();
    }
    */

}

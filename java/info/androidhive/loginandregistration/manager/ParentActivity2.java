package info.androidhive.loginandregistration.manager;


import android.os.Bundle;
import android.preference.PreferenceActivity;


public class ParentActivity2 extends PreferenceActivity{

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
        // �ڷΰ��� ��ư Ŭ����
        Toast.makeText(getBaseContext(), "��� ��Ƽ��Ƽ ����.", Toast.LENGTH_SHORT).show();
        am.finishAllActivity();
    }
    */

}

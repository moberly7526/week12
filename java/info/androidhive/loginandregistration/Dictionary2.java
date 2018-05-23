package info.androidhive.loginandregistration;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;

import info.androidhive.loginandregistration.manager.ParentActivity;

public class Dictionary2 extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);


        //�׼ǹ�(21���� �����̶� getActionBar�ϸ� crash����)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //�߰������� ���� �κ�
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_dictionary, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //Ȩ��ư�� �Ⱥ��̰� �ڷΰ��� �Ⱥ��̰�
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }//onCreate();


    //�׼ǹ� �ڷΰ��� ����
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);//�����ԵǸ� �ƿ� ùȭ������ ���Եǹ���
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
package info.androidhive.loginandregistration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Suzin on 2015-08-22.
 */
public class SuggestResult extends Activity {

   ImageView ll;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_result);

        ll = (ImageView)findViewById(R.id.suggest);
        //던진값을 키값을 통해 받음
        Intent intent = getIntent() ;
        int i = intent.getExtras().getInt("total");
        choice(i);


    }//onCreate();

    public void choice(int total){

        if(total >= 8 && total< 13)//우디

        {
            ll.setImageResource(R.mipmap.woody);
            Toast.makeText(SuggestResult.this, "당신은 우디 계열이 어울립니다!", Toast.LENGTH_LONG).show();

        }
        else if(total >= 13 && total< 22 )//그린
        {
            ll.setImageResource(R.mipmap.green);
            Toast.makeText(SuggestResult.this, "당신은 그린 계열이 어울립니다!", Toast.LENGTH_LONG).show();

        }
        else if(total >= 22 && total< 31 )//시트러스
        {

            ll.setImageResource(R.mipmap.citrus);
            Toast.makeText(SuggestResult.this, "당신은 시트러스 계열이 어울립니다!", Toast.LENGTH_LONG).show();

        }
        else if(total >= 31 && total< 40 ) //프루티
        {
            ll.setImageResource(R.mipmap.fruity);
            Toast.makeText(SuggestResult.this, "당신은 프루티 계열이 어울립니다!", Toast.LENGTH_LONG).show();
        }
        else if(total >= 40 && total< 49 ) //플로럴
        {
            ll.setImageResource(R.mipmap.floral);
            Toast.makeText(SuggestResult.this, "당신은 플로럴 계열이 어울립니다!", Toast.LENGTH_LONG).show();
        }
        else if(total >= 49) //오리엔탈
        {
            ll.setImageResource(R.mipmap.oriental);
            Toast.makeText(SuggestResult.this, "당신은 오리엔탈 계열이 어울립니다!", Toast.LENGTH_LONG).show();
        }
    }
}

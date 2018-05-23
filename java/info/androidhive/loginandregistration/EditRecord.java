package info.androidhive.loginandregistration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class EditRecord extends Activity{
    TextView e1, e2, s;    EditText e3;
    RatingBar r;    Button store;
    RItem origin;
    int p;  long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_record_item);
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int)(display.getWidth()*0.95);
        getWindow().getAttributes().height = (int)(display.getHeight()*0.4);

        store = (Button)findViewById(R.id.ostore);
        e1 = (TextView)findViewById(R.id.obrandname);
        e2 = (TextView)findViewById(R.id.oproductname);
        s = (TextView)findViewById(R.id.oslash);

        r = (RatingBar)findViewById(R.id.orrb);
        e3 = (EditText)findViewById(R.id.orcomment);

        origin = getIntent().getParcelableExtra("position");
        p = getIntent().getIntExtra("p", -1);
        id = getIntent().getLongExtra("dbid", 0);

        //원래 저장된 데이터들 세팅해주기
        e1.setText(origin.getBrandName());
        e2.setText(origin.getProductName());
        r.setRating(origin.getrBar());
        e3.setText(origin.getrEcord());

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //변경된 데이터들 저장해 넘겨주기
                /*item.rBar = r.getRating();
                item.rEcord.replace(item.rEcord, e3.getText().toString());*/
                  //브랜드, 제품명, 이미지 변경 불가
                Intent e = new Intent();

                e.putExtra("newrating", r.getRating());
                e.putExtra("newcomment", e3.getText().toString());
                e.putExtra("p", p);
                e.putExtra("dbid", id);
                setResult(RESULT_OK, e);
                finish();
            }
        });


    }
}

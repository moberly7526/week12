package info.androidhive.loginandregistration;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

public class Suggest extends ActionBarActivity {

    TextView text1, tv;
    ImageView iv;
    EditText edit1;
    Button btnok, btn1, b1;
    RadioGroup rgroup1, rgroup2, rgroup3, rgroup4, rgroup5, rgroup6, rgroup7, rgroup8;
    RadioButton r11, r12, r13, r14, r15, r16, r21, r22, r23, r24, r25, r26, r31, r32, r33, r34, r35, r36;
    RadioButton r41, r42, r43, r44, r45, r46, r51, r52, r53, r54, r55, r56, r61, r62, r63, r64, r65, r66;
    RadioButton r71, r72, r73, r74, r75, r76, r81, r82, r83, r84, r85, r86;

    Integer total;
    int count = 0; //문항 라디오버튼 체크 되었는지 여부 확인하는 카운트

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        btnok = (Button) findViewById(R.id.btn1);

        rgroup1 = (RadioGroup) findViewById(R.id.rgroup1);
        rgroup2 = (RadioGroup) findViewById(R.id.rgroup2);
        rgroup3 = (RadioGroup) findViewById(R.id.rgroup3);
        rgroup4 = (RadioGroup) findViewById(R.id.rgroup4);
        rgroup5 = (RadioGroup) findViewById(R.id.rgroup5);
        rgroup6 = (RadioGroup) findViewById(R.id.rgroup6);
        rgroup7 = (RadioGroup) findViewById(R.id.rgroup7);
        rgroup8 = (RadioGroup) findViewById(R.id.rgroup8);


        r11 = (RadioButton) findViewById(R.id.r1a);
        r12 = (RadioButton) findViewById(R.id.r1b);
        r13 = (RadioButton) findViewById(R.id.r1c);
        r14 = (RadioButton) findViewById(R.id.r1d);
        r15 = (RadioButton) findViewById(R.id.r1e);
        r16 = (RadioButton) findViewById(R.id.r1f);

        r21 = (RadioButton) findViewById(R.id.r2a);
        r22 = (RadioButton) findViewById(R.id.r2b);
        r23 = (RadioButton) findViewById(R.id.r2c);
        r24 = (RadioButton) findViewById(R.id.r2d);
        r25 = (RadioButton) findViewById(R.id.r2e);
        r26 = (RadioButton) findViewById(R.id.r2f);

        r31 = (RadioButton) findViewById(R.id.r3a);
        r32 = (RadioButton) findViewById(R.id.r3b);
        r33 = (RadioButton) findViewById(R.id.r3c);
        r34 = (RadioButton) findViewById(R.id.r3d);
        r35 = (RadioButton) findViewById(R.id.r3e);
        r36 = (RadioButton) findViewById(R.id.r3f);

        r41 = (RadioButton) findViewById(R.id.r4a);
        r42 = (RadioButton) findViewById(R.id.r4b);
        r43 = (RadioButton) findViewById(R.id.r4c);
        r44 = (RadioButton) findViewById(R.id.r4d);
        r45 = (RadioButton) findViewById(R.id.r4e);
        r46 = (RadioButton) findViewById(R.id.r4f);

        r51 = (RadioButton) findViewById(R.id.r5a);
        r52 = (RadioButton) findViewById(R.id.r5b);
        r53 = (RadioButton) findViewById(R.id.r5c);
        r54 = (RadioButton) findViewById(R.id.r5d);
        r55 = (RadioButton) findViewById(R.id.r5e);
        r56 = (RadioButton) findViewById(R.id.r5f);

        r61 = (RadioButton) findViewById(R.id.r6a);
        r62 = (RadioButton) findViewById(R.id.r6b);
        r63 = (RadioButton) findViewById(R.id.r6c);
        r64 = (RadioButton) findViewById(R.id.r6d);
        r65 = (RadioButton) findViewById(R.id.r6e);
        r66 = (RadioButton) findViewById(R.id.r6f);


        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchResult();//완료버튼을 누르면 해당함수 실행
            }//onClick
        });//setOnClickListener

        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_suggest, null),
                new android.support.v7.app.ActionBar.LayoutParams(
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 안보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }//onCreate();

    public void searchResult(){//if문 돌려서 설문조사의 결과를 계산

        total = 0;
        count = 0;

        switch (rgroup1.getCheckedRadioButtonId()) {
            case R.id.r1a:
                total = total + 1;
                count++;
                break;
            case R.id.r1b:
                total = total + 2;
                count++;
                break;
            case R.id.r1c:
                total = total + 3;
                count++;
                break;
            case R.id.r1d:
                total = total + 4;
                count++;
                break;
            case R.id.r1e:
                total = total + 5;
                count++;
                break;
            case R.id.r1f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup2.getCheckedRadioButtonId()) {
            case R.id.r2a:
                total = total + 1;
                count++;
                break;
            case R.id.r2b:
                total = total + 2;
                count++;
                break;
            case R.id.r2c:
                total = total + 3;
                count++;
                break;
            case R.id.r2d:
                total = total + 4;
                count++;
                break;
            case R.id.r2e:
                total = total + 5;
                count++;
                break;
            case R.id.r2f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup3.getCheckedRadioButtonId()) {
            case R.id.r3a:
                total = total + 1;
                count++;
                break;
            case R.id.r3b:
                total = total + 2;
                count++;
                break;
            case R.id.r3c:
                total = total + 3;
                count++;
                break;
            case R.id.r3d:
                total = total + 4;
                count++;
                break;
            case R.id.r3e:
                total = total + 5;
                count++;
                break;
            case R.id.r3f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup4.getCheckedRadioButtonId()) {
            case R.id.r4a:
                total = total + 1;
                count++;
                break;
            case R.id.r4b:
                total = total + 2;
                count++;
                break;
            case R.id.r4c:
                total = total + 3;
                count++;
                break;
            case R.id.r4d:
                total = total + 4;
                count++;
                break;
            case R.id.r4e:
                total = total + 5;
                count++;
                break;
            case R.id.r4f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup5.getCheckedRadioButtonId()) {
            case R.id.r5a:
                total = total + 1;
                count++;
                break;
            case R.id.r5b:
                total = total + 2;
                count++;
                break;
            case R.id.r5c:
                total = total + 3;
                count++;
                break;
            case R.id.r5d:
                total = total + 4;
                count++;
                break;
            case R.id.r5e:
                total = total + 5;
                count++;
                break;
            case R.id.r5f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup6.getCheckedRadioButtonId()) {
            case R.id.r6a:
                total = total + 1;
                count++;
                break;
            case R.id.r6b:
                total = total + 2;
                count++;
                break;
            case R.id.r6c:
                total = total + 3;
                count++;
                break;
            case R.id.r6d:
                total = total + 4;
                count++;
                break;
            case R.id.r6e:
                total = total + 5;
                count++;
                break;
            case R.id.r6f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup7.getCheckedRadioButtonId()) {
            case R.id.r7a:
                total = total + 1;
                count++;
                break;
            case R.id.r7b:
                total = total + 2;
                count++;
                break;
            case R.id.r7c:
                total = total + 3;
                count++;
                break;
            case R.id.r7d:
                total = total + 4;
                count++;
                break;
            case R.id.r7e:
                total = total + 5;
                count++;
                break;
            case R.id.r7f:
                total = total + 6;
                count++;
                break;
            default:
                break;
        }
        switch (rgroup8.getCheckedRadioButtonId()) {
            case R.id.r8a:
                total = total + 1;
                count++;
                break;
            case R.id.r8b:
                total = total + 2;
                count++;
                break;
            case R.id.r8c:
                total = total + 3;
                count++;
                break;
            case R.id.r8d:
                total = total + 4;
                count++;
                break;
            case R.id.r8e:
                total = total + 5;
                count++;
                break;
            case R.id.r8f:
                total = total + 6;
                count++;
                break;
            default:
              break;
        }
        printResult(total);
        // printResult(count);


    };//searchResult

    public void printResult(int total){//설문으로 날라온 값 받기
        //total별로 넘기는 액티비티를 다르게 해야겠다
        //예를들어서 8이상 13미만의 total이라면, 아로마계열 액티비티로 넘기게

        if(count==8){
            Intent intent1 = new Intent(Suggest.this, SuggestResult.class);
            intent1.putExtra("total", total);
            startActivity(intent1);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "모든 문항의 답을 입력 해 주세요", Toast.LENGTH_SHORT).show();
        }

    };//printResult()

    //액션바 뒤로가기 구현
    @Override
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
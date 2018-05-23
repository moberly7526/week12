package info.androidhive.loginandregistration;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MyHistory extends Activity
						implements View.OnTouchListener,
						CompoundButton.OnCheckedChangeListener {
	//CheckBox checkBox;
    ViewFlipper flipper;
    

    float xAtDown;
    float xAtUp;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_history);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//Toast.makeText(this, "오른쪽으로 넘겨주세요!", Toast.LENGTH_SHORT).show();

        flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        flipper.setOnTouchListener(this);

    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(v != flipper) return false;		
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			xAtUp = event.getX();
			
			if( xAtUp < xAtDown ) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_left_out));

				flipper.showNext();
			}
			else if (xAtUp > xAtDown){
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_right_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		        		R.anim.push_right_out));
		        // ?쟾 view 蹂댁뿬以?
				flipper.showPrevious();			
			}
		}		
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		
		Log.w("checked", Boolean.toString(isChecked));
		
		if(isChecked == true) {
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,
	        		R.anim.push_left_in));
	        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
	        		R.anim.push_left_out));

			flipper.setFlipInterval(3000);
			flipper.startFlipping();
		}
		else 
		{
			flipper.stopFlipping();
		}
	}
}
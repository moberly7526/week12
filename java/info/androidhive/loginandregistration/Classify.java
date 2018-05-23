package info.androidhive.loginandregistration;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

import info.androidhive.loginandregistration.R;

/**
 * Created by Suzin on 2015-08-23.
 */
public class Classify extends Activity {

    ImageView img;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        img = (ImageView)findViewById(R.id.classify);

        Intent intent = getIntent();
        String title= intent.getExtras().getString("title");
        //Toast.makeText(Classify.this, title, Toast.LENGTH_SHORT).show(); //mtitle
        result(title);

    }//onCreate();

    public void result(String title){

        switch(title){

            case "알데히드(Aldehyde)":
                img.setImageResource(R.mipmap.aldehyde);
                //img.setImageDrawable(getResources().getDrawable(R.mipmap.aldehyde));
                break;
            case "아쿠아(Aqua)":
                img.setImageResource(R.mipmap.aqua);
                break;
            case "시프레(Chypre)":
                img.setImageResource(R.mipmap.chypre);
                break;

            case "시트러스(Citrus)":
                img.setImageResource(R.mipmap.citrus);
                break;
            case "플로럴(Floral)":
                img.setImageResource(R.mipmap.floral);
                break;
            case "프루티(Fruity)":
                img.setImageResource(R.mipmap.fruity);
                break;
            case "그루망(Gourmand)":
                img.setImageResource(R.mipmap.gourmand);
                break;
            case "그린(Green)":
                img.setImageResource(R.mipmap.green);
                break;
            case "레더(Leather)":
                img.setImageResource(R.mipmap.leather);
                break;
            case "후제아(Fougere)":
                img.setImageResource(R.mipmap.fougere);
                break;
            case "머스크(Musk)":
                img.setImageResource(R.mipmap.musk);
                break;
            case "오리엔탈(Oriental)":
                img.setImageResource(R.mipmap.oriental);
                break;
            case "파우더리(Powdery)":
                img.setImageResource(R.mipmap.powdery);
                break;
            case  "스파이시(Spicy)":
                img.setImageResource(R.mipmap.spicy);
                break;

            case "우디(Woody)":
                img.setImageResource(R.mipmap.woody);
                break;

            default:
                break;
        }

    }


}

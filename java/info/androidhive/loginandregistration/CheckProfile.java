package info.androidhive.loginandregistration;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by »ï¼ºÂô on 2015-08-24.
 */
public class CheckProfile {

    int i;

    //»ý¼ºÀÚ
    public CheckProfile(){
        super();

    }

    public int isYourFlower(String flowerName){

        switch (flowerName){
            case "rose":
                i = R.mipmap.rose;
                break;
            case "lily":
                i = R.mipmap.lily;
                break;
            case "babybreath":
                i = R.mipmap.babybreath;
                break;
            case "narcissus":
               i = R.mipmap.narcissus;
                break;
            case "daisy":
                i = R.mipmap.daisy;
                break;
            case "sunflower":
               i = R.mipmap.sunflower;
                break;
        }

        return i;

    }//isYourFlower()
}

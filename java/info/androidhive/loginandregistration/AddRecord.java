package info.androidhive.loginandregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddRecord extends Activity{
    EditText e1, e2, e3;    TextView s;
    RatingBar r;    ImageView iv;   Button cancel, store;
    RItem item;
    Bitmap img;
    Bitmap storeimg;
// comment added
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_record_item);
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int)(display.getWidth()*0.95);
        getWindow().getAttributes().height = (int)(display.getHeight()*0.4);

        cancel = (Button)findViewById(R.id.cancel);
        store = (Button)findViewById(R.id.store);
        e1 = (EditText)findViewById(R.id.nbrandname);
        e2 = (EditText)findViewById(R.id.nproductname);
        s = (TextView)findViewById(R.id.slash);
        iv = (ImageView)findViewById(R.id.nrimg);
        r = (RatingBar)findViewById(R.id.nrrb);
        e3 = (EditText)findViewById(R.id.nrcomment);

        //Bitmap bbb = ((BitmapDrawable) iv.getDrawable()).getBitmap(); //카메라 호출해서 찍힌 사진 넘겨주려고했는데 포기..ㅋ
        //카메라 구현시 세번째 인자 변경해주기(★)

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

        //bmp = (BitmapDrawable)getResources().getDrawable(R.drawable.seric);
        //img = bmp.getBitmap();

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent();

                img = ((BitmapDrawable)iv.getDrawable()).getBitmap();

                //비트맵 이미지를 바이트어레이로 변환해 넘겨준다
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imgbyte = stream.toByteArray();

                a.putExtra("a1", e1.getText().toString());
                a.putExtra("a2", e2.getText().toString());
                a.putExtra("a3", imgbyte);
                a.putExtra("a4", r.getRating());
                a.putExtra("a5", e3.getText().toString());

                setResult(RESULT_OK, a);
                finish();
            }
        });//새로운 데이터들 저장해 넘겨주기

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void getPhoto(){
        final String[] choices = {"갤러리", "카메라"};
        new AlertDialog.Builder(this).setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Uri uri = Uri.parse("content://media/external/images/media");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                } //갤러리
                else if(which == 1){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 2);
                } //카메라

                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&!data.equals(null)){
            try{
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                photo = Bitmap.createBitmap(photo);
                storeimg = photo.copy(Bitmap.Config.ARGB_8888, true);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageBitmap(photo);
            } catch (FileNotFoundException e){ e.printStackTrace(); }
            catch (IOException e){ e.printStackTrace(); }
        } else if(requestCode == 2&&!data.equals(null)){
            try{
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                storeimg = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                iv.setImageBitmap(bitmap);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);

            } catch (Exception e){ e.printStackTrace(); }
        }

    }


    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(data.getData() != null){
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                photo = Bitmap.createScaledBitmap(photo, 110, 110, true);
                iv.setImageBitmap(photo);
            }
        } catch (IOException e){ e.printStackTrace(); }

    } //참고 http://javaexpert.tistory.com/611    http://androi.tistory.com/174*/

}



/*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);*/
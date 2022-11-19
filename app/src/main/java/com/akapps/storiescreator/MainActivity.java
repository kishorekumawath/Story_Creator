package com.akapps.storiescreator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    ImageView selectOption,imageView,download;
    CardView layoutView;
    public static final int SELECT_IMAGE_CODE = 1;
    private ScaleGestureDetector scaleGestureDetector ;
    private float factor = 1.0f;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectOption = findViewById(R.id.selectImg);
        imageView = findViewById(R.id.imageView);
        download = findViewById(R.id.download);
        layoutView = findViewById(R.id.editedLayout);
        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(this,new scaleListner());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = Bitmap.createBitmap(layoutView.getWidth(),layoutView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                layoutView.draw(canvas);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                byte[] byteArray = bytes.toByteArray();
                Intent intent = new Intent(MainActivity.this, download_layout.class);
                intent.putExtra("bitmap",byteArray);
                startActivity(intent);

            }
        });
    }
    float x,y;
    float dx,dy;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            x=event.getX();
            y=event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            dx=event.getX()-x;
            dy=event.getY()-y;
            imageView.setX(imageView.getX()+dx);
            imageView.setY(imageView.getY()+dy);
            x=event.getX();
            y = event.getY();
        }

        return super.onTouchEvent(event);
    }
    class scaleListner extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            factor *= detector.getScaleFactor();
            factor = Math.max(0.1f,Math.min(factor,10.f));
            imageView.setScaleX(factor);
            imageView.setScaleY(factor);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            selectOption.setVisibility(View.GONE);
        }
    }
}
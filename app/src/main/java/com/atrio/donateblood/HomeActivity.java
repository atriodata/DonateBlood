package com.atrio.donateblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    ImageView img_drop,img_bgdrop;
    Button btn_donate,btn_recive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        img_drop=(ImageView)findViewById(R.id.img_animation);
        img_bgdrop=(ImageView)findViewById(R.id.img_bgdrop);
        btn_donate=(Button)findViewById(R.id.btn_doner);
        btn_recive=(Button)findViewById(R.id.btn_reciver);
        btn_donate.setVisibility(View.GONE);
        btn_recive.setVisibility(View.GONE);
        img_bgdrop.setVisibility(View.GONE);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.drop);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);
//                TranslateAnimation(0, 0,0, Animation.RELATIVE_TO_PARENT);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(3000);  // animation duration
        animation.setFillAfter(true);
//        animation.setRepeatCount(2);  // animation repeat count
//        animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
//      animation.setFillAfter(true);

        img_drop.startAnimation(animation);
//        img_drop.setVisibility(View.INVISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_drop.setVisibility(View.GONE);
                img_bgdrop.setVisibility(View.VISIBLE);
//                img_bgdrop.startAnimation(myAnim);
                btn_donate.setVisibility(View.VISIBLE);
                btn_donate.startAnimation(myAnim);
                btn_recive.setVisibility(View.VISIBLE);
                btn_recive.startAnimation(myAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

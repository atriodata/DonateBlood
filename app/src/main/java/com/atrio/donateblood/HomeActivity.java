package com.atrio.donateblood;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {
    ImageView img_drop,img_bgdrop;
    Button btn_donate,btn_recive,btn_notify;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        img_drop = (ImageView) findViewById(R.id.img_animation);
        img_bgdrop = (ImageView) findViewById(R.id.img_bgdrop);
        btn_notify = (Button) findViewById(R.id.btn_notify);
        btn_donate = (Button) findViewById(R.id.btn_doner);
        btn_recive = (Button) findViewById(R.id.btn_reciver);
        btn_donate.setVisibility(View.GONE);
        btn_recive.setVisibility(View.GONE);
        img_bgdrop.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dialog = new SpotsDialog(HomeActivity.this, R.style.Custom);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.drop);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);
        animation.setDuration(3000);  // animation duration
        animation.setFillAfter(true);
        img_drop.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_drop.setVisibility(View.GONE);
                img_bgdrop.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(HomeActivity.this, ResigrationActivity.class);
                startActivity(intent);
            }
        });

        btn_recive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    db_instance = FirebaseDatabase.getInstance();
                    db_ref = db_instance.getReference();
                    Query query_id = db_ref.child("Recipient").orderByKey().equalTo(user.getPhoneNumber());
                    query_id.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() != 0) {
                                dialog.dismiss();
                                Intent intent = new Intent(HomeActivity.this, RecipientActivity.class);
                                startActivity(intent);
                            } else {
                                dialog.dismiss();
                                Intent intent = new Intent(HomeActivity.this, RecipentDetailsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

}

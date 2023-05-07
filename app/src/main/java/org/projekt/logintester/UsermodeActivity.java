package org.projekt.logintester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.projekt.logintester.R;

public class UsermodeActivity extends AppCompatActivity {

    private TextView mPersonalName;
    private ImageView mPersonalPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermode);


        Intent intent = getIntent();
        String nickName=intent.getStringExtra("nickName");
        String photoUrl=intent.getStringExtra("photoUrl");

        mPersonalName=findViewById(R.id.personalName);
        mPersonalName.setText(nickName); //Get profile name

        mPersonalPhoto=findViewById(R.id.personalPhoto);
        Glide.with(this).load(photoUrl).into(mPersonalPhoto); //Get profile photo by using glide
    }
}
package com.gachugusville.development.serviced.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Provider;
import com.gachugusville.development.serviced.Utils.Skills;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobPlacementActivity extends AppCompatActivity {

    private Skills skill;
    private Provider provider;
    private String txt_distance;
    private TextView txt_provider_distance, txt_provider_rating, provider_number_of_reviews, txt_skill_job_num,
            txt_provider_experience, txt_provider_certification;
    private CircleImageView provider_profile_pic;
    private AppCompatRatingBar provider_rating_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_placement);
        //Initialize our views
        txt_provider_distance = findViewById(R.id.txt_provider_distance);
        provider_profile_pic = findViewById(R.id.provider_profile_pic);
        provider_rating_bar = findViewById(R.id.provider_rating_bar);
        txt_provider_rating = findViewById(R.id.txt_provider_rating);
        provider_number_of_reviews = findViewById(R.id.provider_number_of_reviews);
        txt_skill_job_num = findViewById(R.id.txt_skill_job_num);
        txt_provider_experience = findViewById(R.id.txt_provider_experience);
        txt_provider_certification = findViewById(R.id.txt_provider_certification);


        //Get provider and skill Details
        getPassedDetails();
        //Get provider details

        //set up View values
        setUpViews();

    }

    private void getPassedDetails() {
        Intent intent = getIntent();
        skill = (Skills) intent.getSerializableExtra("skill_data");
        provider = (Provider) intent.getSerializableExtra("provider_details");
        txt_distance = intent.getStringExtra("distance");
    }

    private void setUpViews() {
        //Distance
        txt_provider_distance.setText(txt_distance);
        //Back button
        findViewById(R.id.job_placement_back_button).setOnClickListener(v -> JobPlacementActivity.super.onBackPressed());
        //Profile picture

        //Rating
        provider_rating_bar.setRating(provider.getRating());
        txt_provider_rating.setText(String.valueOf(provider.getRating()));
        provider_number_of_reviews.setText(MessageFormat.format("({0})", provider.getNumber_of_reviews()));

        Picasso.get()
                .load(provider.getProfile_pic_url())
                .into(provider_profile_pic);

        //Skill details
        txt_skill_job_num.setText(String.valueOf(skill.getSkill_jobs_number()));
        //TODO configure these two
        txt_provider_experience.setText(String.valueOf(5));
        txt_provider_certification.setText(String.valueOf(4));


    }
}
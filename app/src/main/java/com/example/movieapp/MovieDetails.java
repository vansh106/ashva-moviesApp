package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Models.MovieModel;

public class MovieDetails extends AppCompatActivity {
    ImageView imageView;
    TextView title,overview;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        imageView=findViewById(R.id.imageView_details);
        title=findViewById(R.id.textView_title_details);
        ratingBar=findViewById(R.id.rating_bar_details);
        overview=findViewById(R.id.textView_desc_details);

        GetDataFromIntent();
    }

    @SuppressLint("SetTextI18n")
    private void GetDataFromIntent() {
        if(getIntent().hasExtra("movie")){
            MovieModel movieModel=getIntent().getParcelableExtra("movie");
            title.setText(movieModel.getTitle());
            overview.setText(movieModel.getOverview());
            ratingBar.setRating(movieModel.getVote_average());
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path()).into(imageView);
        }
    }
}
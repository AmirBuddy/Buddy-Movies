package com.example.buddymovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.buddymovies.Models.MovieModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MovieDetailsActivity extends AppCompatActivity {

    ShapeableImageView imageViewDetailPoster;
    MaterialTextView textViewDetailTitle;
    MaterialTextView textViewDetailReleaseYear;
    MaterialTextView textViewDetailGenre;
    MaterialTextView textViewDetailDescription;
    LinearLayout detailBackground;
    MovieDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedMovie")) {
            MovieModel selectedMovie = (MovieModel) intent.getSerializableExtra("selectedMovie");

            initializeViews();
            fixTransitions();

            setListenerForLiveData();

            assert selectedMovie != null;
            createPage(selectedMovie);
        }
    }
    private void initializeViews() {
        imageViewDetailPoster = findViewById(R.id.imageViewDetailPoster);
        textViewDetailTitle = findViewById(R.id.textViewDetailTitle);
        textViewDetailReleaseYear = findViewById(R.id.textViewDetailReleaseYear);
        textViewDetailGenre = findViewById(R.id.textViewDetailGenre);
        textViewDetailDescription = findViewById(R.id.textViewDetailDescription);
        detailBackground = findViewById(R.id.detailBackground);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
    }

    private void fixTransitions() {
        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private void fetchAndShowDescription(MovieModel movieModel) {
        // Fetch movie details using ViewModel
        viewModel.fetchMovieDescription(movieModel.getId());
    }

    private void setListenerForLiveData() {
        // Observe changes in the LiveData
        viewModel.getDescriptionLiveData().observe(this, description -> {
            textViewDetailDescription.setText(description);
        });
    }

    private void createPage(MovieModel selectedMovie) {
        Glide.with(this).load(selectedMovie.getImgSrc().toString()).into(imageViewDetailPoster);
        textViewDetailTitle.setText(selectedMovie.getTitle());
        textViewDetailReleaseYear.setText(selectedMovie.getReleaseYear());
        textViewDetailGenre.setText(selectedMovie.getGenre());

        fetchAndShowDescription(selectedMovie);

        Glide.with(this)
                .asBitmap()
                .load(selectedMovie.getImgSrc().toString())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the bitmap as the background of the LinearLayout
                        detailBackground.setBackground(new BitmapDrawable(getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle any cleanup if needed
                    }
                });
    }
}

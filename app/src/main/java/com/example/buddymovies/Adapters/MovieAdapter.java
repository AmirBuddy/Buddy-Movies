package com.example.buddymovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buddymovies.MovieDetailsActivity;
import com.example.buddymovies.Models.MovieModel;
import com.example.buddymovies.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Objects;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<MovieModel> movieList;

    public MovieAdapter(Context context, ArrayList<MovieModel> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieModel selectedMovie = movieList.get(position);
        holder.title.setText(selectedMovie.getTitle());
        holder.releaseYear.setText(selectedMovie.getReleaseYear());
        holder.genre.setText(selectedMovie.getGenre());
        Glide.with(context).load(selectedMovie.getImgSrc().toString()).into(holder.imgSrc);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("selectedMovie", selectedMovie);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.imgSrc, Objects.requireNonNull(ViewCompat.getTransitionName(holder.imgSrc)));
            context.startActivity(intent, optionsCompat.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView title, releaseYear, genre;
        ShapeableImageView imgSrc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            releaseYear = itemView.findViewById(R.id.textViewReleaseYear);
            genre = itemView.findViewById(R.id.textViewGenre);
            imgSrc = itemView.findViewById(R.id.imageViewPoster);
        }
    }
}

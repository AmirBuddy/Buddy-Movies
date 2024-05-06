package com.example.buddymovies.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddymovies.Models.ContainerModel;
import com.example.buddymovies.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ContainerModel> containerList;

    public ContainerAdapter(Context context, ArrayList<ContainerModel> containerList) {
        this.context = context;
        this.containerList = containerList;
    }

    @NonNull
    @Override
    public ContainerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.container_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ContainerAdapter.ViewHolder holder, int position) {
        holder.containerTitle.setText(containerList.get(position).getTitle());

        MovieAdapter movieAdapter = new MovieAdapter(context, containerList.get(position).getMovieList());
        holder.container.setAdapter(movieAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.container.setLayoutManager(linearLayoutManager);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return containerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView containerTitle;
        RecyclerView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerTitle = itemView.findViewById(R.id.containerTitle);
            container = itemView.findViewById(R.id.containerBox);
        }
    }
}

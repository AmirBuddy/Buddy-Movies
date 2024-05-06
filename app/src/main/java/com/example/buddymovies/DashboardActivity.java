package com.example.buddymovies;

import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddymovies.Adapters.ContainerAdapter;
import com.example.buddymovies.Models.ContainerModel;
import com.example.buddymovies.Models.MovieModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    protected static final int[] GENRE_IDS = {2, 3, 9, 17, 15};
    private static final Map<Integer, String> GENRE_NAMES;
    static {
        GENRE_NAMES = new HashMap<>();
        GENRE_NAMES.put(2, "Drama");
        GENRE_NAMES.put(3, "Action");
        GENRE_NAMES.put(9, "Comedy");
        GENRE_NAMES.put(17, "Horror");
        GENRE_NAMES.put(15, "Animation");
    }

    private MaterialToolbar materialToolbar;
    private DashboardViewModel viewModel;
    private RecyclerView dashboard;
    private boolean showByGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setSupportActionBar(materialToolbar);
        fixTransitions();

        setListenerForLiveData();
        fetchAndShowMovies(true);
    }

    private void initializeViews() {
        materialToolbar = findViewById(R.id.dashboardToolbar);
        dashboard = findViewById(R.id.dashboard);

        // Initialize ViewModel
        viewModel = DashboardViewModel.getInstance();
    }

    private void fixTransitions() {
        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(materialToolbar, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.genreSort) {
            fetchAndShowMovies(true);
            return true;
        } else if (id == R.id.yearSort) {
            fetchAndShowMovies(false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void fetchAndShowMovies(boolean isByGenre) {
        // Fetch movie lists using ViewModel
        showByGenres = isByGenre;
        viewModel.fetchMovies(showByGenres, GENRE_IDS);
    }

    private void setListenerForLiveData() {
        // Observe changes in the LiveData
        viewModel.getMovieListsLiveData().observe(this, movieLists -> {
            showLists(movieLists, showByGenres);
        });
    }

    private void showLists(ArrayList<ArrayList<MovieModel>> allMovieLists, boolean showByGenres) {
        ArrayList<ContainerModel> containerList = new ArrayList<>();

        if (showByGenres) {
            for (int i = 0; i < allMovieLists.size(); i++) {
                String genreName = getGenreName(GENRE_IDS[i]);
                containerList.add(new ContainerModel(genreName, allMovieLists.get(i)));
            }
        } else {
            containerList.add(new ContainerModel("Newer than 2000", allMovieLists.get(0)));
            containerList.add(new ContainerModel("2000 - 1995", allMovieLists.get(1)));
            containerList.add(new ContainerModel("Older than 1995", allMovieLists.get(2)));
        }

        ContainerAdapter containerAdapter = new ContainerAdapter(this, containerList);
        dashboard.setAdapter(containerAdapter);
        LinearLayoutManager verticalLayout = new LinearLayoutManager(this);
        dashboard.setLayoutManager(verticalLayout);
    }

    private String getGenreName(int genreId) {
        return GENRE_NAMES.get(genreId);
    }
}

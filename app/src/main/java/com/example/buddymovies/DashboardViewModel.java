package com.example.buddymovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.buddymovies.Models.MovieModel;
import com.example.buddymovies.Utils.MovieNetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ArrayList<MovieModel>>> movieListsLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<Boolean, ArrayList<ArrayList<MovieModel>>> cache = new HashMap<>();
    private static DashboardViewModel instance;

    public static synchronized DashboardViewModel getInstance() {
        if (instance == null) {
            instance = new DashboardViewModel();
        }
        return instance;
    }

    LiveData<ArrayList<ArrayList<MovieModel>>> getMovieListsLiveData() {
        return movieListsLiveData;
    }

    public void fetchMovies(boolean showByGenres, int[] genreIDs) {
        if (cache.containsKey(showByGenres)) {
            movieListsLiveData.setValue(cache.get(showByGenres));
            return;
        }

        executorService.execute(() -> {
            ArrayList<ArrayList<MovieModel>> movieLists = MovieNetworkUtils.fetchMoviesLogic(showByGenres, genreIDs);
            cache.put(showByGenres, movieLists);
            movieListsLiveData.postValue(movieLists);
        });
    }

    public void saveToCache(boolean showByGenres, int[] genreIDs) {
        executorService.execute(() -> {
            ArrayList<ArrayList<MovieModel>> movieLists = MovieNetworkUtils.fetchMoviesLogic(showByGenres, genreIDs);
            cache.put(showByGenres, movieLists);
        });
    }
}

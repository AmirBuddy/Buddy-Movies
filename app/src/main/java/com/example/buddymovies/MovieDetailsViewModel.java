package com.example.buddymovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.buddymovies.Utils.MovieNetworkUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> descriptionLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    LiveData<String> getDescriptionLiveData() {
        return descriptionLiveData;
    }

    void fetchMovieDescription(int movieId) {
        executorService.execute(() -> {
            String description = MovieNetworkUtils.fetchDescriptionFromApi(movieId);
            descriptionLiveData.postValue(description);
        });
    }
}

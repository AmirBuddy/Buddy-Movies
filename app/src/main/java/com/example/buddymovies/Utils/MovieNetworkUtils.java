package com.example.buddymovies.Utils;

import android.net.Uri;

import com.example.buddymovies.Models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MovieNetworkUtils {

    protected static ArrayList<MovieModel> parseJson(String json) {
        ArrayList<MovieModel> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject metadata = jsonObject.getJSONObject("metadata");
            int currentPage = metadata.getInt("current_page");
            int pageCount = metadata.getInt("page_count");
            int totalMovies = metadata.getInt("total_count");
            int perPage = metadata.getInt("per_page");
            if (currentPage > pageCount || totalMovies < 1) return movieList;

            JSONArray data = jsonObject.getJSONArray("data");
            int movieInPage = Math.min(totalMovies - ((currentPage - 1) * perPage), perPage);
            for (int i = 0; i < movieInPage; i++) {
                JSONObject movieData = data.getJSONObject(i);

                int id = movieData.getInt("id");
                String title = movieData.getString("title");
                String releaseYear = movieData.getString("year");
                String genre = movieData.getJSONArray("genres").getString(0);
                String imgSrcUrlString = movieData.getString("poster");
                URL imgSrc = new URL(imgSrcUrlString);

                movieList.add(new MovieModel(title, releaseYear, genre, imgSrc, id));
            }
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    protected static String buildApiUrlByGenre(int genreId, int page) {
        return Uri.parse("https://moviesapi.ir/api/v1/genres/")
                .buildUpon()
                .appendPath(String.valueOf(genreId))
                .appendPath("movies")
                .appendQueryParameter("page", String.valueOf(page))
                .build().toString();
    }

    protected static String buildApiUrlForAllMovies(int page) {
        return Uri.parse("https://moviesapi.ir/api/v1/movies")
                .buildUpon()
                .appendQueryParameter("page", String.valueOf(page))
                .build().toString();
    }

    protected static String buildApiUrlForOneMovies(int movieId) {
        return Uri.parse("https://moviesapi.ir/api/v1/movies")
                .buildUpon()
                .appendPath(String.valueOf(movieId))
                .build().toString();
    }

    public static ArrayList<ArrayList<MovieModel>> fetchMoviesLogic(boolean showByGenres, int[] genreIds) {
        ArrayList<ArrayList<MovieModel>> allLists = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(showByGenres ? genreIds.length * 2 : 13);
        ArrayList<CompletableFuture<ArrayList<MovieModel>>> futures;

        if (showByGenres) {
            // Fetch movies by genre asynchronously using CompletableFuture
            futures = Arrays.stream(genreIds)
                    .mapToObj(genreId -> CompletableFuture.supplyAsync(() -> fetchMoviesFromApi(buildApiUrlByGenre(genreId, 1)), executorService)
                            .thenCombineAsync(CompletableFuture.supplyAsync(() -> fetchMoviesFromApi(buildApiUrlByGenre(genreId, 2)), executorService),
                                    (list1, list2) -> {
                                        list1.addAll(list2);
                                        return list1;
                                    }))
                    .collect(Collectors.toCollection(ArrayList::new));

            // Wait for all CompletableFuture to complete and collect the results
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            try {
                allOf.join(); // Wait for all CompletableFuture to complete
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Collect the results into the final list
            allLists = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(list -> !list.isEmpty())
                    .collect(Collectors.toCollection(ArrayList::new));

            // Shutdown the executor service
        } else {
            // Fetch all movies asynchronously using CompletableFuture
            futures = IntStream.rangeClosed(10, 22)
                    .mapToObj(index -> CompletableFuture.supplyAsync(() -> fetchMoviesFromApi(buildApiUrlForAllMovies(index)), executorService))
                    .collect(Collectors.toCollection(ArrayList::new));

            // Wait for all CompletableFuture to complete and collect the results
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            try {
                allOf.join(); // Wait for all CompletableFuture to complete
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Collect the results into a single list
            ArrayList<MovieModel> allMoviesList = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .sorted((movie1, movie2) -> movie2.getReleaseYear().compareTo(movie1.getReleaseYear()))
                    .collect(Collectors.toCollection(ArrayList::new));

            // Create subLists based on release year
            ArrayList<MovieModel> newerThan2000 = new ArrayList<>();
            ArrayList<MovieModel> between1995And2000 = new ArrayList<>();
            ArrayList<MovieModel> olderThan1995 = new ArrayList<>();

            for (MovieModel movie : allMoviesList) {
                String releaseYear = movie.getReleaseYear();
                if (releaseYear != null) {
                    int year = Integer.parseInt(releaseYear);
                    if (year > 2000) {
                        newerThan2000.add(movie);
                    } else if (year >= 1995) {
                        between1995And2000.add(movie);
                    } else {
                        olderThan1995.add(movie);
                    }
                }
            }

            // Add subLists to the result
            allLists.add(newerThan2000);
            allLists.add(between1995And2000);
            allLists.add(olderThan1995);

            // Shutdown the executor service
        }
        executorService.shutdown();

        return allLists;
    }

    protected static String apiCall(String apiUrl) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    protected static ArrayList<MovieModel> fetchMoviesFromApi(String apiUrl) {
        return parseJson(apiCall(apiUrl));
    }

    public static String fetchDescriptionFromApi(int movieId) {
        String apiUrl = buildApiUrlForOneMovies(movieId);
        String description = "description";

        try {
            return new JSONObject(apiCall(apiUrl)).getString("plot");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return description;
    }
}

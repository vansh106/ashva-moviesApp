package com.example.movieapp.utils;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //Search fro movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    // https://api.themoviedb.org/3/movie/550?api_key=62251ad3cb0192d15107e4846e0eaed0

    @GET("/3/movie/{id}?")
    Call<MovieModel> getMovie(
            @Path("id") int id,
            @Query("api_key") String api_key

    );

    //get popular movie
    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    @GET("/3/movie/now_playing")
    Call<MovieSearchResponse> getPopular(
        @Query("api_key") String key,
        @Query("page") int page
    );



}

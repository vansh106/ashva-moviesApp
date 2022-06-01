package com.example.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {
    private static MovieRepository instance;
    private MovieApiClient movieClient;
    private String mQuery;
    private int mPageNUmber;

    public static MovieRepository getInstance() {
        if(instance == null) {
            instance = new MovieRepository();

        }
        return instance;
    }
    private MovieRepository(){
        movieClient = MovieApiClient.getInstance();
    }
    public LiveData<List<MovieModel>> getMovies() {
        return movieClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPop() {
        return movieClient.getMoviesPop();
    }

    public void searchMovieApi(String query, int pageNumber){
        mQuery=query;
        mPageNUmber=pageNumber;
        movieClient.searchMoviesApi(query, pageNumber);

    }
    public void searchMoviePop(int pageNumber){
        mPageNUmber=pageNumber;
        movieClient.searchMoviesApiPop(pageNumber);

    }
    public void searchNextPage(){
        searchMovieApi(mQuery,mPageNUmber+1);
    }

}

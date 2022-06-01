package com.example.movieapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    private MovieRepository movieRepository;
    public MovieListViewModel() {
        movieRepository =  MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPop() {
        return movieRepository.getPop();
    }

    //calling method in View-model
    public void searchMoviesApi(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }
    public void searchMoviePop( int pageNumber) {
        movieRepository.searchMoviePop(pageNumber);
    }
    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}

package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecuters;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    private MutableLiveData<List<MovieModel>> mMovienew, popMovies ; // Live date for search and popular
    private static MovieApiClient instance;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    public static MovieApiClient getInstance(){
        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }
    private MovieApiClient(){
        mMovienew = new MutableLiveData<>();
        popMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovienew;
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return popMovies;
    }

    public void searchMoviesApi(String query , int pageNumber){
        if(retrieveMoviesRunnable!=null){
            retrieveMoviesRunnable=null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);

        final Future myHandler = AppExecuters.getInstance().networkIO().submit(retrieveMoviesRunnable);
        AppExecuters.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);


    }
    public void searchMoviesApiPop(int pageNumber){
        if(retrieveMoviesRunnablePop!=null){
            retrieveMoviesRunnablePop=null;
        }

        retrieveMoviesRunnablePop= new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecuters.getInstance().networkIO().submit(retrieveMoviesRunnablePop);
        AppExecuters.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler2.cancel(true);
            }
        },1000, TimeUnit.MILLISECONDS);


    }
    private class RetrieveMoviesRunnable implements Runnable {
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getMovies(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber==1){
                        mMovienew.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMovienew.getValue();
                        currentMovies.addAll(list);
                        mMovienew.postValue(currentMovies);
                    }

                }else{
                    String error=response.errorBody().string();
                    Log.v("Tag","Error "+error);
                    mMovienew.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovienew.postValue(null);
                Log.v("Tag","error");
            }
            if(cancelRequest){
                return;
            }

        }
        private Call<MovieSearchResponse> getMovies(String query,int pageNumber){
            return Service.getMovieApi().searchMovie(Credentials.API_KEY,query,pageNumber);
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Request");
            cancelRequest=true;
        }
    }
    private class RetrieveMoviesRunnablePop implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {

            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getPop(pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber==1){
                        popMovies.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = popMovies.getValue();
                        currentMovies.addAll(list);
                        popMovies.postValue(currentMovies);
                    }

                }else{
                    String error=response.errorBody().string();
                    Log.v("Tag","Error "+error);
                    popMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                popMovies.postValue(null);
                Log.v("Tag","error");
            }
            if(cancelRequest){
                return;
            }

        }
        private Call<MovieSearchResponse> getPop(int pageNumber){
            return Service.getMovieApi().getPopular(Credentials.API_KEY,pageNumber);
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Request");
            cancelRequest=true;
        }
    }

}

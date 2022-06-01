package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.ViewModels.MovieListViewModel;
import com.example.movieapp.adapters.MovieRecyclerView;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.request.Service;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;
import com.example.movieapp.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView adapter;
    TextView textView;
    

    private MovieListViewModel movieListViewModel;
    boolean isPopular =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView5);
        Toolbar toolbar1 = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar1);
        SetupSearchView();

        //searchView
        SetupSearchView();
        recyclerView=findViewById(R.id.recyclerView);
        movieListViewModel= new ViewModelProvider(MovieListActivity.this).get(MovieListViewModel.class);
        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopularMovies();
        //getting popular movies
        movieListViewModel.searchMoviePop(1);

    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels!=null){
                    for(MovieModel movieModel : movieModels){
                        Log.v("Tag","onChanged: "+movieModel.getTitle());
                        adapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }


    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels!=null){
                    for(MovieModel movieModel : movieModels){
                        Log.v("Tag","onChanged: "+movieModel.getTitle());
                        adapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    private void ConfigureRecyclerView(){
        adapter = new MovieRecyclerView(MovieListActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    private void GetRetrofitResponse() {

        MovieApi movieApi= Service.getMovieApi();
        Call<MovieSearchResponse> responseCall=movieApi.searchMovie(
                Credentials.API_KEY, "Jack Reacher",1);
        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if(response.code() == 200){
                    Log.v("Tag","The response"+response.body().toString());
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
                    for(MovieModel movie: movies){
                        Log.v("Tag","The release date"+movie.getRelease_date());
                    }
                }else{

                    try{
                        Log.v("Tag","error"+response.errorBody().toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }

    private void GetRetrofitResponseAccordingToID(){
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(550,Credentials.API_KEY);
        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if(response.code() == 200){
                    MovieModel movie =response.body();
                    Log.v("Tag","The response "+movie.getTitle());
                }else{
                    try{
                        Log.v("Tag","Error "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this,MovieDetails.class);
        intent.putExtra("movie",adapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }
    //get Data from searchView & query the api to get the results
    private void SetupSearchView() {
        final SearchView searchView=findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textView.setText("Movies Related to \""+query+"\" are:");
                movieListViewModel.searchMoviesApi(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular=false;
            }
        });

    }
}
//making Search with ID https://api.themoviedb.org/3/movie/550?api_key=62251ad3cb0192d15107e4846e0eaed0
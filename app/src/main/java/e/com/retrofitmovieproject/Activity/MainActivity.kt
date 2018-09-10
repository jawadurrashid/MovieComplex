package e.com.retrofitmovieproject.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import e.com.retrofitmovieproject.Adapter.MoviesAdapter
import e.com.retrofitmovieproject.Model.MovieResponse
import e.com.retrofitmovieproject.R
import e.com.retrofitmovieproject.Rest.APIClient
import e.com.retrofitmovieproject.Rest.APIInterface
import retrofit2.Response
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    val APIrequest = "https://api.themoviedb.org/3/movie/550?api_key=a0c12905ea6039a939f8330d7ee93364"
    val TopFiftyMovieRoute = "http://api.themoviedb.org/3/movie/top_rated?api_key=a0c12905ea6039a939f8330d7ee93364"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (API_KEY.isEmpty()) {
            Toast.makeText(applicationContext, "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show()
            return
        }

        val recyclerView = findViewById<View>(R.id.movies_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        apiService
                ?.getTopRatedMovies(API_KEY)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<Response<MovieResponse>> {
                    override fun onError(e: Throwable?) {
                        // Log error here since request failed
                        Log.e(TAG, e.toString())
                    }

                    override fun onNext(response: Response<MovieResponse>?) {
                        val statusCode = response?.code()
                        Log.d(TAG, "Top rated movie response: $statusCode")
                        val movies = response?.body()?.results

                        movies?.let{
                            recyclerView.adapter = MoviesAdapter(it, R.layout.movie_item_holder, applicationContext)
                        }
                    }

                    override fun onCompleted() {
                        Log.d(TAG, "Completed")
                    }
                })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val API_KEY = "a0c12905ea6039a939f8330d7ee93364"
    }
}
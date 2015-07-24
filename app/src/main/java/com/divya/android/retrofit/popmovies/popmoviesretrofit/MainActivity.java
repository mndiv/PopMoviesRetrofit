package com.divya.android.retrofit.popmovies.popmoviesretrofit;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    final String TAG = getClass().getSimpleName();
    final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
    ImageAdapter imageAdapter;
    private GridView gridView;

    public class ImageAdapter extends BaseAdapter {
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();
        private Context mContext;
        private Results mResults;
        //Constructor which takes context as inputs
        public ImageAdapter(Context context, Results results) {
            mContext = context;
            mResults = results;
        }

        @Override
        //return the no. of Views to be displayed
        public int getCount() {
            return mResults.getResults().size();
        }

        @Override
        public Object getItem(int position) {
            return mResults.getResults().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(mContext);
                view.setLayoutParams(new GridView.LayoutParams(220, 220));
            }

            //Picasso easily load album art thumbnails into your views ...
            //Picasso will handle loading the images on a background thread, image decompression and caching the images.
            //Fetches Images and load them into Views
            Picasso.with(mContext).load(mResults.getResults().get(position).getPosterPath()).into(view);
            return view;
        }

    }

          @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String SORT_PARAM = "sort_by";
        final String API_KEY = "api_key";

        String sortBy = "popularity.desc";
        String api_key = "2fc475941d44b7da433d1f18e24e2551";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MOVIES_BASE_URL)
                .build();

        // Callback<Results> res = new Callback<Results>();
        GetMovieDataApi service = restAdapter.create(GetMovieDataApi.class);

        gridView = (GridView)findViewById(R.id.gridview);

        service.getMovieDataFromApi(sortBy, api_key, new Callback<Results>() {
            @Override
            public void success(Results results, Response response) {

                imageAdapter = new ImageAdapter(getApplicationContext(),results);
                imageAdapter.notifyDataSetChanged();
                gridView.setAdapter(imageAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
        Log.d(TAG, "onCreate(): Created");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

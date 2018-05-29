package android.example.com.popularmovies2marzena.adapter;


import android.content.Context;
import android.database.Cursor;
import android.example.com.popularmovies2marzena.R;
import android.example.com.popularmovies2marzena.data.MovieContract.MovieEntry;
import android.example.com.popularmovies2marzena.object.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesMoviesViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    String favouritesMoviePosterUrl;

    //Defining ClickHandler member variable
    final private MovieAdapter.PosterAdapterOnClickHandler mClickHandler;


    /**
     * Constructs a new {@link FavouritesAdapter}.
     *
     * @param context           The context
     * @param cursor            The cursor from which to get the data
     * @param clickHandler      The clickHandler
     */
    public FavouritesAdapter(Context context, Cursor cursor, MovieAdapter.PosterAdapterOnClickHandler clickHandler) {
        mContext = context;
        mCursor = cursor;
        mClickHandler = clickHandler;
    }

    // Inflating a layout from XML and returning new view holder
    @NonNull
    @Override
    public FavouritesMoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.poster_list_item, viewGroup, false);
        rootView.setFocusable(true);
        return new FavouritesMoviesViewHolder(rootView);
    }

    // Populating data into the item through holder
    @Override
    public void onBindViewHolder(FavouritesMoviesViewHolder favouritesMoviesViewHolder, int position) {
        mCursor.moveToPosition(position);

        // Find the columns of favourites movies attributes that we're interested in
        int posterUrlColumnIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER_URL);
        // Read the favourites movies attributes from the Cursor for the current movie
        favouritesMoviePosterUrl = mCursor.getString(posterUrlColumnIndex);

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + favouritesMoviePosterUrl).into(favouritesMoviesViewHolder.posterIv);

    }

    /**
     * This method returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our favourites movies list
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    class FavouritesMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterIv;

        FavouritesMoviesViewHolder(View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            Movie movie = new Movie(mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE)), mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE)), mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE)), mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER_URL)), mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_BACKDROP_URL)), mCursor.getDouble(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)), mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_OVERVIEW)), mCursor.getInt(mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
            mClickHandler.onClick(movie);

        }
    }
    /**
     * Swaps the cursor used by the MovieAdapter for its favourites movies data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the favourites movies data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as MovieAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}

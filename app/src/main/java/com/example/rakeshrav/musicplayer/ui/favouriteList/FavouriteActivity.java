package com.example.rakeshrav.musicplayer.ui.favouriteList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;
import com.example.rakeshrav.musicplayer.ui.splash.AdapterSongs;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavouriteActivity extends BaseActivity implements FavouriteView {

    private static final String TAG = FavouriteActivity.class.getSimpleName();

    @Inject
    FavouriteMvpPresenter<FavouriteView> mPresenter;
    @BindView(R.id.ivAction)
    ImageView ivAction;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivFav)
    ImageView ivFav;
    @BindView(R.id.tvSongsCount)
    TextView tvSongsCount;
    @BindView(R.id.llSongsCount)
    LinearLayout llSongsCount;
    @BindView(R.id.rvFavourites)
    RecyclerView rvFavourites;
    ArrayList<Result> results;
    @BindView(R.id.tvPlaceholder)
    TextView tvPlaceholder;
    @BindView(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, FavouriteActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourites);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        ivFav.setVisibility(View.GONE);
        tvTitle.setText("Favourites");

        results = (ArrayList<Result>) mPresenter.getFavResults();

        if (results != null && !results.isEmpty()) {
            llPlaceHolder.setVisibility(View.INVISIBLE);
            rvFavourites.setVisibility(View.VISIBLE);
            tvSongsCount.setText("  " + results.size());
            setUpRecyclerView();
        }else {
            llPlaceHolder.setVisibility(View.VISIBLE);
            rvFavourites.setVisibility(View.GONE);
        }
    }

    private void setUpRecyclerView() {
        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setItemAnimator(new DefaultItemAnimator());
        final AdapterSongs testAdapter = new AdapterSongs(this, results);
        rvFavourites.setAdapter(testAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                Log.d(TAG, "Swiped.....");

                int index = viewHolder.getAdapterPosition();
                mPresenter.removeFavSongs(results.get(index));
                testAdapter.remove(index);

                tvSongsCount.setText("  " + testAdapter.getItemCount());

                if (testAdapter.getItemCount() == 0){
                    llPlaceHolder.setVisibility(View.VISIBLE);
                    rvFavourites.setVisibility(View.GONE);
                }

            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvFavourites);
    }

    @OnClick({R.id.ivAction, R.id.tvTitle, R.id.ivFav})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAction:
                finish();
                break;
            case R.id.tvTitle:
                break;
            case R.id.ivFav:
                break;
        }
    }
}

package com.example.rakeshrav.musicplayer.ui.favouriteList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setItemAnimator(new DefaultItemAnimator());
        final TestAdapter testAdapter = new TestAdapter();
        rvFavourites.setAdapter(testAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                Log.d(TAG,"Swiped.....");
                if (viewHolder instanceof TestViewHolder) {
                    // get the removed item name to display it in snack bar
                    String name = items.get(viewHolder.getAdapterPosition());

                    // backup of removed item for undo purpose
//                    final Item deletedItem = cartList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    testAdapter.remove(viewHolder.getAdapterPosition());

                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(rvFavourites, name + " removed from cart!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
//                            mAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
            }
        }});
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvFavourites);

    }

    List<String> items;
    String[] itemArr = new String[]{"a", "b", "c", "d","a", "b", "c", "d",
            "a", "b", "c", "d",
            "a", "b", "c", "d"};

    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */
    class TestAdapter extends RecyclerView.Adapter {


        public TestAdapter() {
            items = new ArrayList(Arrays.asList(itemArr));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         *  Utility method to add some rows for testing purposes. You can add rows from the toolbar menu.
         */

        public void remove(int position) {
                items.remove(position);
                notifyItemRemoved(position);
        }

    }

    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    static class TestViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout backgroundView;
        CardView cardView;

        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false));
            backgroundView = (RelativeLayout) itemView.findViewById(R.id.backgroundView);
            cardView = (CardView) itemView.findViewById(R.id.cvForegroundView);
        }

    }

}

package com.example.rakeshrav.musicplayer.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;
import com.example.rakeshrav.musicplayer.ui.favouriteList.FavouriteActivity;
import com.example.rakeshrav.musicplayer.utility.ScreenUtils;
import com.example.rakeshrav.musicplayer.utility.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    SplashMvpPresenter<SplashView> mPresenter;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.ivAction)
    ImageView ivAction;
    @BindView(R.id.cvSearchSplash)
    CardView cvSearchSplash;
    @BindView(R.id.flMainSpalsh)
    FrameLayout flMainSpalsh;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivFav)
    ImageView ivFav;
    @BindView(R.id.cvSearchMain)
    CardView cvSearchMain;
    @BindView(R.id.llMainActivity)
    LinearLayout llMainActivity;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvSongsCount)
    TextView tvSongsCount;
    @BindView(R.id.llSongsCount)
    LinearLayout llSongsCount;
    @BindView(R.id.viewPagerItems)
    ViewPager viewPagerItems;
    @BindView(R.id.llIndicators)
    LinearLayout llIndicators;
    int numberItemToFit = 0;
    int currentIndicator = 0;
    int prevIndicator = 0;
    @BindView(R.id.tvPlaceholder)
    TextView tvPlaceholder;
    @BindView(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    private SearchResultsPagerAdapter pagerAdapter;
    private int sizeToolbar = 60;
    private int sizeSearch = 60;
    private int sizeCount = 40;
    private int sizeIndicators = 50;
    //layout size all in dp
    private int layoutSize = 70;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        calculateListSize();
        setUp();

//        mPresenter.getSongList("", "");
    }

    private void calculateListSize() {
        int screenSize = ScreenUtils.getScreenHeight(this);
        int totalSizeOccupied = sizeToolbar + sizeCount + sizeIndicators + sizeSearch;

        int availabelSize = screenSize - ViewUtils.dpToPx(totalSizeOccupied);

        numberItemToFit = availabelSize / ViewUtils.dpToPx(layoutSize);

        Log.d(TAG, "size available for search result : " + availabelSize + ", Items can be fit in that layout : "
                + numberItemToFit);
    }

    @Override
    protected void setUp() {
        ivAction.setImageResource(R.drawable.shape);

        cvSearchSplash.setVisibility(View.INVISIBLE);
        ivLogo.animate().translationYBy(ViewUtils.dpToPx(100)).setDuration(0).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivLogo.animate().translationYBy(-ViewUtils.dpToPx(100)).setDuration(500).start();

                cvSearchSplash.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in);
                cvSearchSplash.startAnimation(animation);
            }
        }, 2000);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "search text : " + editable.toString());

                searchForSongs(editable.toString());
            }
        });
    }

    private void searchForSongs(final String s) {
        if (isNetworkConnected()){
            mPresenter.getSongList(s, "");
        }else {
            showErrorDialog("No Internet Connection Available!", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissErrDialog();
                    searchForSongs(s);
                }
            });
        }
    }

    @OnClick(R.id.cvSearchSplash)
    public void onViewClicked() {

        cvSearchSplash.animate().translationYBy(-(
                (ScreenUtils.getScreenHeight(this) / 2)
                        -
                        ViewUtils.dpToPx(50))
        ).setDuration(500).start();

        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flMainSpalsh.setVisibility(View.GONE);
                etSearch.requestFocus();
                showKeyboard(etSearch);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        flMainSpalsh.startAnimation(fadeOut);

        llMainActivity.setVisibility(View.VISIBLE);
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        llMainActivity.startAnimation(fadein);
    }

    @Override
    public void populateData(ItunesData itunesData) {

        if (itunesData.getResultCount() > 0) {

            tvSongsCount.setText("  " + String.valueOf(itunesData.getResultCount()));

            int pages = itunesData.getResultCount() / numberItemToFit;

            if (itunesData.getResultCount() % numberItemToFit != 0) {
                pages++;
            }

            Log.d(TAG, "pages : " + pages);
            makeBottomBar(pages);

            viewPagerItems.setVisibility(View.VISIBLE);
            llIndicators.setVisibility(View.VISIBLE);
            llPlaceHolder.setVisibility(View.INVISIBLE);
            viewPagerItems.setOffscreenPageLimit(pages);
            pagerAdapter = new SearchResultsPagerAdapter(getSupportFragmentManager(), pages, itunesData);

            viewPagerItems.setAdapter(pagerAdapter);

            viewPagerItems.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    updateIndicators(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {
            Snackbar.make(llMainActivity, "No Results found!", 2000);
            tvSongsCount.setText("  0");
            llPlaceHolder.setVisibility(View.VISIBLE);
            tvPlaceholder.setText("No Results found, Taste some other search");
            viewPagerItems.setVisibility(View.GONE);
            llIndicators.setVisibility(View.INVISIBLE);
        }
    }

    private void makeBottomBar(int pages) {
        llIndicators.removeAllViews();

        for (int i = 0; i < pages; i++) {
            ImageView imageView = new ImageView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dpToPx(30), ViewUtils.dpToPx(30));
            imageView.setLayoutParams(params);

            if (i == 0) {
                imageView.setImageResource(R.drawable.rectangle_4);
            } else {
                imageView.setImageResource(R.drawable.rectangle_4_copy);
            }

            imageView.setPadding(ViewUtils.dpToPx(4), 0, ViewUtils.dpToPx(4), 0);

            llIndicators.addView(imageView);
        }
    }

    private void updateIndicators(int pos) {

        prevIndicator = currentIndicator;
        currentIndicator = pos;

        ImageView imageView = (ImageView) llIndicators.getChildAt(prevIndicator);
        imageView.setImageResource(R.drawable.rectangle_4_copy);

        ImageView imageViewCurr = (ImageView) llIndicators.getChildAt(currentIndicator);
        imageViewCurr.setImageResource(R.drawable.rectangle_4);
    }

    @OnClick(R.id.ivFav)
    public void onViewClickedFAV() {
        Intent intent = FavouriteActivity.getStartIntent(this);
        startActivity(intent);
    }

    private class SearchResultsPagerAdapter extends FragmentStatePagerAdapter {

        private int pages;
        private ItunesData itunesData;
        private int start = 0;

        SearchResultsPagerAdapter(FragmentManager fm, int pages, ItunesData itunesData) {
            super(fm);
            this.pages = pages;
            this.itunesData = itunesData;
        }

        @Override
        public Fragment getItem(int position) {

            ArrayList<Result> results = getListItems();
            return FragmentSearchResult.getInstance(numberItemToFit, results);
        }

        ArrayList<Result> getListItems() {

            ArrayList<Result> results = new ArrayList<>();

            List<Result> itunesResult = itunesData.getResults();

            int end = numberItemToFit + start;

            if (end > itunesResult.size()) {
                end = itunesResult.size();
            }

            for (int i = start; i < end; i++) {
                results.add(itunesResult.get(start));
                start++;
            }
            return results;
        }

        @Override
        public int getCount() {
            return pages;
        }
    }
}

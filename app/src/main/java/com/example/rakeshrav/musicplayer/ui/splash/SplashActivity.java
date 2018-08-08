package com.example.rakeshrav.musicplayer.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
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
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;
import com.example.rakeshrav.musicplayer.utility.ScreenUtils;
import com.example.rakeshrav.musicplayer.utility.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();
    int listSize = 24;

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

        pagerAdapter = new SearchResultsPagerAdapter(getSupportFragmentManager());
        viewPagerItems.setAdapter(pagerAdapter);
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

    private class SearchResultsPagerAdapter extends FragmentStatePagerAdapter {
        public SearchResultsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentSearchResult.getInstance(numberItemToFit);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}

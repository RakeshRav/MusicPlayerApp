package com.example.rakeshrav.musicplayer.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.utility.ViewUtils;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentSearchResult extends Fragment {

    private static final String TAG = FragmentSearchResult.class.getSimpleName();
    @BindView(R.id.llSearchResults)
    LinearLayout llSearchResults;
    Unbinder unbinder;
    private View rootView;
    int itemCount;

    public static FragmentSearchResult getInstance(int itemCount) {
        FragmentSearchResult fragmentSearchResult = new FragmentSearchResult();
        fragmentSearchResult.itemCount = itemCount;

        return fragmentSearchResult;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_search_items, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewInLL();
            }
        },500);
        return rootView;
    }

    private void setViewInLL() {

        llSearchResults.removeAllViews();

        for (int i = 0; i < itemCount; i++){
            Log.d(TAG,"i = "+i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dpToPx(70));
            itemView.setLayoutParams(params);

            llSearchResults.addView(itemView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

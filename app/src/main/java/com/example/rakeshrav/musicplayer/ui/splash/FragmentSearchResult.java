package com.example.rakeshrav.musicplayer.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.utility.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentSearchResult extends Fragment {

    private static final String TAG = FragmentSearchResult.class.getSimpleName();
    Unbinder unbinder;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    private View rootView;
    int itemCount;
    private ArrayList<Result> results;

    public static FragmentSearchResult getInstance(int itemCount, ArrayList<Result> results) {
        FragmentSearchResult fragmentSearchResult = new FragmentSearchResult();
        fragmentSearchResult.itemCount = itemCount;
        fragmentSearchResult.results = results;
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
        }, 500);
        return rootView;
    }

    private void setViewInLL() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(manager);

        rvItems.setAdapter(new AdapterSongs(getContext(), results));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

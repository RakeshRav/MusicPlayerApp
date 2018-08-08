package com.example.rakeshrav.musicplayer.ui.splash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;

import java.util.ArrayList;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.SongsSearchViewHolder>{

    private Context context;
    private ArrayList<Result> results;
    public AdapterSongs(Context context, ArrayList<Result> results){
        this.context = context;
        this.results  = results;
    }

    @Override
    public SongsSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongsSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongsSearchViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class SongsSearchViewHolder extends RecyclerView.ViewHolder{

        public SongsSearchViewHolder(View itemView) {
            super(itemView);
        }
    }
}

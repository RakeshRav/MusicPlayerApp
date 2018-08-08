package com.example.rakeshrav.musicplayer.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.MusicPlayerApp;
import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.player.PlayerActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.SongsSearchViewHolder> {

    private Context context;
    private ArrayList<Result> results;

    public AdapterSongs(Context context, ArrayList<Result> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public SongsSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongsSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongsSearchViewHolder holder, final int position) {

        holder.tvAlbumName.setText(results.get(position).getCollectionName());
        holder.tvArtistName.setText(results.get(position).getArtistName());
        holder.tvSongName.setText(results.get(position).getTrackName());

        Picasso.with(context).load(results.get(position).getArtworkUrl100()).into(holder.ivImage);

        holder.flMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = PlayerActivity.getStartIntent(context);

                String json = new Gson().toJson(results.get(position));
                intent.putExtra(PlayerActivity.RESULT, json);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void remove(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }


    public class SongsSearchViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvSongName;
        TextView tvArtistName;
        TextView tvAlbumName;
        FrameLayout flMainItem;

        public RelativeLayout backgroundView;
        public CardView cardView;

        public SongsSearchViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            flMainItem = itemView.findViewById(R.id.flMainItem);

            backgroundView = (RelativeLayout) itemView.findViewById(R.id.backgroundView);
            cardView = (CardView) itemView.findViewById(R.id.cvForegroundView);
        }
    }
}

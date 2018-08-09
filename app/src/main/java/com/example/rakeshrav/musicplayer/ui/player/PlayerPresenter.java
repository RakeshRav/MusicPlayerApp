package com.example.rakeshrav.musicplayer.ui.player;

import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.logging.Handler;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class PlayerPresenter<V extends PlayerView> extends BasePresenter<V> implements PlayerMvpPresenter<V> {


    private static final String TAG = PlayerPresenter.class.getSimpleName();

    @Inject
    public PlayerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void setFavSongs(Result result) {
            ItunesData itunesData = getDataManager().getFavsResult();

            if  (itunesData == null){
                itunesData = new ItunesData();
                ArrayList<Result> results = new ArrayList<>();

                results.add(result);

                itunesData.setResults(results);
            }else {
                if (itunesData.getResults() == null){
                    ArrayList<Result> results = new ArrayList<>();

                    results.add(result);

                    itunesData.setResults(results);
                }else {

                    for (Result result1: itunesData.getResults()){
                        if (result1.getTrackId().equals(result.getTrackId())) {
                            //notify already exist

                            return;
                        }
                    }
                    itunesData.getResults().add(result);
                }
            }

            getDataManager().setFavsResult(itunesData);
    }

    @Override
    public void removeFavSongs(Result result) {
        ItunesData itunesData = getDataManager().getFavsResult();

        if  (itunesData == null){
            return;
        }else {
            if (itunesData.getResults() == null){
                return;
            }else {
                for (int i = 0; i<itunesData.getResults().size(); i++){
                    if (itunesData.getResults().get(i).getTrackId().equals(result.getTrackId())) {
                        itunesData.getResults().remove(i);
                        break;
                    }
                }
            }
        }

        getDataManager().setFavsResult(itunesData);
    }

    @Override
    public boolean checkSongIsFav(Result result) {
        ItunesData itunesData = getDataManager().getFavsResult();

        if  (itunesData == null){
            return false;
        }else {
            if (itunesData.getResults() == null){
                return false;
            }else {
                for (int i = 0; i<itunesData.getResults().size(); i++){
                    if (itunesData.getResults().get(i).getTrackId().equals(result.getTrackId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

package com.example.rakeshrav.musicplayer.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.forecastData.ForecastData;
import com.example.rakeshrav.musicplayer.data.network.model.forecastData.Forecastday;
import com.example.rakeshrav.musicplayer.utility.CommonUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private Context context;
    private ForecastData forecastData;

    public ForecastAdapter(Context context, ForecastData forecastData) {
        this.context = context;
        this.forecastData = forecastData;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_forecast_tile, parent, false);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {

        Forecastday forecastday = forecastData.getForecast().getForecastday().get(position);

        Double avgTemp =
                ((forecastday.getDay().getMintempC()
                +
                forecastday.getDay().getMaxtempC())/2);

        holder.tvAvgTemp.setText(avgTemp.intValue()+" C");

        holder.tvDate.setText(String.valueOf(CommonUtils.getDayOfWeek(String.valueOf(forecastday.getDate()))));
    }

    @Override
    public int getItemCount() {
        return forecastData.getForecast().getForecastday().size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;

        TextView tvAvgTemp;


        public ForecastViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);

            tvAvgTemp = itemView.findViewById(R.id.tvAvgTemp);
        }
    }
}

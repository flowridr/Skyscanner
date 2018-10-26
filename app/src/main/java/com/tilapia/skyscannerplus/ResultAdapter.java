package com.tilapia.skyscannerplus;

/**
 * Created by Pomo on 11/08/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tilapia.skyscannerplus.models.Agent;
import com.tilapia.skyscannerplus.models.Carrier;
import com.tilapia.skyscannerplus.models.Currency;
import com.tilapia.skyscannerplus.models.Itinerary;
import com.tilapia.skyscannerplus.models.Leg;
import com.tilapia.skyscannerplus.models.Place;
import com.tilapia.skyscannerplus.models.PollResult;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ItineraryViewHolder> {

    private List<Itinerary> itineraryList;
    private PollResult result;

    public ResultAdapter(PollResult result) {
        this.itineraryList = result.getItineraries();
        this.result = result;
    }

    @Override
    public ItineraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_result, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItineraryViewHolder holder, int position) {
        Itinerary itinerary = itineraryList.get(position);
        Leg outLeg = getLeg(itinerary.getOutboundLegId());
        Leg inLeg = getLeg(itinerary.getInboundLegId());

        //Outbound
        holder.txtGoTime.setText(getTimeString(outLeg));
        holder.txtGoAirline.setText(getAirlineString(outLeg));
        holder.txtGoDuration.setText(getDurationString(outLeg.getDuration()));
        Glide.with(holder.goAirlineLogo.getContext()).load(getCarrierUrl(outLeg.getCarriers().get(0))).into(holder.goAirlineLogo);

        //Inbound
        holder.txtReturnTime.setText(getTimeString(inLeg));
        holder.txtReturnAirline.setText(getAirlineString(inLeg));
        holder.txtReturnDuration.setText(getDurationString(inLeg.getDuration()));
        Glide.with(holder.returnAirlineLogo.getContext()).load(getCarrierUrl(inLeg.getCarriers().get(0))).into(holder.returnAirlineLogo);

        //Footer
        holder.txtPrice.setText(getPriceString(itinerary.getPricingOptions().get(0).getPrice()));
        holder.txtProvider.setText(holder.txtProvider.getContext().getString(R.string.provider, getAgent(itinerary.getPricingOptions().get(0).getAgents().get(0)).getName()));
    }

    private StringBuilder getPriceString(Double price){
        StringBuilder builder = new StringBuilder();
        builder.append(result.getCurrencies().get(0).getSymbol());
        builder.append(price);
        return builder;
    }

    private StringBuilder getDurationString(int i){
        int hrs = i / 60;
        int mins = i % 60;
        StringBuilder builder = new StringBuilder();
        if(hrs > 0){
            builder.append(hrs);
            builder.append("h ");
        }
        builder.append(mins);
        builder.append("m");
        return builder;
    }

    private StringBuilder getTimeString(Leg leg){
        StringBuilder timeStringBuilder = new StringBuilder();
        timeStringBuilder.append(getTimeString(leg.getDeparture()));
        timeStringBuilder.append(" - ");
        timeStringBuilder.append(getTimeString(leg.getArrival()));
        return timeStringBuilder;
    }

    private StringBuilder getAirlineString(Leg leg){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPlace(leg.getOriginStation()).getCode());
        stringBuilder.append("-");
        stringBuilder.append(getPlace(leg.getDestinationStation()).getCode());
        for(int id : leg.getCarriers()){
            stringBuilder.append(", ");
            stringBuilder.append(getCarrier(id).getName());
        }
        return stringBuilder;
    }

    private String getTimeString(String longTime){
        return longTime.substring(11, 16);
    }

    private Place getPlace(int id){
        for(Place place : result.getPlaces()){
            if(place.getId() == id)
                return place;
        }
        return null;
    }

    private Agent getAgent(int id){
        for(Agent agent : result.getAgents()){
            if(agent.getId() == id)
                return agent;
        }
        return null;
    }

    private Leg getLeg(String id){
        for(Leg leg : result.getLegs()){
            if(leg.getId().equalsIgnoreCase(id))
                return leg;
        }
        return null;
    }

    private Carrier getCarrier(int id){
        for(Carrier carrier : result.getCarriers()){
            if(carrier.getId() == id){
                return carrier;
            }
        }
        return null;
    }

    private String getCarrierUrl(int id){
        for(Carrier carrier : result.getCarriers()){
            if(carrier.getId() == id)
                return carrier.getImageUrl();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    class ItineraryViewHolder extends RecyclerView.ViewHolder {

        TextView txtGoTime, txtGoDuration, txtGoAirline, txtGoType, txtPrice, txtProvider;
        TextView txtReturnTime, txtReturnDuration, txtReturnAirline, txtReturnType;
        ImageView goAirlineLogo, returnAirlineLogo;

        ItineraryViewHolder(View itemView) {
            super(itemView);
            txtGoTime = itemView.findViewById(R.id.txt_go_time);
            txtGoDuration = itemView.findViewById(R.id.txt_go_duration);
            txtGoAirline = itemView.findViewById(R.id.txt_go_airline);
            txtGoType = itemView.findViewById(R.id.txt_go_type);

            txtReturnTime = itemView.findViewById(R.id.txt_return_time);
            txtReturnDuration = itemView.findViewById(R.id.txt_return_duration);
            txtReturnAirline = itemView.findViewById(R.id.txt_return_airline);
            txtReturnType = itemView.findViewById(R.id.txt_return_type);

            txtPrice = itemView.findViewById(R.id.txt_price);
            txtProvider = itemView.findViewById(R.id.txt_provider);
            goAirlineLogo = itemView.findViewById(R.id.image_go_airline_logo);
            returnAirlineLogo = itemView.findViewById(R.id.image_return_airline_logo);
        }
    }
}
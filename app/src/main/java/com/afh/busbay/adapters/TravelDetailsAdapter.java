package com.afh.busbay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afh.busbay.R;
import com.afh.busbay.models.EveningTravel;
import com.afh.busbay.models.MorningTravel;
import com.afh.busbay.models.TravelDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TravelDetailsAdapter extends RecyclerView.Adapter<TravelDetailsAdapter.TravelDetailsViewHolder> {

    private ArrayList<TravelDetails> travelDetailsArrayList;

    public void setTravelDetailsArrayList(ArrayList<TravelDetails> travelDetailsArrayList) {
        this.travelDetailsArrayList = travelDetailsArrayList;
    }

    @NonNull
    @Override
    public TravelDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_student_expense, parent, false);
        return new TravelDetailsAdapter.TravelDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelDetailsViewHolder holder, int position) {
        TravelDetails travelDetails = travelDetailsArrayList.get(position);
        MorningTravel morningTravel = travelDetails.getMorning();
        EveningTravel eveningTravel = travelDetails.getEvening();
        String formattedDate = formatDate(travelDetails.getDate());
        holder.dateTxtView.setText(formattedDate);
        if (eveningTravel != null) {
            holder.eveningPlaceTxtView.setText(eveningTravel.getToLocation());
            holder.eveningExpenseTxtView.setText(String.valueOf(eveningTravel.getCharge()));
            holder.eveningDistanceTxtView.setText(eveningTravel.getDistance());
        }
        if (morningTravel != null) {
            holder.morningExpenseTxtView.setText(String.valueOf(morningTravel.getCharge()));
            holder.morningPlaceTxtView.setText(morningTravel.getFromLocation());
            holder.morningDistanceTxtView.setText(morningTravel.getDistance());
        }
    }

    private String formatDate(String date) {
        String[] dateList = date.split("_");
        return "Date: " + dateList[2] + "/" + dateList[1] + "/" + dateList[3];
    }

    @Override
    public int getItemCount() {
        return travelDetailsArrayList.size();
    }

    class TravelDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView dateTxtView;
        TextView morningExpenseTxtView;
        TextView eveningExpenseTxtView;
        TextView morningPlaceTxtView;
        TextView eveningPlaceTxtView;
        TextView morningDistanceTxtView;
        TextView eveningDistanceTxtView;


        public TravelDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTxtView = itemView.findViewById(R.id.tv_stud_exp_date);
            morningExpenseTxtView = itemView.findViewById(R.id.tv_stud_exp_morning);
            eveningExpenseTxtView = itemView.findViewById(R.id.tv_stud_exp_evening);
            morningDistanceTxtView = itemView.findViewById(R.id.tv_km_travel_morning);
            eveningDistanceTxtView = itemView.findViewById(R.id.tv_km_travel_evening);
            morningPlaceTxtView = itemView.findViewById(R.id.tv_to_place_morning);
            eveningPlaceTxtView = itemView.findViewById(R.id.tv_to_place_evening);

        }
    }
}

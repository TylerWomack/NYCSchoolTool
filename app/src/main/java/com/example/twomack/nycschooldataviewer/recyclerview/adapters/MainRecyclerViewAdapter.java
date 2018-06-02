package com.example.twomack.nycschooldataviewer.recyclerview.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.R;

import java.util.List;

/**
 * Created by twomack on 3/23/18.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<DetailedSchool> schoolList;
    private OnSchoolSelectedListener listener;

    public interface OnSchoolSelectedListener {
        void onSchoolClicked(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        TextView name, SATTotal, neighborhood, borough, graduationRate, students, safetyRating, distance;
        RelativeLayout summaryLayout;
        ViewHolder(View v) {
            super(v);
            findViews(v);
        }

        RelativeLayout getSchoolRelativeLayout(){return summaryLayout;}

        private void findViews(View v){
            name = v.findViewById(R.id.name_summary);
            SATTotal = v.findViewById(R.id.SAT_summary);
            borough = v.findViewById(R.id.summary_borough);
            graduationRate = v.findViewById(R.id.graduation_rate_summary);
            students = v.findViewById(R.id.summary_population);
            safetyRating = v.findViewById(R.id.safety_summary);
            distance = v.findViewById(R.id.summary_distance);
            summaryLayout = v.findViewById(R.id.summary_layout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapter(OnSchoolSelectedListener listener) { this.listener = listener;}

    // Create new views (invoked by the layout manager) - inflates each view
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_summary, parent, false);
        return new ViewHolder(v);
    }

    public void setSchoolList(List<DetailedSchool> schoolList) {
        this.schoolList = schoolList;
        notifyDataSetChanged();
    }

    // Replace the contents of a view when it is bound (updating it with the right data before we display it)
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(final MainRecyclerViewAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        setHolderText(holder, position);

        setSchoolSafetyColor(holder, position);
        setGraduationRateColor(holder, position);
        setSATScoreColor(holder, position);

        holder.getSchoolRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSchoolClicked(holder.getAdapterPosition());
            }
        });
    }

    private void setHolderText(MainRecyclerViewAdapter.ViewHolder holder, int position){
        holder.name.setText(schoolList.get(position).getSchoolName());
        holder.graduationRate.setText(schoolList.get(position).getGraduationRate() + "% Graduate" );
        holder.borough.setText(schoolList.get(position).getBorough());
        holder.students.setText("Students: " + schoolList.get(position).getTotalStudents());
        holder.safetyRating.setText(schoolList.get(position).getPctStuSafe() + "% Feel Safe");
        holder.SATTotal.setText(schoolList.get(position).getTotalSATScore() + " Average SAT");
        String schoolDist = schoolList.get(position).getDistanceInMiles();
        if (schoolDist != null && schoolList.get(position).getDistanceInMeters() != 0){
            holder.distance.setText(schoolList.get(position).getDistanceInMiles() + " miles");
            holder.distance.setVisibility(View.VISIBLE);
        }else {
            holder.distance.setVisibility(View.INVISIBLE);
        }
    }

    private void setSchoolSafetyColor(MainRecyclerViewAdapter.ViewHolder holder, int position){
        if (schoolList.get(position).getPctStuSafe().length() > 1) {
            if (Integer.valueOf(schoolList.get(position).getPctStuSafe()) > 90) {
                holder.safetyRating.setTextColor(Color.argb(255, 95, 186, 50));
                //holder.safetyRating.setTextColor(ResourcesCompat.getColor(get));
            }

            if (Integer.valueOf(schoolList.get(position).getPctStuSafe()) < 70) {
                holder.safetyRating.setTextColor(Color.RED);
            }

            if ((Integer.valueOf(schoolList.get(position).getPctStuSafe()) >= 70) && (Integer.valueOf(schoolList.get(position).getPctStuSafe()) <= 90)) {
                holder.safetyRating.setTextColor(Color.GRAY);
            }
        }
        if (schoolList.get(position).getPctStuSafe() == null || schoolList.get(position).getPctStuSafe().isEmpty()){
            holder.safetyRating.setTextColor(Color.GRAY);
        }
    }

    private void setGraduationRateColor(MainRecyclerViewAdapter.ViewHolder holder, int position){
        if (schoolList.get(position).getGraduationRate().length() > 1) {
            if (Integer.valueOf(schoolList.get(position).getGraduationRate()) > 85) {
                holder.graduationRate.setTextColor(Color.argb(255, 95, 186, 50));
            }

            if (Integer.valueOf(schoolList.get(position).getGraduationRate()) < 65) {
                holder.graduationRate.setTextColor(Color.RED);
            }

            if ((Integer.valueOf(schoolList.get(position).getGraduationRate()) >= 65) && (Integer.valueOf(schoolList.get(position).getGraduationRate()) <= 85)) {
                holder.graduationRate.setTextColor(Color.GRAY);
            }
        }
        if (schoolList.get(position).getGraduationRate() == null || schoolList.get(position).getGraduationRate().isEmpty()){
            holder.graduationRate.setTextColor(Color.GRAY);
        }
    }

    private void setSATScoreColor(MainRecyclerViewAdapter.ViewHolder holder, int position){

        if (schoolList.get(position).getTotalSATScore().length() > 1) {
            if (Integer.valueOf(schoolList.get(position).getTotalSATScore()) > 1100) {
                holder.SATTotal.setTextColor(Color.argb(255, 95, 186, 50));
            }

            if (Integer.valueOf(schoolList.get(position).getTotalSATScore()) < 750) {
                holder.SATTotal.setTextColor(Color.RED);
            }

            if ((Integer.valueOf(schoolList.get(position).getTotalSATScore()) >= 750) && (Integer.valueOf(schoolList.get(position).getTotalSATScore()) <= 1100)) {
                holder.SATTotal.setTextColor(Color.GRAY);
            }
        }

        if (schoolList.get(position).getTotalSATScore().isEmpty() || schoolList.get(position).getTotalSATScore() == null){
            holder.SATTotal.setTextColor(Color.GRAY);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    public int getItemCount() {
        if (schoolList != null){
            return schoolList.size();
        }
        return 0;
    }
}

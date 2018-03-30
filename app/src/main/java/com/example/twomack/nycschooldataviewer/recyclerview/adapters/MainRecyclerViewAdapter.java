package com.example.twomack.nycschooldataviewer.recyclerview.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.twomack.nycschooldataviewer.DetailedSchool;
import com.example.twomack.nycschooldataviewer.R;
import com.example.twomack.nycschooldataviewer.School;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;

/**
 * Created by twomack on 3/23/18.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<DetailedSchool> schoolList;
    private OnSchoolSelectedListener listener;

    public interface OnSchoolSelectedListener {
        void onSchoolClicked(int position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView name, SATTotal, neighborhood, borough, graduationRate, students, safetyRating, distance;
        public RelativeLayout summaryLayout;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name_summary);
            SATTotal = (TextView) v.findViewById(R.id.SAT_summary);
            borough = (TextView) v.findViewById(R.id.summary_borough);
            graduationRate = (TextView) v.findViewById(R.id.graduation_rate_summary);
            students = (TextView) v.findViewById(R.id.summary_population);
            safetyRating = (TextView) v.findViewById(R.id.safety_summary);
            distance = (TextView) v.findViewById(R.id.summary_distance);
            summaryLayout = (RelativeLayout) v.findViewById(R.id.summary_layout);
        }

        public RelativeLayout getSchoolRelativeLayout(){return summaryLayout;}
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainRecyclerViewAdapter(OnSchoolSelectedListener listener) { this.listener = listener;}

    //A ViewGroup is a linearlayout, etc.
    // Create new views (invoked by the layout manager)
    //@Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_summary, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setSchoolList(List<DetailedSchool> schoolList) {
        this.schoolList = schoolList;
        notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(final MainRecyclerViewAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

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


        holder.getSchoolRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSchoolClicked(holder.getAdapterPosition());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    //@Override
    public int getItemCount() {
        if (schoolList != null){
            return schoolList.size();
        }
        return 0;
    }
}

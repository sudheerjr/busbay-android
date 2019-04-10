package com.afh.busbay.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afh.busbay.R;
import com.afh.busbay.models.Attendance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private ArrayList<Attendance> attendanceArrayList;

    public void setAttendanceArrayList(ArrayList<Attendance> attendanceArrayList) {
        this.attendanceArrayList = attendanceArrayList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceArrayList.get(position);
        holder.userNameTxtView.setText(attendance.getUsername());
        holder.userPresenceChkBox.setChecked(attendance.isUserPresence());
    }

    @Override
    public int getItemCount() {
        return attendanceArrayList.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTxtView;
        CheckBox userPresenceChkBox;

        public AttendanceViewHolder(@NonNull final View itemView) {
            super(itemView);
            userNameTxtView = itemView.findViewById(R.id.li_attendance_stud_name);
            userPresenceChkBox = itemView.findViewById(R.id.li_attendance_stud_presence);
            userPresenceChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == false) {
                        if (attendanceArrayList.get(getAdapterPosition()).isUserPresence()) {
                            String message = attendanceArrayList.get(getAdapterPosition()).getUsername() + " was present in the college bus today!";
                            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}

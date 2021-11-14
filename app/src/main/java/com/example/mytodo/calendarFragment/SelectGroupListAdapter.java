package com.example.mytodo.calendarFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.model.Group;
import com.example.mytodo.R;

import java.util.List;

public class SelectGroupListAdapter extends RecyclerView.Adapter<SelectGroupListAdapter.GroupViewHolder> {
    private List<Group> groups;
    private SelectGroupEventListener eventListener;
    public SelectGroupListAdapter(List<Group> groups,SelectGroupEventListener eventListener){

        this.groups = groups;
        this.eventListener =eventListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_group_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.titleTv.setText(groups.get(position).getGroupTitle());
        holder.itemView.setOnClickListener(v -> {
            eventListener.onGroupSelected(groups.get(position));
        });
    }



    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.txtTitle);
        }


    }
    public interface SelectGroupEventListener{
        void onGroupSelected(Group group);
    }
}

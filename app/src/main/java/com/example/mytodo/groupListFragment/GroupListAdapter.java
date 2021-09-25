package com.example.mytodo.groupListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Group;
import com.example.mytodo.R;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    private List<Group> groups;
    private GroupEventListener eventListener;
    public GroupListAdapter(List<Group> groups,GroupEventListener eventListener) {
        this.groups = groups;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {

        holder.onBind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView groupTitleTv;
        private TextView groupCounterTv;
        private ImageView groupStar;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitleTv = itemView.findViewById(R.id.txtTitle);
            groupCounterTv = itemView.findViewById(R.id.txtCounter);
            groupStar = itemView.findViewById(R.id.imgStar);
        }

        public void onBind(Group group) {
            groupTitleTv.setText(group.getGroupTitle());
            if (group.isStar()){
                groupStar.setImageResource(R.drawable.ic_full_star_24);
            }else {
                groupStar.setImageResource(R.drawable.ic_star_outline_24);
            }
            groupCounterTv.setText(String.valueOf(group.getCounter()));
            itemView.setOnClickListener(v -> eventListener.onGroupClickListener(group));
            itemView.setOnLongClickListener(v -> {
                eventListener.onLongGroupClickListener(group);
                return false;
            });
            groupStar.setOnClickListener(v -> eventListener.onImgStarClickListener(group));
        }

    }
    public interface GroupEventListener{
        void onGroupClickListener(Group group);
        void onLongGroupClickListener(Group group);
        void onImgStarClickListener(Group group);
    }
}

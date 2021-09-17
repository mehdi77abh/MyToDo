package com.example.mytodo.MainFragment;

import static com.example.mytodo.other.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.other.Const.IMPORTANCE_LOW;
import static com.example.mytodo.other.Const.IMPORTANCE_NORMAL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.databinding.ItemTaskBinding;

import java.util.Calendar;
import java.util.List;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class MainTaskAdapter extends RecyclerView.Adapter<MainTaskAdapter.MyViewHolder> {
    private List<Task> notDoneTasks;
    private MainTaskAdapter.EventListener eventListener ;

    public MainTaskAdapter(List<Task> notDoneTasks,MainTaskAdapter.EventListener eventListener) {
        this.notDoneTasks = notDoneTasks;
        this.eventListener =eventListener;
    }

    @NonNull
    @Override
    public MainTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MainTaskAdapter.MyViewHolder holder, int position) {
    holder.bindView(notDoneTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return notDoneTasks.size();
    }

    public Task getTask(int pos) {
        return notDoneTasks.get(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTv, dateTv, monthTv, titleTv, timeTv,descriptionTv;
        private View importanceView;
        private ImageView imgCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.dayTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            importanceView = itemView.findViewById(R.id.importanceView);
            imgCheck = itemView.findViewById(R.id.img_check);
        }

        private void bindView(Task task) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(task.getDateLong());
            PersianDateImpl persianDate = new PersianDateImpl();
            persianDate.setDate(task.getDateLong());

            titleTv.setText(task.getTitle());
            descriptionTv.setText(task.getDescription());
            timeTv.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

            dayTv.setText(persianDate.getPersianDayOfWeekName());
            String number = PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianDay()));
            dateTv.setText(number);
            monthTv.setText(persianDate.getPersianMonthName());

            imgCheck.setImageResource(R.drawable.shape_checkbox_default);


            switch (task.getImportance()) {
                case IMPORTANCE_HIGH:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_high_rect);
                    break;
                case IMPORTANCE_NORMAL:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);

                    break;
                case IMPORTANCE_LOW:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_low_rect);

                    break;
                default:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);


            }

            imgCheck.setOnClickListener(v -> {
                imgCheck.setBackgroundResource(R.drawable.shape_checkbox_check);
                imgCheck.setImageResource(R.drawable.ic_baseline_check_24_white);

                eventListener.ImgClickListener(task);

            });

            itemView.setOnLongClickListener(v -> {
                eventListener.ItemClickListener(task);

                return false;
            });


        }
    }
    public interface EventListener{
        void ItemClickListener(Task task);
        void ImgClickListener(Task task);
    }

}

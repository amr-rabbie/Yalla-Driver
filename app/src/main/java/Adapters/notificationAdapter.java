package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asi.yalla_egy.R;

import java.util.List;

import Models.notificationModel;

/**
 * Created by ASI on 4/3/2017.
 */

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.StatisticHolder> {

    private final LayoutInflater mInflater;
    private final List<notificationModel> mModels;
    private int lastPosition=-1;
    Context context;

    public notificationAdapter(Context context, List<notificationModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = models;
        this.context=context;
    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.notificationitem, parent, false);
        return new StatisticHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final StatisticHolder holder, int position) {
        final notificationModel model = mModels.get(position);
        holder.bind(model);



    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<notificationModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<notificationModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final notificationModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<notificationModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final notificationModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<notificationModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final notificationModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public notificationModel removeItem(int position) {
        final notificationModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }
    public void addItem(int position, notificationModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }
    public void moveItem(int fromPosition, int toPosition) {
        final notificationModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }





    class StatisticHolder extends RecyclerView.ViewHolder {

        public TextView title,message,date;


        public StatisticHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTitle);

            message = (TextView) itemView.findViewById(R.id.tvMessage);
            date= (TextView) itemView.findViewById(R.id.tvDate);


        }

        public void bind(notificationModel model) {

            title.setText(model.getTitle());
            if (model.getMessaage().equals("null"))
            {
                message.setText(" ");
            }else {
                message.setText(model.getMessaage());
            }

            date.setText(model.getDate());



        }


    }


}
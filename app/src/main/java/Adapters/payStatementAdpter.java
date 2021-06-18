package Adapters;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.asi.yalla_egy.R;

import java.util.List;

import Models.payModel;

/**
 * Created by ASI on 4/3/2017.
 */

public class payStatementAdpter extends RecyclerView.Adapter<payStatementAdpter.StatisticHolder> {

    private final LayoutInflater mInflater;
    private final List<payModel> mModels;
    private int lastPosition=-1;
    Context context;

    public payStatementAdpter(Context context, List<payModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = models;
        this.context=context;
    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.paystatementitem, parent, false);
        return new StatisticHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final StatisticHolder holder, int position) {
        final payModel model = mModels.get(position);
        holder.bind(model);



    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<payModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<payModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final payModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<payModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final payModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<payModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final payModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public payModel removeItem(int position) {
        final payModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }
    public void addItem(int position, payModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }
    public void moveItem(int fromPosition, int toPosition) {
        final payModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class StatisticHolder extends RecyclerView.ViewHolder {

        public TextView name,value;


        public StatisticHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvName);

            value= (TextView) itemView.findViewById(R.id.tvValue);


        }

        public void bind(payModel model) {

            name.setText(model.getName());
            value.setText(model.getValue());



        }


    }


}
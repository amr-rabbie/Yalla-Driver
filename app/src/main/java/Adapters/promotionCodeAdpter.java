package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asi.yalla_egy.R;

import java.util.List;

import Models.promotionCodeModel;


/**
 * Created by ASI on 4/3/2017.
 */

public class promotionCodeAdpter extends RecyclerView.Adapter<promotionCodeAdpter.StatisticHolder> {

    private final LayoutInflater mInflater;
    private final List<promotionCodeModel> mModels;
    Context context;

    public promotionCodeAdpter(Context context, List<promotionCodeModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = models;
        this.context=context;
    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.promotionitem, parent, false);
        return new StatisticHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final StatisticHolder holder, int position) {
        final promotionCodeModel model = mModels.get(position);
        holder.bind(model);



    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<promotionCodeModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<promotionCodeModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final promotionCodeModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<promotionCodeModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final promotionCodeModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<promotionCodeModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final promotionCodeModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public promotionCodeModel removeItem(int position) {
        final promotionCodeModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }
    public void addItem(int position, promotionCodeModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }
    public void moveItem(int fromPosition, int toPosition) {
        final promotionCodeModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }





    class StatisticHolder extends RecyclerView.ViewHolder {

        public TextView date,code,dsicount;





        public StatisticHolder(final View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tvdate);
            code= (TextView) itemView.findViewById(R.id.tvPromotionCode);
            dsicount= (TextView) itemView.findViewById(R.id.tvDiscountAmount);

        }

        public void bind(promotionCodeModel model) {


            date.setText(model.getDate());
            code.setText(model.getCode());
            dsicount.setText(model.getDiscountAmount());


        }


    }


}
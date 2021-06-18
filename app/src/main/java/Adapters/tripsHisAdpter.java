package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asi.yalla_egy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.tripshistorymodel;

/**
 * Created by ASI on 4/3/2017.
 */

public class tripsHisAdpter extends RecyclerView.Adapter<tripsHisAdpter.StatisticHolder> {

    private final LayoutInflater mInflater;
    private final List<tripshistorymodel> mModels;
    private int lastPosition=-1;
    Context context;

    public tripsHisAdpter(Context context, List<tripshistorymodel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = models;
        this.context=context;
    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.tripshistoryitem, parent, false);
        return new StatisticHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final StatisticHolder holder, int position) {
        final tripshistorymodel model = mModels.get(position);
        holder.bind(model);



    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<tripshistorymodel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<tripshistorymodel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final tripshistorymodel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<tripshistorymodel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final tripshistorymodel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<tripshistorymodel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final tripshistorymodel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public tripshistorymodel removeItem(int position) {
        final tripshistorymodel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }
    public void addItem(int position, tripshistorymodel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }
    public void moveItem(int fromPosition, int toPosition) {
        final tripshistorymodel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }





    class StatisticHolder extends RecyclerView.ViewHolder {

        public TextView date,ditance,cost;

        ImageView passengerpic;
        RatingBar ratingBar;



        public StatisticHolder(final View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tvdate);
            ratingBar= (RatingBar) itemView.findViewById(R.id.review_ratingBar);
            ditance= (TextView) itemView.findViewById(R.id.tvDistace);
            passengerpic= (ImageView) itemView.findViewById(R.id.ivPassanger);
            cost= (TextView) itemView.findViewById(R.id.tvCost);

        }

        public void bind(tripshistorymodel model) {


            date.setText(model.getDate());
            ratingBar.setRating(Float.parseFloat(model.getRate()));
            ditance.setText(model.getDistance());
            ratingBar.setRating(Float.parseFloat(model.getRate()));
            cost.setText(model.getFare());

            Picasso.with(context).load(model.getImage())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.warning)
                    .into(passengerpic);

//            Glide.with(context).load(model.getImage())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .placeholder(R.drawable.loooooosogooo)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(passengerpic);
            cost.setText(model.getFare()+" SAR");


        }


    }


}
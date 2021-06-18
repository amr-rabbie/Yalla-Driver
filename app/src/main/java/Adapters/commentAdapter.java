package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asi.yalla_egy.R;

import java.util.List;

import Models.commentsModel;
/**
 * Created by ASI on 4/3/2017.
 */

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.StatisticHolder> {

    private final LayoutInflater mInflater;
    private final List<commentsModel> mModels;
    private int lastPosition=-1;
    Context context;

    public commentAdapter(Context context, List<commentsModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = models;
        this.context=context;
    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.comment_item, parent, false);
        return new StatisticHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final StatisticHolder holder, int position) {
        final commentsModel model = mModels.get(position);
        holder.bind(model);



    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<commentsModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<commentsModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final commentsModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<commentsModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final commentsModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<commentsModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final commentsModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public commentsModel removeItem(int position) {
        final commentsModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }
    public void addItem(int position, commentsModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }
    public void moveItem(int fromPosition, int toPosition) {
        final commentsModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }





    class StatisticHolder extends RecyclerView.ViewHolder {

        public TextView comment,date;

        RatingBar ratingBar;

        public StatisticHolder(final View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.tvcomment);

            date = (TextView) itemView.findViewById(R.id.tvdate);

            ratingBar= (RatingBar) itemView.findViewById(R.id.review_ratingBar);


        }

        public void bind(commentsModel model) {

            comment.setText(model.getComment());
            date.setText(model.getDate());
            ratingBar.setRating(Float.parseFloat(model.getRate()));



        }


    }


}
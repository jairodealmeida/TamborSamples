package com.tambor.samples;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tambor.samples.placeholder.PlaceholderContent.PlaceholderItem;
import com.tambor.samples.databinding.FragmentQuestListBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class QuestListRecyclerViewAdapter extends RecyclerView.Adapter<QuestListRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public QuestListRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentQuestListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).details);
        if(mValues.get(position).value==null) {
            holder.mValueYesView.setChecked(false);
            holder.mValueNoView.setChecked(false);
            holder.mValueNAView.setChecked(true);
        }else if(mValues.get(position).value){
            holder.mValueYesView.setChecked(true);
            holder.mValueNoView.setChecked(false);
            holder.mValueNAView.setChecked(false);
        }else{
            holder.mValueYesView.setChecked(false);
            holder.mValueNoView.setChecked(true);
            holder.mValueNAView.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final CheckBox mValueYesView;
        public final CheckBox mValueNoView;
        public final CheckBox mValueNAView;
        public final TextView mObservationView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentQuestListBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mValueYesView = binding.yes;
            mValueNoView = binding.no;
            mValueNAView =            binding.na;
            mObservationView =            binding.observation;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
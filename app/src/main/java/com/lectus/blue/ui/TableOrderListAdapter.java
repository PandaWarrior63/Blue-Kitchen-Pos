package com.lectus.blue.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lectus.blue.model.TableItem;
import com.lectus.blue.model.TableOrderItem;
import com.lectus.blue.ui.placeholder.PlaceholderContent.PlaceholderItem;
import com.lectus.blue.databinding.FragmentTableOrderItemBinding;
import com.lectus.blue.utils.TableAdapter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TableOrderListAdapter extends RecyclerView.Adapter<TableOrderListAdapter.ViewHolder> {

    //private final List<PlaceholderItem> mValues;

    //public TableOrderListAdapter(List<PlaceholderItem> items) {
      //  mValues = items;
    //}
    private TableOrderListAdapter.OnRemoveButtonClickListener onRemoveButtonClickListener;
    private final List<TableOrderItem> mOrderLists;
    public TableOrderListAdapter(List<TableOrderItem> orderList,OnRemoveButtonClickListener listener) {
        mOrderLists = orderList;
        this.onRemoveButtonClickListener = listener;
    }
    public interface OnRemoveButtonClickListener {
        void onRemoveButtonClick(int position, TableOrderItem orderItem);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTableOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mOrderLists.get(position);
        //holder.mIdView.setText(mOrderLists.get(position).id);
        holder.mIdView.setText(String.valueOf(position+1));
        holder.mContentView.setText(mOrderLists.get(position).getItem_code());
        float count = Float.parseFloat(mOrderLists.get(position).getQuantity());
        holder.mCountView.setText(String.format("%.2f",count));
        holder.mPriceView.setText(mOrderLists.get(position).getPrice());
        holder.mDeleteButton.setOnClickListener(v -> {
            TableOrderItem item = mOrderLists.get(position);
            onRemoveButtonClickListener.onRemoveButtonClick(position,item);
        });

    }

    @Override
    public int getItemCount() {
        return mOrderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mCountView;
        public final TextView mPriceView;
        public final Button  mDeleteButton;
        public TableOrderItem mItem;

        public ViewHolder(FragmentTableOrderItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mCountView = binding.count;
            mPriceView = binding.price;
            mDeleteButton = binding.deleteButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
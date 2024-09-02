package  com.lectus.blue.adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.lectus.blue.model.TableItem;
import com.lectus.blue.R;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.CardViewHolder> {
    private OnCardClickListener onCardClickListener;
    private List<TableItem> cardItemList;

    public interface OnCardClickListener {
        void onCardClick(int position, TableItem cardItem);
    }

    public TableAdapter(List<TableItem> cardItemList,OnCardClickListener onCardClickListener) {
        this.cardItemList = cardItemList;
        this.onCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        TableItem cardItem = cardItemList.get(position);
        holder.cardTitle.setText(cardItem.getTitle());
        holder.cardDescription.setText(cardItem.getDescription());
        holder.cardBack.setCardBackgroundColor(Color.parseColor(cardItem.getBackground()));
        holder.cardBack.setRadius(30);
        holder.cardBack.setOnClickListener(v -> {
            //holder.cardBack.setChecked(! holder.cardBack.isChecked());
            onCardClickListener.onCardClick(position,cardItem);
            //return true;
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDescription;
        MaterialCardView cardBack;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardBack = itemView.findViewById(R.id.cardBack);
        }
    }
}
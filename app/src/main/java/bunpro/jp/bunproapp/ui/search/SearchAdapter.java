package bunpro.jp.bunproapp.ui.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.GrammarPoint;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;

class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<SearchAdapter.HeaderHolder> {

    private LayoutInflater inflater;
    private ItemClickListener listener;

    List<GrammarPoint> points;

    SearchAdapter(List<GrammarPoint> points, Context context, ItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.points = points;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = inflater.inflate(R.layout.item_search_word, viewGroup, false);
        return new ViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        GrammarPoint point = this.points.get(position);
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).tvJapanese.setText(point.title);
            ((ViewHolder) viewHolder).tvEnglish.setText(point.meaning);
        }
    }

    @Override
    public int getItemCount() {
        return this.points.size();
    }

    public int getGrammarPointId(int position) {
        return points.get(position).id;
    }

    @Override
    public long getHeaderId(int position) {

        GrammarPoint point = points.get(position);
        String numberStr = point.level != null ? point.level.replaceAll("[^0-9]", "") : "0";

        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @NonNull
    @Override
    public HeaderHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_search_word_header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(@NonNull HeaderHolder viewHolder, int position) {

        viewHolder.tvHeader.setText(String.format("N%s", String.valueOf(getHeaderId(position))));
    }

    void update(List<GrammarPoint> points) {
        this.points = points;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlContainer;
        TextView tvEnglish, tvJapanese;

        WeakReference<ItemClickListener> ref;

        ViewHolder(@NonNull View itemView, ItemClickListener listener) {
            super(itemView);
            ref = new WeakReference<>(listener);

            rlContainer = itemView.findViewById(R.id.rlContainer);
            rlContainer.setOnClickListener(this);

            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvJapanese = itemView.findViewById(R.id.tvJapanese);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlContainer) {
                ref.get().positionClicked(getAdapterPosition());
            }
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        TextView tvHeader;
        HeaderHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }

    public interface ItemClickListener {
        void positionClicked(int position);
    }
}

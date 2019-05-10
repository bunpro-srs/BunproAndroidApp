package bunpro.jp.bunproapp.ui.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import bunpro.jp.bunproapp.R;

class KanjiWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> kanjis;
    ClickListener listener;

    KanjiWordAdapter(List<String> kanjis, ClickListener listener) {
        this.kanjis = kanjis;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new WordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kanji_word, viewGroup, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        String word = kanjis.get(i);
        ((WordViewHolder)viewHolder).tvWord.setText(word);
    }

    @Override
    public int getItemCount() {
        return kanjis.size();
    }

    void update(List<String> kanjis) {
        this.kanjis = kanjis;
    }

    class WordViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        LinearLayout llContainer;
        TextView tvWord;
        WeakReference<ClickListener> ref;


        WordViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);
            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);
            tvWord = itemView.findViewById(R.id.tvWord);
        }

        @Override
        public void onClick(View view) {

            int id = view.getId();
            if (id == R.id.llContainer) {
                ref.get().positionClicked(getAdapterPosition());
            }

        }
    }

    interface ClickListener {
        void positionClicked(int position);
    }
}

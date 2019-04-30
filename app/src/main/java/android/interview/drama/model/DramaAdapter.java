package android.interview.drama.model;

import android.content.Context;
import android.interview.drama.R;
import android.interview.drama.model.DramaList.Drama;
import android.interview.drama.utils.Utils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DramaAdapter extends RecyclerView.Adapter<DramaAdapter.DramaViewHolder> {

    class DramaViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumbnail;
        private TextView textName;
        private TextView textCreated;
        private TextView textRating;

        public DramaViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text_name);
            textRating = itemView.findViewById(R.id.text_rating);
            textCreated = itemView.findViewById(R.id.text_created);
            imageThumbnail = itemView.findViewById(R.id.image_thumbnail);
        }
    }

    private List<Drama> mDramaList;
    private Context mContext;

    public DramaAdapter(Context context, List<Drama> dramaList) {
        this.mContext = context;
        if(dramaList==null) {
            this.mDramaList = new ArrayList<>();
        } else {
            this.mDramaList = dramaList;
        }
    }

    @NonNull
    @Override
    public DramaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drama, viewGroup, false);

        return new DramaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DramaViewHolder myViewHolder, int i) {

        Drama drama = mDramaList.get(i);
        myViewHolder.textName.setText(drama.name);
        myViewHolder.textCreated.setText(mContext.getString(R.string.title_created_at, Utils.covertDateString(drama.created_at)));
        myViewHolder.textRating.setText(mContext.getString(R.string.title_rating, drama.rating));

        myViewHolder.imageThumbnail.setContentDescription(drama.name);
        Glide.with(myViewHolder.itemView.getContext())
                .load(drama.thumb)
                .into(myViewHolder.imageThumbnail);

    }

    @Override
    public int getItemCount() {
        return mDramaList.size();
    }

    public void updateList(List<Drama> dramaList) {
        this.mDramaList = dramaList;
        notifyDataSetChanged();
    }
}

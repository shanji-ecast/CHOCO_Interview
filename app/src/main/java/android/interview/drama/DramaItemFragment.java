package android.interview.drama;

import android.interview.drama.model.DataManager;
import android.interview.drama.model.DramaList.Drama;
import android.interview.drama.utils.Utils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DramaItemFragment extends Fragment implements DataManager.RequestListener {

    private TextView mTextErrorHint;
    private ImageView mImageThumbnail;
    private TextView mTextName;
    private TextView mTextCreated;
    private TextView mTextRating;
    private TextView mTextTotalViews;

    private DataManager mDataManager;
    private Drama mDrama;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataManager = new DataManager(getActivity(), this);

        if (getArguments()!=null) {
            int drama_id = getArguments().getInt("drama_id");
            mDataManager.requestDramaItem(drama_id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drama_item, container, false);
        mTextErrorHint = rootView.findViewById(R.id.text_error_hint);

        mImageThumbnail = rootView.findViewById(R.id.image_thumbnail);
        mTextName = rootView.findViewById(R.id.text_name);
        mTextCreated = rootView.findViewById(R.id.text_created);
        mTextRating = rootView.findViewById(R.id.text_rating);
        mTextTotalViews = rootView.findViewById(R.id.text_total_views);

        return rootView;
    }

     private void setViews() {

        if(mDrama==null) {
            mTextErrorHint.setText(getString(R.string.hint_drama_empty));
            mTextErrorHint.setVisibility(View.VISIBLE);
        } else {
            mTextErrorHint.setVisibility(View.GONE);

            mTextName.setText(mDrama.name);
            mTextCreated.setText(getString(R.string.title_created_at, Utils.covertDateString(mDrama.created_at)));
            mTextRating.setText(getString(R.string.title_rating, mDrama.rating));
            mTextTotalViews.setText(getString(R.string.title_total_views, mDrama.total_views));

            mImageThumbnail.setContentDescription(mDrama.name);
            Glide.with(getActivity())
                    .load(mDrama.thumb)
                    .into(mImageThumbnail);

        }
    }

    @Override
    public void onRequestNewDramaList(boolean successful, List<Drama> dramaList) {

    }

    @Override
    public void onRequestDramaList(List<Drama> dramaList) {

    }

    @Override
    public void onRequestDramaItem(Drama drama) {
        mDrama = drama;
        setViews();
    }
}

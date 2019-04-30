package android.interview.drama;

import android.interview.drama.model.DataManager;
import android.interview.drama.model.DramaAdapter;
import android.interview.drama.model.DramaList;
import android.interview.drama.model.RecycleViewOnItemClickListener;
import android.interview.drama.utils.Utils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

public class DramaListFragment extends Fragment implements DataManager.RequestListener {

    private DataManager mDataManager;
    private List<DramaList.Drama> mDramaList;
    private boolean hasKeyword = false;

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private TextView mTextErrorHint;
    private DramaAdapter mDramaAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drama_list, container, false);

        mTextErrorHint = rootView.findViewById(R.id.text_error_hint);
        initRecycleView(rootView);
        initSearchView(rootView);
        initSwipeContainer(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataManager = new DataManager(getActivity(), this);
        mDataManager.requestDramaList();
        mDataManager.requestNewestDataList();

        String keyword = Utils.getLastQuery(getContext());
        if (!TextUtils.isEmpty(keyword)) {
            hasKeyword = true;
            mSearchView.onActionViewExpanded();
            mSearchView.setQuery(keyword, true);
            mSearchView.clearFocus();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.saveLastQuery(getContext(), mSearchView.getQuery().toString());
    }

    private void initRecycleView(View rootView) {

        mRecyclerView = rootView.findViewById(R.id.recycleView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecycleViewOnItemClickListener(getContext(), mRecyclerView, new RecycleViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mSwipeContainer !=null && !mSwipeContainer.isRefreshing()) {

                    MainActivity activity = (MainActivity) getActivity();
                    DramaItemFragment fragment = new DramaItemFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("drama_id", mDramaList.get(position).drama_id);
                    fragment.setArguments(bundle);
                    activity.addFragment(fragment);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        updateRecycleView();
    }

    private void initSearchView(View rootView) {

        mSearchView = rootView.findViewById(R.id.searchView);
        mSearchView.setQueryHint(getString(R.string.hint_search));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                if (!TextUtils.isEmpty(text)) {
                    Utils.saveLastQuery(getContext(), text);
                    mDataManager.requestDramaItem(text);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (TextUtils.isEmpty(text)) {
                    mDataManager.requestDramaList();
                }

                return false;
            }
        });
    }

    private void initSwipeContainer(View rootView) {
        mSwipeContainer = rootView.findViewById(R.id.swipe_container);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSearchView.setQuery("",false);
                hasKeyword = false;
                mDataManager.requestNewestDataList();
            }
        });
        mSwipeContainer.setColorSchemeResources(R.color.swipe_1, R.color.swipe_2);
    }

    private void updateRecycleView() {
        if(mDramaAdapter==null) {
            mDramaAdapter = new DramaAdapter(getContext(), mDramaList);
            mRecyclerView.setAdapter(mDramaAdapter);
        } else {
            mDramaAdapter.updateList(mDramaList);
        }

        if(mDramaList==null || mDramaList.isEmpty()) {
            mTextErrorHint.setText(getString(R.string.hint_drama_empty));
            mTextErrorHint.setVisibility(View.VISIBLE);
        } else {
            mTextErrorHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestNewDramaList(boolean successful, List<DramaList.Drama> dramaList) {
        //Only updating list if there is no keyword
        if(successful && !hasKeyword) {
            mDramaList = dramaList;
            updateRecycleView();
        }
        if(mSwipeContainer !=null) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onRequestDramaList(List<DramaList.Drama> dramaList) {
        mDramaList = dramaList;
        updateRecycleView();

        if(mSwipeContainer !=null) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onRequestDramaItem(DramaList.Drama drama) {

    }
}

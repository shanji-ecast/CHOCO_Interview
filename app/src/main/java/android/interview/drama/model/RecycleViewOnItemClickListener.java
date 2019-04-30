package android.interview.drama.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecycleViewOnItemClickListener implements RecyclerView.OnItemTouchListener {

    /**
     *  implements RecycleView item click listener
     */

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private Context mContext;
    private RecyclerView mRecycleView;
    private OnItemClickListener mListener;
    private GestureDetector gestureDetector;

    public RecycleViewOnItemClickListener(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        this.mContext = context;
        this.mRecycleView = recyclerView;
        this.mListener = listener;

        gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mListener != null) {
                    mListener.onItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View childView = mRecycleView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
        gestureDetector.setIsLongpressEnabled(true);

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && mListener != null && gestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}

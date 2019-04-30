package android.interview.drama.model;

import android.content.Context;
import android.interview.drama.database.DramaContractUtils;
import android.interview.drama.webservice.RetrofitAPI;
import android.interview.drama.webservice.RetrofitClient;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Handles drama data here, return values through callback
 *
 * */

public class DataManager implements Handler.Callback {

    public interface RequestListener {
        void onRequestNewDramaList(boolean successful, List<DramaList.Drama> dramaList);

        void onRequestDramaList(List<DramaList.Drama> dramaList);

        void onRequestDramaItem(DramaList.Drama drama);
    }

    private Context mContext;
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;
    private RequestListener listener;
    private final RetrofitAPI retrofitAPI;

    private static final int MSG_GET_DRAMAS = 1;
    private static final int MSG_GET_DRAMA_BY_ID = 2;
    private static final int MSG_GET_DRAMA_BY_NAME = 3;

    public DataManager(Context context, RequestListener listener) {
        this.mContext = context;
        this.listener = listener;
        retrofitAPI = RetrofitClient.getClient().create(RetrofitAPI.class);

        mHandlerThread = new HandlerThread("DataHandleBackgroundThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), this);
    }

    public void requestDramaList() {
        mHandler.obtainMessage(MSG_GET_DRAMAS).sendToTarget();
    }

    public void requestDramaItem(String name) {
        Message message = new Message();
        message.what = MSG_GET_DRAMA_BY_NAME;
        message.obj = name;
        mHandler.dispatchMessage(message);
    }

    public void requestDramaItem(int drama_id) {
        Message message = new Message();
        message.what = MSG_GET_DRAMA_BY_ID;
        message.arg1 = drama_id;
        mHandler.dispatchMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_GET_DRAMAS: {
                List<DramaList.Drama> dramaList = DramaContractUtils.getDramaList(mContext);
                listener.onRequestDramaList(dramaList);
                return true;
            }
            case MSG_GET_DRAMA_BY_ID: {
                DramaList.Drama drama = DramaContractUtils.queryDramaId(mContext, msg.arg1);
                listener.onRequestDramaItem(drama);
                return true;
            }
            case MSG_GET_DRAMA_BY_NAME: {
                List<DramaList.Drama> dramaList = DramaContractUtils.queryDramaName(mContext, (String) msg.obj);
                listener.onRequestDramaList(dramaList);
                return true;
            }
            default:
                return true;
        }
    }

    public void requestNewestDataList() {
        Call<DramaList> dramaListCall = retrofitAPI.loadDramaList();
        dramaListCall.enqueue(new Callback<DramaList>() {
            @Override
            public void onResponse(Call<DramaList> call, Response<DramaList> response) {
                DramaList dramaList = response.body();
                if (dramaList != null) {
                    DramaContractUtils.updateDramas(mContext, null, dramaList.list);
                    listener.onRequestNewDramaList(true, dramaList.list);
                }
            }

            @Override
            public void onFailure(Call<DramaList> call, Throwable t) {
                listener.onRequestNewDramaList(false, null);
                call.cancel();
            }
        });
    }
}

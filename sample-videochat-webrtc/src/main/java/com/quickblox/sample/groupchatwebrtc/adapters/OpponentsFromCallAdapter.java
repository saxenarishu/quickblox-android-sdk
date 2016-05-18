package com.quickblox.sample.groupchatwebrtc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.quickblox.sample.groupchatwebrtc.R;
import com.quickblox.sample.groupchatwebrtc.view.RTCGLVideoView;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * QuickBlox team
 */
public class OpponentsFromCallAdapter extends RecyclerView.Adapter<OpponentsFromCallAdapter.ViewHolder> {

    public static final int OPPONENT = 1;
    public static final int HOLDER = 2;

    private static final int NUM_IN_ROW = 3;
    private static final String TAG = OpponentsFromCallAdapter.class.getSimpleName();
    private final int itemHeight;
    private final int itemWidth;
    private final int paddingLeft = 0;

    private Context context;
    private List<QBUser> opponents;
    private int gridWidth;
    private boolean showVideoView;
    private LayoutInflater inflater;
    private int columns;
    private OnAdapterEventListener adapterListener;


    public OpponentsFromCallAdapter(Context context, List<QBUser> users, int width, int height,
                                    int gridWidth, int columns, int itemMargin,
                                    boolean showVideoView) {
        this.context = context;
        this.opponents = users;
        this.gridWidth = gridWidth;
        this.columns = columns;
        this.showVideoView = showVideoView;
        this.inflater = LayoutInflater.from(context);
        itemWidth = width;
        itemHeight = height;
//        setPadding(itemMargin);
        Log.d(TAG, "item width=" + itemWidth + ", item height=" + itemHeight);
    }

    private void setPadding(int itemMargin) {
        int allCellWidth = (itemWidth + (itemMargin * 2)) * columns;
        if ((allCellWidth < gridWidth) && ((gridWidth - allCellWidth) > (itemMargin * 2))) { //set padding if it makes sense to do it
//            paddingLeft = (gridWidth - allCellWidth) / 2;
        }
    }

    public void setAdapterListener(OnAdapterEventListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    @Override
    public int getItemCount() {
        return opponents.size();
    }

    public Integer getItem(int position) {
        return opponents.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_opponent_from_call, null);
//        TODO maybe it's no reason to do this
        v.findViewById(R.id.innerLayout).setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        if (paddingLeft != 0) {
            Log.d(TAG, "paddingLeft1=" + paddingLeft + ", v.getPaddingRight()1=" + v.getPaddingRight());
            v.setPadding(paddingLeft, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
        }
        ViewHolder vh = new ViewHolder(v);
        vh.setListener( new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onShowOpponent(int callerId) {
                Log.d("OpponentsAdapter", "onShowOpponent onClick");
                adapterListener.onItemClick(callerId);
//                vh.showOpponentView(false);
//                opponents.remove(caller);
////                notifyItemRemoved(caller);
//                notifyItemRangeChanged(caller, opponents.size());
            }
        });
        vh.showOpponentView(true);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QBUser user = opponents.get(position);

        holder.opponentsName.setText(user.getFullName());
        holder.setUserId(user.getId());
        if (position == (opponents.size() - 1)) {
            adapterListener.OnBindLastViewHolder(holder, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnAdapterEventListener {
        void OnBindLastViewHolder(ViewHolder holder, int position);
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView opponentsName;
        TextView connectionStatus;
        RTCGLVideoView opponentView;
        private int userId;
        private ViewHolderClickListener viewHolderClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            opponentsName = (TextView) itemView.findViewById(R.id.opponentName);
            connectionStatus = (TextView) itemView.findViewById(R.id.connectionStatus);
            opponentView = (RTCGLVideoView) itemView.findViewById(R.id.opponentView);
        }

        private void setListener(ViewHolderClickListener viewHolderClickListener){
            this.viewHolderClickListener = viewHolderClickListener;
        }

        public void setStatus(String status) {
            connectionStatus.setText(status);
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public RTCGLVideoView getOpponentView() {
            return opponentView;
        }

        public void showOpponentView(boolean show) {
            Log.d("OpponentsAdapter", "show? "+ show);
            opponentView.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onShowOpponent(getAdapterPosition());
        }

        public interface ViewHolderClickListener {
            void onShowOpponent(int callerId);
        }
    }
}

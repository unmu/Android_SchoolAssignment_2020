package com.example.fragment1;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StickyAdapter extends RecyclerView.Adapter<StickyAdapter.StickyViewHolder> {
    private Context mContext;
    private List<GroupData> mList;
    private OnItemClickListener mListener;

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    public StickyAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setStickyDataList(List<GroupData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StickyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        StickyViewHolder stickyViewHolder = new StickyViewHolder(view);
        return stickyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StickyViewHolder holder, int position) {
        final GroupData groupData = mList.get(position);
        holder.txtPlants.setText(groupData.plants);
        if (position == 0) {
            holder.txtSunlight.setVisibility(View.VISIBLE);
            holder.txtSunlight.setText(groupData.sunlight);
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(groupData.sunlight, mList.get(position-1).sunlight)) {
                holder.txtSunlight.setVisibility(View.VISIBLE);
                holder.txtSunlight.setText(groupData.sunlight);
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                holder.txtSunlight.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        holder.itemView.setContentDescription(groupData.sunlight);

        //点击事件传入addrFragment(Activity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(groupData.plants);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class StickyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSunlight, txtPlants;
        public StickyViewHolder(View view) {
            super(view);
            txtSunlight = view.findViewById(R.id.txtSunlight);
            txtPlants = view.findViewById(R.id.txtPlants);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String content);
    }

}

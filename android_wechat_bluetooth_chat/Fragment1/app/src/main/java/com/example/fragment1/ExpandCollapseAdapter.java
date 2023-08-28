package com.example.fragment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpandCollapseAdapter extends RecyclerView.Adapter<ExpandCollapseAdapter.ExpandCollapseViewHolder> {

    private static final String TAG = ExpandCollapseAdapter.class.getSimpleName();

    private Context mContext;

    private List<PersonData> mList = new ArrayList<>();

    private int expandedPosition = -1;

    private ExpandCollapseViewHolder mViewHolder;

    public ExpandCollapseAdapter(Context context, List<PersonData> list) {
        mContext = context;
        mList = list;
    }

    public void setExpandCollapseDataList(List<PersonData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpandCollapseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.expandcollapse_item, parent, false);
        return new ExpandCollapseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpandCollapseViewHolder holder, int position) {
        PersonData personData = mList.get(position);
        holder.txtName.setText(personData.name);
        holder.txtTel.setText("电话号码：" + personData.phoneNumber);
        holder.txtEmail.setText("邮箱地址：" + personData.emailAddress);

        final boolean isExpanded = position == expandedPosition;
        holder.rlChild.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewHolder != null) {
                    mViewHolder.rlChild.setVisibility(View.GONE);
                    notifyItemChanged(expandedPosition);
                }
                expandedPosition = isExpanded ? -1 : holder.getAdapterPosition();
                mViewHolder = isExpanded ? null : holder;
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ExpandCollapseViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlParent, rlChild;
        TextView txtName, txtTel, txtEmail;

        public ExpandCollapseViewHolder(View itemView) {
            super(itemView);
            rlParent = itemView.findViewById(R.id.rl_parent);
            rlChild = itemView.findViewById(R.id.rl_child);
            txtName = itemView.findViewById(R.id.txtName);
            txtTel = itemView.findViewById(R.id.txtTel);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());   // https://blog.csdn.net/qq_33829413/article/details/80067060?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1
    }

}
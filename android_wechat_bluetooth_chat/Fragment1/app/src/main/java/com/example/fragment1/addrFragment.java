package com.example.fragment1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class addrFragment extends Fragment implements StickyAdapter.OnItemClickListener {
    private Context context;
    private RecyclerView mRcv;
    private StickyAdapter stickyAdapter;
    private List<String> mList = new ArrayList<>();
    private List<GroupData> mDataList = new ArrayList<>();

    public addrFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_addr, container, false);
        context = view.getContext();
        initList();
        initData();
        initView(view);
        return view;
    }

    private void initList() {
        mList.add("阳光：0|小喷菇");
        mList.add("阳光：0|水兵菇");
        mList.add("阳光：25|土豆地雷");
        mList.add("阳光：25|阳光菇");
        mList.add("阳光：25|胆小菇");
        mList.add("阳光：25|莲叶");
        mList.add("阳光：25|缠绕水草");
        mList.add("阳光：25|路灯花");
        mList.add("阳光：25|花盆");
        mList.add("阳光：50|向日葵");
        mList.add("阳光：50|坚果");
        mList.add("阳光：50|窝瓜");
        mList.add("阳光：50|大蒜");
        mList.add("阳光：50|金盏花");
        mList.add("阳光：75|大喷菇");
        mList.add("阳光：75|咬咬碑");
        mList.add("阳光：75|迷糊菇");
        mList.add("阳光：75|冰川菇");
        mList.add("阳光：75|咖啡豆");
        mList.add("阳光：100|豌豆射手");
        mList.add("阳光：100|地刺");
        mList.add("阳光：100|三叶草");
        mList.add("阳光：100|磁力菇");
        mList.add("阳光：100|卷心菜投手");
        mList.add("阳光：100|玉米投手");
        mList.add("阳光：100|萝卜伞");
        mList.add("阳光：125|末日菇");
        mList.add("阳光：125|火爆辣椒");
        mList.add("阳光：125|高坚果");
        mList.add("阳光：125|仙人掌");
        mList.add("阳光：125|双向射手");
        mList.add("阳光：125|星星果");
        mList.add("阳光：125|南瓜头");
        mList.add("阳光：150|爆炸樱桃");
        mList.add("阳光：150|大嘴花");
        mList.add("阳光：175|寒冰射手");
        mList.add("阳光：175|火炬树桩");
        mList.add("阳光：200|双重射手");
        mList.add("阳光：300|西瓜投手");
        mList.add("阳光：325|三重射手");
    }

    private void initData() {
        for (int i = 0; i < mList.size(); i++) {
            GroupData groupData = new GroupData();
            String s = mList.get(i);
            String sunlight = s.substring(0, s.indexOf("|"));
            String plants = s.substring(s.indexOf("|") + 1, s.length());
            groupData.setSunlight(sunlight);
            groupData.setPlants(plants);
            mDataList.add(groupData);
        }
    }

    private void initView(View view) {
        stickyAdapter = new StickyAdapter(context, this);
        mRcv = view.findViewById(R.id.rcv_sticky);
        final TextView txtSunlight = view.findViewById(R.id.txtSunlight);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mRcv.setLayoutManager(manager);
        mRcv.setHasFixedSize(true);
        mRcv.setAdapter(stickyAdapter);

        stickyAdapter.setStickyDataList(mDataList);
        mRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View stickyView = recyclerView.findChildViewUnder(txtSunlight.getMeasuredWidth()/2, 5);
                if (stickyView != null && stickyView.getContentDescription() != null) {
                    txtSunlight.setText(String.valueOf(stickyView.getContentDescription()));
                }

                View transInfoView = recyclerView.findChildViewUnder(txtSunlight.getMeasuredWidth()/2, txtSunlight.getMeasuredHeight()+1);
                if (transInfoView != null && transInfoView.getTag() != null) {
                    int tranViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - txtSunlight.getMeasuredHeight();

                    if (tranViewStatus == StickyAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            txtSunlight.setTranslationY(dealtY);
                        } else {
                            txtSunlight.setTranslationY(0);
                        }
                    } else if (tranViewStatus == StickyAdapter.NONE_STICKY_VIEW) {
                        txtSunlight.setTranslationY(0);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(String content) {
        Toast.makeText(context, "这是 " + content, Toast.LENGTH_SHORT).show();
    }
}

package com.tencent.qchat.activity;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.model.StaffMsgData;
import com.tencent.qchat.model.StaffMsgRow;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.TimeUtil;
import com.tencent.qchat.utils.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 回答者消息通知页面
 * Created by cliffyan on 2016/8/30.
 */
public class StaffMsgActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.staff_msg_list)
    ListView mStaffMsgList;

    List<StaffMsgRow> mStaffMsgRows;
    StaffMsgListAdapter mAdapter;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    @Override
    public int initLayoutRes() {
        return R.layout.activity_msg_staff;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("通知");
        initView();
        getListDataFromNet();
    }

    private void initView() {
        mAdapter=new StaffMsgListAdapter();
        mStaffMsgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openWebActivity(mStaffMsgRows.get(position).getQuestion_url(), "问题详情");
                playOpenAnimation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }

    protected void getListDataFromNet() {
        RetrofitHelper.getInstance().getStaffMsgList(new Subscriber<StaffMsgData>() {


            @Override
            public void onCompleted() {
                    mStaffMsgList.setAdapter(mAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(StaffMsgData staffMsgData) {
                mStaffMsgRows = staffMsgData.getRows();
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    class StaffMsgListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mStaffMsgRows==null)
                return 0;
            else
                return mStaffMsgRows.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(superCtx, convertView, R.layout.staff_msg_item, position, parent);
            holder.setText(R.id.staff_msg_title, "\\u3000\\u3000\\u3000\\u3000"+mStaffMsgRows.get(position).getQuestion_content());
            holder.setText(R.id.staff_msg_time, TimeUtil.msecToString(mStaffMsgRows.get(position).getQuestion_time()));
            return holder.getConvertView();
        }
    }

}

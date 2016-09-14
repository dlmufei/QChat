package com.tencent.qchat.activity;


import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.tencent.qchat.utils.UserUtil;
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

    @BindView(R.id.no_item_tip)
    View mEmptyTip;



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
        mStaffMsgList.setEmptyView(mEmptyTip);
        mStaffMsgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //改变item状态，表示item已读
                TextView tv_staff_msg_title= (TextView)view.findViewById(R.id.staff_msg_title);
                tv_staff_msg_title.setTextColor(Color.rgb(184,184,184));
                if ( !mStaffMsgRows.get(position).getIs_fresh()){
                    openWebActivity(mStaffMsgRows.get(position).getQuestion_url(), "问题详情");
                    playOpenAnimation();
                }else if (UserUtil.isLogin(superCtx)&&UserUtil.getIsStaff(superCtx)){//跳转到回答界面
                    Intent intent = new Intent(StaffMsgActivity.this, NewAnswerActivity.class);
                    intent.putExtra(NewAnswerActivity.Q_ID,mStaffMsgRows.get(position).getQuestion_id());
                    intent.putExtra(NewAnswerActivity.Q_TITLE,mStaffMsgRows.get(position).getQuestion_content());
                    startActivityForResult(intent,NewAnswerActivity.REQ_CODE);
                    playOpenAnimation();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }

    protected void getListDataFromNet() {
        RetrofitHelper.getInstance().getStaffMsgList(UserUtil.getToken(this),new Subscriber<StaffMsgData>() {

            @Override
            public void onCompleted() {
                    mStaffMsgList.setAdapter(mAdapter);
            }

            @Override
            public void onError(Throwable e) {
                showToast("请检查网络连接");
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
            if (mStaffMsgRows.get(position).getIs_fresh()){
                holder.setTextAndColor(R.id.staff_msg_title, "\u3000\u3000\u3000"+mStaffMsgRows.get(position).getQuestion_content(),Color.rgb(50,87,153));
            }else {
                holder.setTextAndColor(R.id.staff_msg_title, "\u3000\u3000\u3000"+mStaffMsgRows.get(position).getQuestion_content(),Color.rgb(184,184,184));
            }

            holder.setText(R.id.staff_msg_time, TimeUtil.msecToString(System.currentTimeMillis()-mStaffMsgRows.get(position).getQuestion_time()*1000));
            return holder.getConvertView();
        }
    }

}

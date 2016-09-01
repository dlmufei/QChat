package com.tencent.qchat.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.BaseAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息通知页面
 * Created by cliffyan on 2016/8/30.
 */
public class MsgActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.msg_list)
    ListView mMsgList;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    @Override
    public int initLayoutRes() {
        return R.layout.activity_msg;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("通知");



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }

    private void initListView(){
        mMsgList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
    }

}

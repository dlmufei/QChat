package com.tencent.qchat.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.BaseAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邀请回答者列表页
 *
 * Created by cliffyan on 2016/8/30.
 */
public class InviteActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.more_tv)
    TextView mPostView;

    @BindView(R.id.invite_list)
    ListView mInviteList;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    @Override
    public int initLayoutRes() {
        return R.layout.acvity_invite;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("邀请回答者");
        mPostView.setText("保存");



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }


    private void initListView(){
        mInviteList.setAdapter(new BaseAdapter() {
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

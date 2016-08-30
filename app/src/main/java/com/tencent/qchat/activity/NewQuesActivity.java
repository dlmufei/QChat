package com.tencent.qchat.activity;

import android.widget.TextView;

import com.tencent.qchat.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hiwang on 16/8/28.
 */
public class NewQuesActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.more_tv)
    TextView mPostView;

    @OnClick(R.id.more_tv)
    protected void to_post_ques(){

    }

    @OnClick(R.id.invite)
    protected void to_invite(){
        openActivity(InviteActivity.class);
        playOpenAnimation();
    }


    @Override
    public int initLayoutRes() {
        return R.layout.activity_new_ques;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("提问");
        mPostView.setText("发布");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }
}

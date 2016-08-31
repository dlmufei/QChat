package com.tencent.qchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qchat.R;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_selectStaff)
    TextView mSelectStaffText;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    private ArrayList<Integer> ids;
    private ArrayList<String> avatars;

    @OnClick(R.id.more_tv)
    protected void to_post_ques(){
        Toast.makeText(this,"发布",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.invite)
    protected void to_invite(){
        openInviteActivity(ids);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode==InviteActivity.RESULT_SUC && requestCode==InviteActivity.REQ_CODE){

           Bundle bundle=data.getExtras().getBundle(InviteActivity.DATA);
           avatars=bundle.getStringArrayList(InviteActivity.AVATARS);
           ids=bundle.getIntegerArrayList(InviteActivity.IDS);

           mSelectStaffText.setText("已选择"+ids.size()+"位回答者");

       }
        Log.i("ids",ids==null?"null":ids.toString());
        Log.i("avatars",avatars==null?"null":avatars.toString());
    }

    private void openInviteActivity(ArrayList<Integer> ids){
        Intent intent = new Intent(this, InviteActivity.class);
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList(InviteActivity.IDS,ids);
        intent.putExtra(InviteActivity.DATA,bundle);
        startActivityForResult(intent,InviteActivity.REQ_CODE);

    }

}

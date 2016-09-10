package com.tencent.qchat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.utils.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscriber;

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
    @BindView(R.id.gv_selectStaff)
    GridView mSelectStaffGrid;
    @BindView(R.id.et_question)
    EditText mQuestionEdit;
    @BindView(R.id.tv_textNum)
    TextView mTextNum;

    @OnTextChanged(R.id.et_question)
    protected void to_textNumChange(){
        mTextNum.setText(mQuestionEdit.getText().length()+"/80");
    }

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    private ArrayList<Integer> ids;
    private ArrayList<String> avatars;

    @OnClick(R.id.more_tv)
    protected void to_post_ques(){
        String content=mQuestionEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            showToast("少年，请先输入问题");
            return;
        }
        if (ids==null || ids.size()<1){
            showToast("少年，请邀请至少一名回答者");
            return;
        }

        RetrofitHelper.getInstance().addQuestion(UserUtil.getToken(this), content, ids, new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast(e.getMessage());
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                Log.i("jsonObject",jsonObject.toString());//TODO
                showToast("发布成功");
                NewQuesActivity.this.finish();

                //TODO
                /*if (jsonObject.get("code").getAsInt()!=0){
                    showToast(jsonObject.get("message").getAsString());
                }else {
                    showToast("发布成功");
                    NewQuesActivity.this.finish();
                }*/
            }
        });
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
           if (ids!=null&& ids.size()>0){
               mSelectStaffText.setText("已选择"+ids.size()+"位回答者");
               mSelectStaffText.setTextColor(Color.rgb(50,87,153));
           }else {
               mSelectStaffText.setText("请选择问题的回答者");
               mSelectStaffText.setTextColor(Color.BLACK);
           }

           if (avatars!=null){
               ArrayList<HashMap<String, Object>> imagelist = new ArrayList<>();
               for (String avatar :avatars){
                   HashMap<String, Object> map = new HashMap<>();
                   map.put("url",avatar);
                   imagelist.add(map);
               }
               SimpleAdapter simpleAdapter = new SimpleAdapter(this, imagelist,
                       R.layout.select_staff_grid_item, new String[] { "url"}, new int[] {
                       R.id.selectedstaff_avatar});
               mSelectStaffGrid.setAdapter(simpleAdapter);
           }

       }
    }

    private void openInviteActivity(ArrayList<Integer> ids){
        Intent intent = new Intent(this, InviteActivity.class);
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList(InviteActivity.IDS,ids);
        bundle.putStringArrayList(InviteActivity.AVATARS,avatars);
        intent.putExtra(InviteActivity.DATA,bundle);
        startActivityForResult(intent,InviteActivity.REQ_CODE);

    }

}

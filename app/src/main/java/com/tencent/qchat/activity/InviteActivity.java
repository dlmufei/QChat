package com.tencent.qchat.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.model.StaffData;
import com.tencent.qchat.model.StaffRow;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 邀请回答者列表页
 * <p/>
 * Created by cliffyan on 2016/8/30.
 */
public class InviteActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.more_tv)
    TextView mSaveView;
    @BindView(R.id.invite_list)
    ListView mInviteList;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }
    @OnClick(R.id.more_tv)
    protected void to_save() {
        Toast.makeText(InviteActivity.this,"保存",Toast.LENGTH_SHORT).show();
    }

    List<StaffRow> mStaffRows;
    StaffListAdapter mAdapter;

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
        mSaveView.setText("保存");
        initView();
        getListDataFromNet();

    }

    private void initView(){

        mAdapter = new StaffListAdapter();
        mInviteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(InviteActivity.this,"显示详情页",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }

    protected void getListDataFromNet() {
        RetrofitHelper.getInstance().getStaffList(new Subscriber<StaffData>() {

            @Override
            public void onCompleted() {
                mInviteList.setAdapter(mAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(StaffData staffData) {
                mStaffRows = staffData.getRows();
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    class StaffListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mStaffRows == null)
                return 0;
            else
                return mStaffRows.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(superCtx, convertView, R.layout.staff_list_item, position, parent);
            holder.setImageUrl(R.id.staff_avatar, Uri.parse(mStaffRows.get(position).getAvatar()));
            holder.setText(R.id.staff_nick, mStaffRows.get(position).getNickname());
            holder.setText(R.id.staff_title, mStaffRows.get(position).getTitle());
            holder.setText(R.id.staff_skill, mStaffRows.get(position).getSkilled_field());
            return holder.getConvertView();
        }
    }



}

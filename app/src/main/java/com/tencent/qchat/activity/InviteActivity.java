package com.tencent.qchat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.model.StaffData;
import com.tencent.qchat.model.StaffRow;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.ViewHolder;

import java.util.ArrayList;
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

    public static final String DATA="data";
    public static final String IDS="ids";
    public static final String AVATARS="avatars";
    public static final int REQ_CODE=0x01;
    public static final int RESULT_SUC=0x02;
    public static final int RESULT_ERR=0x03;

    private ArrayList<Integer> ids=new ArrayList<>();
    private ArrayList<String> avatars=new ArrayList<>();

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }
    @OnClick(R.id.more_tv)
    protected void to_save() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList(IDS,ids);
        bundle.putStringArrayList(AVATARS,avatars);
        intent.putExtra(DATA,bundle);
        if (ids==null||ids.size()==0)
            setResult(RESULT_ERR);
        else{
            setResult(RESULT_SUC,intent);
        }
        this.finish();
    }





    List<StaffRow> mStaffRows;
    StaffListAdapter mAdapter;

    @Override
    public int initLayoutRes() {
        return R.layout.acvity_invite;
    }

    @Override
    public void onWillLoadView() {
        ids = getIntent().getExtras().getBundle(DATA).getIntegerArrayList(IDS);
        Log.i("ids",ids==null?"null":ids.toString());
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
                Toast.makeText(InviteActivity.this,"显示详情页"+position,Toast.LENGTH_SHORT).show();

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
            if (ids!=null&& ids.contains(mStaffRows.get(position).getId())){
                holder.setCheckBox(R.id.cb_staff,true);
            }else {
                holder.setCheckBox(R.id.cb_staff,false);
            }
            final int p = position;
            holder.getView(R.id.cb_staff).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    if (ids==null) ids=new ArrayList<>();
                    if (cb.isChecked()){
                        if (!ids.contains(mStaffRows.get(p).getId())){
                            ids.add(mStaffRows.get(p).getId());
                            avatars.add(mStaffRows.get(p).getAvatar());
                        }
                    }else {
                        if (ids.contains(mStaffRows.get(p).getId())){
                            ids.remove((Integer) mStaffRows.get(p).getId());
                            avatars.remove(mStaffRows.get(p).getAvatar());
                        }
                    }
                }
            });

            return holder.getConvertView();
        }
    }



}

package com.tencent.qchat.activity;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.model.Data;
import com.tencent.qchat.model.Row;
import com.tencent.qchat.utils.ScreenUtil;
import com.tencent.qchat.utils.TimeUtil;
import com.tencent.qchat.utils.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by hiwang on 16/9/1.
 * <p/>
 * 普通用户的通知列表
 */
public class UserMsgActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    TextView mTitleView;


    List<Row> mMsgList;
    MsgAdapter mAdapter;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_msg_user;
    }

    @Override
    public void onWillLoadView() {
        refreshListDataFromNet();
    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("通知");
        mAdapter = new MsgAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(mAdapter.new MsgDivider(ScreenUtil.dp2px(this, 10)));
        mRecyclerView.setAdapter(mAdapter);

    }

    protected void refreshListDataFromNet() {
        RetrofitHelper.getInstance().getUserMsgList(UserUtil.getToken(this), new Subscriber<Data>() {
            @Override
            public void onCompleted() {
                showToast("获取成功");
            }

            @Override
            public void onError(Throwable e) {
                showToast("加载失败");
            }

            @Override
            public void onNext(Data data) {
                mMsgList = data.getRows();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    protected class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgHolder> {


        @Override
        public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MsgHolder(LayoutInflater.from(superCtx).inflate(R.layout.list_item_msg_user, null));
        }

        @Override
        public void onBindViewHolder(MsgHolder holder, final int position) {
            Row row = mMsgList.get(position);
            MsgHolder msgHolder = holder;
            msgHolder.qContent.setText(row.getQuestionContent().replaceAll("(\r\n)+", "\n"));
            msgHolder.aNick.setText(row.getAnswerLead().getUserNickname());
            msgHolder.aTitle.setText(" · " + row.getAnswerLead().getUserTitle());
            msgHolder.aTime.setText(TimeUtil.msecToString(row.getQuestionTime()));
            msgHolder.aContent.setText(row.getAnswerLead().getAnswerContent().replaceAll("(\r\n)+", "\n"));
            if (row.getAnswerCount() <= 1) {
                msgHolder.qCountLayout.setVisibility(View.GONE);
            } else {
                msgHolder.qCountLayout.setVisibility(View.VISIBLE);
                msgHolder.aCount.setText("还有" + (row.getAnswerCount() - 1) + "个回答");
            }

            msgHolder.aAvatar.setImageURI(Uri.parse(row.getAnswerLead().getUserAvatar()));
//            msgHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openWebActivity(mMsgList.get(position - 1).getQuestionUrl(), "问题详情");
//                    playOpenAnimation();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mMsgList == null ? 0 : mMsgList.size();
        }

        public class MsgHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.q_content)
            TextView qContent;
            @BindView(R.id.a_nick)
            TextView aNick;
            @BindView(R.id.a_title)
            TextView aTitle;
            @BindView(R.id.a_time)
            TextView aTime;
            @BindView(R.id.a_content)
            TextView aContent;
            @BindView(R.id.a_count)
            TextView aCount;
            @BindView(R.id.a_avatar)
            SimpleDraweeView aAvatar;
            @BindView(R.id.a_count_layout)
            LinearLayout qCountLayout;

            public MsgHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class MsgDivider extends RecyclerView.ItemDecoration {

            private int mOffset;

            public MsgDivider(int offset) {
                mOffset = offset;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildLayoutPosition(view);
                if (pos == 0 || pos == mAdapter.getItemCount() - 1) {
                    outRect.bottom = 0;
                } else {
                    outRect.bottom = mOffset;
                }
            }
        }
    }

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
        playExitAnimation();
    }
}

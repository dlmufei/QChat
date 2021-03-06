package com.tencent.qchat.activity;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
 * 我的通知--我的回答  界面
 */
public class MyQuesAnswerListActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.no_item_tip)
    LinearLayout mNoItemTip;

    List<Row> mQAList;
    MQAAdapter mAdapter;

    public static final String TYPE = "qa_type";
    public static final int TYPE_QUES = 0x01, TYPE_ANSWER = 0x02;
    protected int mType;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_ques_answer;
    }

    @Override
    public void onWillLoadView() {
        mType = getIntent().getIntExtra(TYPE, 0);
        refreshListDataFromNet();
    }

    @Override
    public void onDidLoadView() {
        switch (mType) {
            case TYPE_QUES:
                mTitleView.setText("我的提问");
                break;
            case TYPE_ANSWER:
                mTitleView.setText("我的回答");
                break;
        }
        mAdapter = new MQAAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(mAdapter.new MsgDivider(ScreenUtil.dp2px(this, 10)));
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void refreshListDataFromNet() {
        switch (mType) {
            case TYPE_QUES:
                RetrofitHelper.getInstance().getMyQuesList(UserUtil.getToken(this), new Subscriber<Data>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Data data) {
                        mQAList = data.getRows();
                        if (mQAList==null || mQAList.size()==0){
                            mNoItemTip.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }else {
                            mNoItemTip.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }
                        onCreate(null);

                    }
                });
                break;
            case TYPE_ANSWER:
                RetrofitHelper.getInstance().getMyAnswerList(UserUtil.getToken(this), new Subscriber<Data>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(System.out);
                    }

                    @Override
                    public void onNext(Data data) {
                        mQAList = data.getRows();
                        if (mQAList==null || mQAList.size()<1){
                            mNoItemTip.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }else {
                            mNoItemTip.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }
                        onCreate(null);


                    }
                });
                break;
        }

    }

    protected class MQAAdapter extends RecyclerView.Adapter<MQAAdapter.MQAHolder> {


        @Override
        public MQAHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MQAHolder(LayoutInflater.from(superCtx).inflate(R.layout.list_item_ques_answer, null));
        }

        @Override
        public void onBindViewHolder(MQAHolder holder, final int position) {
            Row row = mQAList.get(position);
            MQAHolder msgHolder = holder;
            switch (mType) {
                case TYPE_QUES:
                    msgHolder.mTypeView.setText("你的提问");
                    break;
                case TYPE_ANSWER:
                    msgHolder.mTypeView.setText("你的回答");
                    break;
            }
            msgHolder.qContent.setText(row.getQuestionContent().replaceAll("(\r\n)+", "\n"));
            msgHolder.aNick.setText(row.getAnswerLead().getUserNickname());
            msgHolder.aTitle.setText(" · " + row.getAnswerLead().getUserTitle());
            msgHolder.aTime.setText(TimeUtil.msecToString(System.currentTimeMillis()-row.getQuestionTime()*1000));
            // 这里防止没有回答的情况下报NullPointerException
            if (row.getAnswerLead() != null && row.getAnswerLead().getAnswerContent() != null) {
                msgHolder.aContent.setText("\u3000\u3000\u3000"+row.getAnswerLead().getAnswerContent().replaceAll("(\r\n)+", "\n"));
                if (row.getAnswerCount() <= 1) {
                    msgHolder.qCountLayout.setVisibility(View.GONE);
                } else {
                    msgHolder.qCountLayout.setVisibility(View.VISIBLE);
                    msgHolder.aCount.setText("还有" + (row.getAnswerCount() - 1) + "个回答");
                }
                msgHolder.aAvatar.setImageURI(Uri.parse(row.getAnswerLead().getUserAvatar()));
            }else{
                msgHolder.aAnswerLayout.setVisibility(View.GONE);
            }
            msgHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWebActivity(mQAList.get(position).getQuestionUrl(), "问题详情");
                    playOpenAnimation();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mQAList == null ? 0 : mQAList.size();
        }

        public class MQAHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.type)
            TextView mTypeView;
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
            @BindView(R.id.answer_layout)
            LinearLayout aAnswerLayout;

            public MQAHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                outRect.bottom = mOffset;
            }
        }
    }

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
        playExitAnimation();
    }
}

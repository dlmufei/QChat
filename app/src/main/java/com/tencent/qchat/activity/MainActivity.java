package com.tencent.qchat.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.model.Banner;
import com.tencent.qchat.model.Data;
import com.tencent.qchat.model.Row;
import com.tencent.qchat.receiver.NetWorkListener;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.NetworkChecker;
import com.tencent.qchat.utils.ScreenUtil;
import com.tencent.qchat.utils.TimeUtil;
import com.tencent.qchat.utils.UserUtil;
import com.tencent.qchat.utils.ViewHolder;
import com.tencent.qchat.widget.RefreshLayout;
import com.tencent.qchat.widget.SlideMenu;
import com.tencent.qchat.widget.TopHintView;
import com.tencent.qchat.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements RefreshLayout.OnRefreshListener {

    protected int[] mMenuIcon = new int[]{R.drawable.menu_qa, R.drawable.menu_exit};
    protected String[] mMenuTextComm = new String[]{"我的提问", "退出登录"};
    protected String[] mMenuTextStaff = new String[]{"我的回答", "退出登录"};
    protected String[] mMenuTextVisitor = new String[]{"立即登录"};

    protected final int LIMIT = 10;//每次刷新获取的数据条数
    protected int mOffset = 0;//已经上拉加载的次数

    @BindView(R.id.menu_list)
    ListView mMenuList;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.slide_menu)
    SlideMenu mSlideMenu;
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.top_hint)
    TopHintView mHintView;

    @BindView(R.id.new_ques_btn)
    ImageButton mNewQuesBtn;
    @BindView(R.id.reddot)
    ImageView mRedDot;

    @OnClick(R.id.new_ques_btn)
    protected void to_new_ques() {
        mRedDot.setVisibility(View.INVISIBLE);
        openActivity(NewQuesActivity.class);
        playOpenAnimation();
    }

    MainAdapter mAdapter;
    List<Row> mQRowList = new ArrayList<>();
    Banner mBannerData;

    int lastItemCount=0;//上一次下拉后拿到的item总数


    protected NetWorkListener mNetChangeReceiver;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onWillLoadView() {
        mNetChangeReceiver = new NetWorkListener() {
            @Override
            public void onNetError() {
                mRefreshLayout.setRefreshable(false);
                mHintView.setPromptText("网络连接失败");
            }

            @Override
            public void onNetOk() {
                mRefreshLayout.setRefreshable(true);
            }
        };
        registerReceiver(mNetChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onDidLoadView() {
//        ViewServer.get(this).addWindow(this);
        setFullScreen();
        initMenu();
        initContentView();
        refreshListDataFromNet();
    }


    /**
     * 初始化Toolbar和主界面的内容
     */
    private void initContentView() {

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MainAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(mAdapter.new MainDivider(ScreenUtil.dp2px(this, 10)));
        mRecyclerView.setAdapter(mAdapter);

        if (UserUtil.isLogin(superCtx)) {
            getMsgCountRepeat();
        }

    }

    /**
     * 初始化Drawer和左侧菜单栏
     */
    private void initMenu() {

        mMenuList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return UserUtil.isLogin(superCtx) ? 2 : 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = ViewHolder.get(superCtx, convertView, R.layout.menu_list_item, position, parent);
                holder.setImageResource(R.id.icon, mMenuIcon[position]);
                if (UserUtil.isLogin(superCtx)) {
                    if (UserUtil.getIsStaff(superCtx)) {
                        holder.setImageResource(R.id.icon, mMenuIcon[position]);
                        holder.setText(R.id.text, mMenuTextStaff[position]);
                    } else {
                        holder.setImageResource(R.id.icon, mMenuIcon[position]);
                        holder.setText(R.id.text, mMenuTextComm[position]);
                    }
                } else {
                    holder.setImageResource(R.id.icon, mMenuIcon[position]);
                    holder.setText(R.id.text, mMenuTextVisitor[position]);
                }
                return holder.getConvertView();
            }
        });
        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (UserUtil.isLogin(superCtx)) {
                            Intent intent = new Intent(superCtx, MyQuesAnswerListActivity.class);
                            if (UserUtil.getIsStaff(superCtx)) {
                                intent.putExtra(MyQuesAnswerListActivity.TYPE, MyQuesAnswerListActivity.TYPE_ANSWER);
                            } else {
                                intent.putExtra(MyQuesAnswerListActivity.TYPE, MyQuesAnswerListActivity.TYPE_QUES);
                            }
                            startActivity(intent);
                            playOpenAnimation();
                        } else {
                            openActivity(WXEntryActivity.class);
                            playOpenAnimation();

                        }
                        break;
                    case 1:
                        UserUtil.clear(superCtx);
                        mSlideMenu.close();
                        ((BaseAdapter) mMenuList.getAdapter()).notifyDataSetChanged();
                        showToast("退出成功");
                        openActivity(WXEntryActivity.class);
                        break;
                }
                mSlideMenu.close();
            }
        });
    }

    protected void getMsgCountRepeat() {

        RetrofitHelper.getInstance().getMsgCount(UserUtil.getToken(superCtx), new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JsonObject data) {
                if (data.get("total").getAsInt() > 0) {
                    mRedDot.setVisibility(View.VISIBLE);
                } else {
                    mRedDot.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    protected void refreshListDataFromNet() {
        boolean isConnected = NetworkChecker.IsNetworkAvailable(superCtx);
        if (!isConnected) {
            mHintView.setPromptText("网络连接已断开");
            mHintView.setVisibility(View.VISIBLE);
            mRefreshLayout.refreshDownComplete();
            return;
        }

        RetrofitHelper.getInstance().getQList(mOffset, LIMIT, mOffset == 0 ? 1 : 0, new Subscriber<Data>() {

            @Override
            public void onCompleted() {
                mOffset= mQRowList.size();
                //TODO
                Log.i("mOffset-onCompleted",mOffset+"");
                Log.i("lastItemCount-onCompleted",lastItemCount+"");
                Log.i("mQRowList.size-onCompleted",mQRowList.size()+"");

                mRefreshLayout.refreshDownComplete();
                if (mQRowList.size()>lastItemCount){
                    mHintView.setPromptText("获得" + mQRowList.size() + "条消息");
                }
            }

            @Override
            public void onError(Throwable e) {
                mHintView.setPromptText("请检查网络连接");
                mRefreshLayout.refreshDownComplete();
            }

            @Override
            public void onNext(Data data) {
                //Log.i("list",data.toString());//TODO
                Log.i("mOffset-onNext",mOffset+"");
                Log.i("data.getRows-onNext",data.getRows().size()+"");
                Log.i("mQRowList.size-onNext",mQRowList.size()+"");

                if (mOffset == 0) {
                    mQRowList.clear();
                }
                lastItemCount=mQRowList.size();//记录上一次的数量
                mQRowList.addAll(data.getRows());
                if(data.getBanners()!=null&&data.getBanners().size()>0){
                    mBannerData=data.getBanners().get(0);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefreshDown() {
        mOffset = 0;
        refreshListDataFromNet();
    }

    @Override
    public void onRefreshUp() {
//        if (newStartIndex==0){
//            mOffset+=LIMIT;
//        }else {
//            mOffset=newStartIndex;
//        }



        refreshListDataFromNet();
    }

    public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_BANNER = 0X01;
        private static final int TYPE_ITEM = 0x02;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_BANNER:
                    return new BannerHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_list_banner, parent, false));
                case TYPE_ITEM:
                    return new MainHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_list_item, parent, false));
            }
            throw new NullPointerException("error return null in function onCreateViewHolder");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_BANNER:
                    BannerHolder bannerHolder = (BannerHolder) holder;
                    bannerHolder.bannerView.setImageURI(mBannerData.getAvatar());
                    bannerHolder.bannerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWebActivity(mBannerData.getUrl(), null);
                        }
                    });
                    break;
                case TYPE_ITEM:
                    final int pos = mBannerData == null ? position : position - 1;
                    Row row = mQRowList.get(pos);
                    MainHolder mainHolder = (MainHolder) holder;
                    mainHolder.qContent.setText(row.getQuestionContent().replaceAll("(\r\n)+", "\n"));
                    // 这里防止没有回答的情况下报NullPointerException
                    if(row.getAnswerLead()!=null&&row.getAnswerLead().getAnswerContent()!=null){
                        mainHolder.aNick.setText(row.getAnswerLead().getUserNickname());
                        mainHolder.aTitle.setText(" · " + row.getAnswerLead().getUserTitle());
                        mainHolder.aTime.setText(TimeUtil.msecToString(System.currentTimeMillis()-row.getQuestionTime()*1000));
                        mainHolder.aContent.setText("\u3000\u3000\u3000" + row.getAnswerLead().getAnswerContent().replaceAll("(\r\n)+", "\n"));
                        if (row.getAnswerCount() <= 1) {
                            mainHolder.qCountLayout.setVisibility(View.GONE);
                        } else {
                            mainHolder.qCountLayout.setVisibility(View.VISIBLE);
                            mainHolder.aCount.setText("还有" + (row.getAnswerCount() - 1) + "个回答");
                        }
                        mainHolder.aAnswerLayout.setVisibility(View.VISIBLE);
                    }else{
                        mainHolder.aAnswerLayout.setVisibility(View.GONE);
                    }

                    mainHolder.aAvatar.setImageURI(Uri.parse(row.getAnswerLead().getUserAvatar()));
                    mainHolder.qHot.setVisibility(View.VISIBLE);
                    if (TimeUtil.isNewPost(row.getQuestionTime())) {
                        mainHolder.qHot.setImageResource(R.mipmap.label_new);
                    } else if (row.getQuestionIsHot()) {
                        mainHolder.qHot.setImageResource(R.mipmap.label_hot);
                    } else {
                        mainHolder.qHot.setVisibility(View.GONE);
                    }
                    mainHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWebActivity(mQRowList.get(pos).getQuestionUrl(), "问题详情");
                            playOpenAnimation();
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && mBannerData != null) {
                return TYPE_BANNER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            if (mQRowList == null) {
                return 0;
            }
            if (mBannerData != null) {
                return mQRowList.size() + 1;
            }
            return mQRowList.size();
        }

        public class MainHolder extends RecyclerView.ViewHolder {

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
            @BindView(R.id.q_hot)
            ImageView qHot;
            @BindView(R.id.a_avatar)
            SimpleDraweeView aAvatar;
            @BindView(R.id.a_count_layout)
            LinearLayout qCountLayout;
            @BindView(R.id.answer_layout)
            LinearLayout aAnswerLayout;

            public MainHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class BannerHolder extends RecyclerView.ViewHolder {

            public SimpleDraweeView bannerView;

            public BannerHolder(View itemView) {
                super(itemView);
                bannerView = (SimpleDraweeView) itemView;
            }
        }

        public class MainDivider extends RecyclerView.ItemDecoration {

            private int mOffset;

            public MainDivider(int offset) {
                mOffset = offset;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildLayoutPosition(view);
                if ((pos == 0 && mBannerData != null) || pos == mAdapter.getItemCount() - 1) {
                    outRect.bottom = 0;
                } else {
                    outRect.bottom = mOffset;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mSlideMenu.isOpened()) {
            mSlideMenu.close();
        } else {
            moveTaskToBack(true);
//            super.onBackPressed();
        }
    }

    @OnClick(R.id.logo)
    void to_logo() {
        mSlideMenu.toggle();
    }

    @OnClick(R.id.msg)
    void to_msg() {
        if (UserUtil.isLogin(this)) {
            if (UserUtil.getIsStaff(this)) {//员工
                openActivity(StaffMsgActivity.class);
            } else {//普通
                openActivity(UserMsgActivity.class);
            }
            playOpenAnimation();
        } else {
            showToast("请先登录");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetChangeReceiver);
//        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ViewServer.get(this).setFocusedWindow(this);
        // activity苏醒时需要根据当前登录状态更新menu列表的数据
        if (mMenuList.getAdapter() != null) {
            BaseAdapter adapter = (BaseAdapter) mMenuList.getAdapter();
            adapter.notifyDataSetChanged();
        }
        //主界面重新显示时，要刷新当前是否显示提问按钮
        mNewQuesBtn.setVisibility(UserUtil.isLogin(this) && !UserUtil.getIsStaff(this) ? View.VISIBLE : View.GONE);
    }

}

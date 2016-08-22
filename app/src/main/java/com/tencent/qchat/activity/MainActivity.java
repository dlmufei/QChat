package com.tencent.qchat.activity;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.qchat.R;
import com.tencent.qchat.module.QList;
import com.tencent.qchat.module.Row;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.RetrofitHelper;
import com.tencent.qchat.utils.ScreenUtil;
import com.tencent.qchat.utils.TimeUtil;
import com.tencent.qchat.utils.ViewHolder;
import com.tencent.qchat.widget.SlideMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private int[] mMenuIcon = new int[]{R.drawable.menu_qa, R.drawable.menu_exit};
    private String[] mMenuText = new String[]{"我的提问", "退出登录"};

    @BindView(R.id.menu_list)
    ListView mMenuList;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.slide_menu)
    SlideMenu mSlideMenu;

    MainAdapter mAdapter;

    List<Row> mQRowList;
    Call<QList> mCaller;


    @Override
    public int initLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onWillLoadView() {
        mCaller = RetrofitHelper.getEndPointInterface().getQList();
        mCaller.enqueue(new Callback<QList>() {

            @Override
            public void onResponse(Call<QList> call, Response<QList> response) {
                if (mQRowList == null) {
                    mQRowList = response.body().getData().getRows();
                } else {
                    mQRowList.addAll(response.body().getData().getRows());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<QList> call, Throwable t) {
                showToast(getResources().getString(R.string.error_network));
            }
        });
    }

    @Override
    public void onDidLoadView() {
        initMenu();
        initContentView();
    }

    /**
     * 初始化Toolbar和主界面的内容
     */
    private void initContentView() {
        mAdapter = new MainAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(mAdapter.new MainDivider(ScreenUtil.dp2px(this, 10)));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化Drawer和左侧菜单栏
     */
    private void initMenu() {

        mMenuList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mMenuIcon.length;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = ViewHolder.get(superCtx, convertView, R.layout.menu_list_item, position, parent);
                holder.setImageResource(R.id.icon, mMenuIcon[position])
                        .setText(R.id.text, mMenuText[position]);
                return holder.getConvertView();
            }
        });
        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }
        });
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
                    bannerHolder.bannerView.setImageURI(Uri.parse("https://interesting.geeyan.com//uploads//images//c5//c1//96d012057c1492f5215ed79e5f94.jpg"));
                    break;
                case TYPE_ITEM:
                    Row row = mQRowList.get(position - 1);
                    MainHolder mainHolder = (MainHolder) holder;
                    mainHolder.qContent.setText(row.getQuestionContent().replaceAll("(\r\n)+", "\n"));
                    mainHolder.aNick.setText(row.getAnswerLead().getUserNickname());
                    mainHolder.aTitle.setText(" · " + row.getAnswerLead().getUserTitle());
                    mainHolder.aTime.setText(TimeUtil.msecToString(row.getQuestionTime()));
                    mainHolder.aContent.setText(row.getAnswerLead().getAnswerContent().replaceAll("(\r\n)+", "\n"));
                    if (row.getAnswerCount() <= 1) {
                        mainHolder.qCountLayout.setVisibility(View.GONE);
                    } else {
                        mainHolder.qCountLayout.setVisibility(View.VISIBLE);
                        mainHolder.aCount.setText("还有" + (row.getAnswerCount() - 1) + "个回答");
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
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
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
            return mQRowList.size() + 1;
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
                if (pos == 0 || pos == mAdapter.getItemCount()-1) {
                    outRect.bottom=0;
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
            moveTaskToBack(false);
            super.onBackPressed();
        }
    }

    @OnClick(R.id.logo)
    void to_logo() {
        mSlideMenu.toggle();
    }


}

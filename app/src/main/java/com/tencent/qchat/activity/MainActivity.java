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
        mRecyclerView.addItemDecoration(mAdapter.new MainDivider(ScreenUtil.dp2px(this,10)));
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

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

        @Override
        public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainHolder holder, int position) {
            Row row = mQRowList.get(position);
            holder.qContent.setText(row.getQuestionContent().replaceAll("(\r\n)+","\n"));
            holder.aNick.setText(row.getAnswerLead().getUserNickname());
            holder.aTitle.setText(" · "+row.getAnswerLead().getUserTitle());
            holder.aTime.setText(TimeUtil.msecToString(row.getQuestionTime()));
            holder.aContent.setText(row.getAnswerLead().getAnswerContent().replaceAll("(\r\n)+","\n"));
            holder.aCount.setText("还有"+(row.getAnswerCount()-1)+"个回答");
            holder.aAvatar.setImageURI(Uri.parse(row.getAnswerLead().getUserAvatar()));
            holder.qHot.setVisibility(View.VISIBLE);
            if (TimeUtil.isNewPost(row.getQuestionTime())) {
                holder.qHot.setImageResource(R.mipmap.label_new);
            } else if (row.getQuestionIsHot()) {
                holder.qHot.setImageResource(R.mipmap.label_hot);
            } else {
                holder.qHot.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (mQRowList == null) {
                return 0;
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

            public MainHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
        public class MainDivider extends RecyclerView.ItemDecoration{

            private int mOffset;
            public MainDivider(int offset){
                mOffset=offset;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view)==mAdapter.getItemCount()-1){
                    mOffset=0;
                }else{
                    outRect.bottom=mOffset;
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

package com.tencent.qchat.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.BaseAdapter;
import com.tencent.qchat.utils.ViewHolder;

import butterknife.BindView;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity{

    private int[] mMenuIcon = new int[]{R.drawable.menu_qa, R.drawable.menu_exit};
    private String[] mMenuText = new String[]{"我的提问", "退出登录"};

    @BindView(R.id.menu_list)
    ListView mMenuList;
    @BindView(R.id.main_content)
    FrameLayout mMainContent;
    @BindView(R.id.drawer)
    DrawerLayout mDrawer;

    FragmentManager mFragManager;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        initDrawerAndMenu();
        initToolbarAndContentView();
    }

    /**
     * 初始化Toolbar和主界面的内容
     */
    private void initToolbarAndContentView() {
        mFragManager = getSupportFragmentManager();

    }

    /**
     * 初始化Drawer和左侧菜单栏
     */
    private void initDrawerAndMenu() {
        mDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
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
                showToast(mMenuText[position]);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawers();
        }else{
            moveTaskToBack(false);
        }
    }
}

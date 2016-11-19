package com.hxy.skynet;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hxy.skynet.depend.StaticViewPager;
import com.hxy.skynet.fragment.Fragment1;
import com.hxy.skynet.fragment.Fragment2;
import com.hxy.skynet.fragment.Fragment3;
import com.hxy.skynet.fragment.Fragment4;
import com.hxy.skynet.until.Update;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StaticViewPager vp_main;
    private TabLayout tl_main;
    private String[] mTitles = new String[]{"运行状态", "数据记录", "报警信息", "设置"};
    private List<Fragment> mList;
    private Update update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        update=new Update(MainActivity.this);
        update.checkUpdate(0);
        initViews();
        initDatas();
    }

    private void initDatas() {
        vp_main.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });


        // 跟随ViewPager的页面切换
        tl_main.setupWithViewPager(vp_main);
        tl_main.getTabAt(0).setIcon(R.drawable.selector_ico01);
        tl_main.getTabAt(1).setIcon(R.drawable.selector_ico02);
        tl_main.getTabAt(2).setIcon(R.drawable.selector_ico03);
        tl_main.getTabAt(3).setIcon(R.drawable.selector_ico04);
        tl_main.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                if (position == 0) {
//
//                } else if (position == 1) {
//
//                } else if (position == 2) {
//
//                } else if (position == 3) {
//
//                }
                // 设置ViewPager的页面切换
                vp_main.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                if (position == 0) {
//
//                } else if (position == 1) {
//
//                } else if (position == 2) {
//
//                } else if (position == 3) {
//
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();

        Fragment1 userFragment = new Fragment1();
        fragments.add(userFragment);

        Fragment2 noteFragment = new Fragment2();
        fragments.add(noteFragment);

        Fragment3 contactFragment = new Fragment3();
        fragments.add(contactFragment);

        Fragment4 recordFragment = new Fragment4();
        fragments.add(recordFragment);

        return fragments;
    }
    private void initViews() {
        tl_main = (TabLayout) findViewById(R.id.tl_main);
        vp_main = (StaticViewPager) findViewById(R.id.view_pager);
        mList = initFragments();
        vp_main.setOffscreenPageLimit(0);
    }

}

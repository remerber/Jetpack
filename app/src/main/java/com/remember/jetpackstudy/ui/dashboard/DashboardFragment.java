package com.remember.jetpackstudy.ui.dashboard;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.remember.jetpackstudy.R;
import com.remember.jetpackstudy.databinding.FragmentDashboardBinding;
import com.remember.jetpackstudy.model.SofaTab;
import com.remember.jetpackstudy.ui.home.HomeFragment;
import com.remember.jetpackstudy.utils.AppConfig;
import com.remember.libnavannotation.FragmentDestination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

@FragmentDestination(pageUrl = "main/tabs/dash", asStarter = false)
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabLayoutMediator mediator;
    private Map<Integer,Fragment> mFragmentMap=new HashMap<>();
    private SofaTab tabConfig;
    private ArrayList<SofaTab.Tabs> tabs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentDashboardBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager=binding.viewPager;
        tabLayout=binding.tabLayout;

        tabConfig=getTabConfig();

        tabs=new ArrayList<>();

        for (SofaTab.Tabs tab : tabConfig.tabs) {
            if(tab.enable){
                tabs.add(tab);
            }
        }


        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        viewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(),this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment=mFragmentMap.get(position);
                if(fragment==null){
                    fragment=new HomeFragment();
                    mFragmentMap.put(position,fragment);
                }
                return fragment;
            }

            @Override
            public int getItemCount() {
                return tabs.size();
            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mediator=new TabLayoutMediator(tabLayout, viewPager, false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(makeTabView(position));
            }
        });
        mediator.attach();
        viewPager.registerOnPageChangeCallback(mPageChangeCallback);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(tabConfig.select);
            }
        });

    }

    private View  makeTabView(int position) {

//返回一个TextView  进行赋值
        TextView tabView = new TextView(getContext());
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(tabConfig.activeColor), Color.parseColor(tabConfig.normalColor)};
        ColorStateList stateList = new ColorStateList(states, colors);
        tabView.setTextColor(stateList);
        tabView.setText(tabs.get(position).title);
        tabView.setTextSize(tabConfig.normalSize);
        return tabView;
    }

    private SofaTab getTabConfig() {
        return AppConfig.getSofaTabConfig();
    }

    ViewPager2.OnPageChangeCallback mPageChangeCallback =new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
                int tabCount=tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab=tabLayout.getTabAt(i);
                TextView customView= (TextView) tab.getCustomView();
                if(tab.getPosition()==position){
                    customView.setTextSize(tabConfig.activeSize);
                    customView.setTypeface(Typeface.DEFAULT_BOLD);
                }else {
                    customView.setTextSize(tabConfig.normalSize);
                    customView.setTypeface(Typeface.DEFAULT);
                }
            }
        }
    };
}
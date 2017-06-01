package com.example.dell.actplus;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class list_fragment extends Fragment {

    private NetTools tool;
    private boolean first_start;
    private List<ActItem> listData;
    private int currentPage;
    private String currentType;
    private Myadpter myadpter;
    private final int UPDATE_CONTENT = 0;
    //设置正在加载,progress
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        //headerview(headerview ,推荐最新活动)
        PullToRefreshListView PTF_listView = (PullToRefreshListView)view.findViewById(R.id.PTF_listview);
        final ListView listView = PTF_listView.getRefreshableView();
        View headerView = View.inflate(getActivity().getApplicationContext(), R.layout.headerview, null);
        //设置仅可上拉刷新
        PTF_listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        PTF_listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
        PTF_listView.getLoadingLayoutProxy().setPullLabel("下拉加载更多");
        PTF_listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
        //设置上拉刷新监听
        PTF_listView.setOnRefreshListener(PullUpRefresh);
        PTF_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), listData.get(position-2).getTitle(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                actdetail act_detail = new actdetail();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.main_fragment, act_detail );
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //bug from pulltorefreshlistview. solve:http://blog.csdn.net/pk0071/article/details/50464247
        //add header view
        listView.addHeaderView(headerView);
        //初始化类

        listData = new ArrayList<>();
        tool = new NetTools();
        //获取初始数据
        try {
            currentPage = 0;
            currentType = "allList";
            dialog = ProgressDialog
                    .show(getActivity(), "亲别急", "活动正在加载中", false);
            UpdateDataAndUI(currentPage, 5, currentType);
            first_start = true;
        } catch (Exception e) {
            Log.e("On Create", e.toString());
        }
        return view;
    }
    public void UpdateDataAndUI(final int startPage, final int pageSize, final String dataType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ActItem> actItems = tool.getList(startPage, pageSize, dataType);
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = actItems;
                    handler.sendMessage(message);
                } catch(Exception e) {
                    Log.i("onCreate", e.toString());
                }
            }
        }).start();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CONTENT:
                    List<ActItem> data = (List<ActItem>) msg.obj;
                    updateUI(data);
                    break;
                default:
                    break;
            }
        }
    };
    private void updateUI(final List<ActItem> data) {
        try {
            listData = data;
            PullToRefreshListView listView = (PullToRefreshListView) getView().findViewById(R.id.PTF_listview);
            if (first_start == true) {
                myadpter = new Myadpter(getActivity().getApplicationContext(), listData);
                //内容加载完毕，开始显示内容
                setUpBanner();
                setUpRecyclerView();
                first_start = false;
                //若每次更新UI都是setAdapter就会不停地弹回顶部
                listView.setAdapter(myadpter);
                dialog.dismiss();
            } else {
                myadpter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("updateUI", e.toString());
        }
    }
    private void setUpRecyclerView() {
        List<Map<String, Object>> stringObjectList = new ArrayList<>();
        //res in local
        int optionImg[] = {R.drawable.all,R.drawable.love,R.drawable.sport,R.drawable.competition,
                R.drawable.play,R.drawable.outdoor,R.drawable.funny,R.drawable.teach };
        String optionNames[] = {"全部", "公益", "运动", "竞赛", "演出", "户外", "休闲", "讲座"};
        for (int i = 0; i < 8; i++) {
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("optionName", optionNames[i]);
            temp.put("optionImg", optionImg[i]);
            stringObjectList.add(temp);
        }
        //set up
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.option_list);
        OptionAdapter optionAdapter = new OptionAdapter(stringObjectList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(optionAdapter);
    }
    private void setUpBanner(){
        Banner banner = (Banner) getView().findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new MyImageLoader());
        //设置图片集合
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String temp = "http://actplus.sysuactivity.com/imgBase/poster/"+ listData.get(i).getActPosterName();
            images.add(temp);
        }
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String temp = "最新活动："+listData.get(i).getTitle();
            titles.add(temp);
        }
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
    private PullToRefreshBase.OnRefreshListener PullUpRefresh = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            new list_fragment.AsyncLoadData().execute();
        }
    };
    private class AsyncLoadData extends AsyncTask<Void, Void, List<ActItem>> {
        @Override
        protected List<ActItem> doInBackground(Void... params) {
            List<ActItem> list = null;
            try {
                currentPage += 1;
                list = tool.getList(currentPage, 5, currentType);
            } catch (Exception e) {
                Log.e("AsyncLoadData", e.toString());
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<ActItem> result) {
            //异步加载完成后
            Toast.makeText(getActivity().getApplicationContext(), "加载完成O(∩_∩)O",Toast.LENGTH_LONG).show();
            listData.addAll(result);
            myadpter.notifyDataSetChanged();
            PullToRefreshListView PTF_listView = (PullToRefreshListView)getView().findViewById(R.id.PTF_listview);
            PTF_listView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}

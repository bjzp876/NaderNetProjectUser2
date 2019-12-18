package com.nader.intelligent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.scene.SceneDetailActivity;
import com.nader.intelligent.activity.scene.SceneListActivity;
import com.nader.intelligent.adapter.BaseRecyclerAdapter;
import com.nader.intelligent.adapter.BaseViewHolder;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseFragment;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Scene;
import com.nader.intelligent.entity.vo.SceneVo;
import com.nader.intelligent.util.RecyclerViewSpacesItemDecoration;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.view.CircleView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 场景列表
 * author:zhangpeng
 * date: 2019/11/8
 */
public class SceneListFragment extends BaseFragment implements View.OnClickListener {
    private ImageView add;
    private ImageView backdrop;
    private House house;
    private SmartRefreshLayout refresh;
    private RecyclerView rv_scene;
    private List<Scene> sceneList = new ArrayList<>();
    private BaseRecyclerAdapter mAdapter;
    private int pageNo = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        gson = new Gson();
        view = inflater.inflate(R.layout.fragment_scenelist, container, false);
        initView();
        return view;
    }

    @Override
    protected void initView() {
        refresh = view.findViewById(R.id.refreshLayout);
        rv_scene = view.findViewById(R.id.recycleView);
        backdrop = view.findViewById(R.id.backdrop);
        mAdapter = new BaseRecyclerAdapter<Scene>(mContext, R.layout.scene_list_item, sceneList) {
            @Override
            public void convert(BaseViewHolder holder, final Scene scene) {
                holder.setText(R.id.name, scene.getName());
                holder.setText(R.id.description, scene.getDescription());
                final CircleView circleView = holder.getView(R.id.circleView);
                circleView.setEnable(scene.isEnable());
                holder.setOnClickListener(R.id.list_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(mContext, SceneDetailActivity.class);
                        intent.putExtra("scene", scene);
                        startActivityForResult(intent, Constants.REFRESH_SCENE);
                    }
                });
                if (circleView != null) {
                    circleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            circleView.execute();

                        }
                    });
                    circleView.setCallback(new CircleView.AnimateCallback() {
                        @SuppressWarnings("ConstantConditions")
                        @Override
                        public void onStop() {
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("sceneId", scene.getSceneId());
                                    apiConfig.run(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<BaseResponse>() {
                                        @Override
                                        public void call(BaseResponse baseResponse) {
                                            if (baseResponse.isSuccess()) {
                                                linkToast(mContext, "执行成功");
                                            }
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Log.e(TAG, "error message:" + throwable.getMessage());
                                        }
                                    });
                                }
                            };
                            thread.start();
                        }
                    });

                }
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv_scene.setLayoutManager(manager);
        rv_scene.setAdapter(mAdapter);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(20);
        rv_scene.addItemDecoration(recyclerViewSpacesItemDecoration);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                addData();
            }
        });
        //设置 Header 为 贝塞尔雷达 样式
        refresh.setRefreshHeader(new MaterialHeader(mContext));
        loadData();
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);

        backdrop.setImageResource(R.drawable.ic_backdrop);
    }

    @Override
    protected void pauseData() {

    }

    @Override
    protected void loadData() {
        pageNo = 1;
        house = new House();
        house = gson.fromJson((String) SettingUtils.get(mContext, "house"), House.class);
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", "57b917c100f911ea915600163e0406b6");
        map.put("pageNo", pageNo);
        map.put("pageSize", 20);
        apiConfig.getSceneByUser(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<SceneVo>() {
            @Override
            public void call(SceneVo sceneVo) {
                refresh.finishRefresh();
                sceneList.clear();
                sceneList.addAll(sceneVo.getData().getData());
                mHandler.sendEmptyMessage(0);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                refresh.finishRefresh();
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                try {
                    linkToast(mContext, responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addData() {
        pageNo += 1;
        house = gson.fromJson((String) SettingUtils.get(mContext, "house"), House.class);
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", "57b917c100f911ea915600163e0406b6");
        map.put("pageNo", pageNo);
        map.put("pageSize", 20);
        apiConfig.getSceneByUser(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<SceneVo>() {
            @Override
            public void call(SceneVo sceneVo) {
                refresh.finishLoadMore();
                sceneList.addAll(sceneVo.getData().getData());
                mHandler.sendEmptyMessage(0);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                refresh.finishLoadMore();
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                try {
                    linkToast(mContext, responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                intent = new Intent(mContext, SceneDetailActivity.class);
                startActivityForResult(intent, Constants.REFRESH_SCENE);
                break;
        }
    }

    public void refreshData() {
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1007:
                    Log.d(TAG, "SceneListFragment");
                    if (resultCode == Constants.SUCCESS) {
                        loadData();
                    }
                    break;
            }
        }
    }
}

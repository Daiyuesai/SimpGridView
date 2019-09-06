package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.CustomLayoutManagerAdapter;
import com.xiaozhanxiang.simplegridview.adapter.TestLayoutManagerAdapter;
import com.xiaozhanxiang.simplegridview.callback.DragDropItemTouchHelperCallback;
import com.xiaozhanxiang.simplegridview.view.CustomLayoutManager;
import com.xiaozhanxiang.simplegridview.view.test.TestDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: dai
 * date:2019/8/14
 */
public class CustomLayoutManagerActivity extends BaseActivity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.bnt_one)
    Button bntOne;
    @BindView(R.id.bnt_two)
    Button bntTwo;
    @BindView(R.id.bnt_three)
    Button bntThree;
    @BindView(R.id.bnt_four)
    Button bntFour;
    @BindView(R.id.bnt_five)
    Button bntFive;
    private TestLayoutManagerAdapter mAdapter;
    private List<String> mDatas;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, CustomLayoutManagerActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layout_manager);
        ButterKnife.bind(this);

        mAdapter = new TestLayoutManagerAdapter();
        TestDecoration decoration = new TestDecoration();
        recyclerview.addItemDecoration(decoration);
        recyclerview.setLayoutManager(new CustomLayoutManager());
        DragDropItemTouchHelperCallback callback = new DragDropItemTouchHelperCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        recyclerview.addItemDecoration(helper);
        helper.attachToRecyclerView(recyclerview);
        mAdapter.setItemTouchHelper(helper);


        recyclerview.setAdapter(mAdapter);


        mDatas = new ArrayList<>();
        String text = "  我的啊我的啊我的啊我的啊";
        for (int i = 0; i < 100; i++) {
            mDatas.add(i + text);
            text = text + "是的哇啊";
        }
        mAdapter.addData(mDatas);

    }


    @OnClick({R.id.bnt_one, R.id.bnt_two, R.id.bnt_three, R.id.bnt_four, R.id.bnt_five})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnt_one:
                mDatas = new ArrayList<>();

                for (int i = 0; i < 40; i++) {
                    if (i % 2 == 0){
                        mDatas.add( i+ "   我欸发就为福建欧韦佛王府井");
                    }else {
                        mDatas.add( i+ "   三菱电机覅我二姐佛i我今儿佛教哦i附近覅哦危机佛i就千佛山金佛埃及发哦i我今儿佛安家费欧式的肌肤");
                    }
                }

                mAdapter.replaceData(mDatas);
                break;
            case R.id.bnt_two:

                mAdapter = new TestLayoutManagerAdapter();
                mAdapter.addData(mDatas);
                recyclerview.setAdapter(mAdapter);


                break;
            case R.id.bnt_three:

//                //刷新数据
//                mAdapter.getDatas().remove(10);
//                mAdapter.getDatas().add(10,"10   更新的数据撒网撒网嗡嗡嗡嗡嗡嗡问我我我我我我我我我");
//                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bnt_four:
//                mAdapter.getDatas().add( 0,mAdapter.getDatas().size() + " 沃诗添加的数据");
//                mAdapter.notifyItemInserted(0);

//                mAdapter.getDatas().add( mAdapter.getDatas().size() + " 沃诗添加的数据");
//                mAdapter.notifyItemInserted(mAdapter.getItemCount() -1);
                break;
            case R.id.bnt_five:

//                mAdapter.getDatas().remove(mAdapter.getDatas().size() -1);
//                mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                break;
        }
    }
}

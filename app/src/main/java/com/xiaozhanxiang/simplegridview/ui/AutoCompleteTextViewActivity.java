package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.MediaController;

import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/6/16
 */
public class AutoCompleteTextViewActivity extends BaseActivity {

    public static void getInstance(Context context) {
        Intent intent = new Intent(context,AutoCompleteTextViewActivity.class);

        context.startActivity(intent);
    }


    private AppCompatAutoCompleteTextView mEtAutoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_text_view);

        mEtAutoText = findViewById(R.id.et_auto_text);
        List<String> datas = new ArrayList<>();
        datas.add("我欸发建瓯文件发我i金额佛");
        datas.add("发我欸经佛法覅文件哦");
        datas.add("不好回答分为哦发");
        datas.add("是对我而佛闹");
        datas.add("帮你我发觉分配欺骗");
        datas.add("不能罚款五百v你");
        datas.add("替我脑白金");
        datas.add("鸡尾酒佛我");
        datas.add("替我脑白金22");
        datas.add("鸡尾酒佛我33");
        datas.add("替我脑白金44");
        datas.add("鸡尾酒佛tt我");
        datas.add("替我脑白金ff");
        datas.add("鸡尾酒佛我aa");
        datas.add("替我脑白金ff");
        datas.add("鸡尾酒佛gg我");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_list_text,datas);
        mEtAutoText.setAdapter(adapter);
    }



    public static class MyAdapter extends BaseAdapter implements Filterable {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                }
            };
        }
    }
}

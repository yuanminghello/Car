package com.example.a123.dian13;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String url = "http://www.zhaoapi.cn/product/getCarts";
    private ExpandableListView expand;
    private CheckBox qx_ck;
    private TextView tv;
    private TextView tv_qian;
    private Button js_btn;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();
    }

    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid","71");
        HttpUtilSingleton.getInstance().doPost(url, map, new HttpUtilSingleton.UtilListener() {

            @Override
            public void succed(String json) {
                Gson gson = new Gson();
                Bean bean = gson.fromJson(json, Bean.class);
                if("0".equals(bean.getCode())){
                    List<Bean.DataBean> data = bean.getData();
                    adapter = new MyAdapter(data);
                    expand.setAdapter(adapter);

                    adapter.setListChangeListener(new MyAdapter.ListChangeListener() {
                        @Override
                        public void onSellerCheckChange(int groupPosition) {
                            boolean currentShopSelect = adapter.isCurrentShopSelect(groupPosition);
                            adapter.groupSelect(groupPosition,!currentShopSelect);
                            adapter.notifyDataSetChanged();
                            refrestSelectAndNum();
                        }

                        @Override
                        public void onProductCheckChange(int groupPosition, int childPosition) {
                            adapter.childSelect(groupPosition,childPosition);
                            adapter.notifyDataSetChanged();
                            refrestSelectAndNum();
                        }

                        @Override
                        public void onProductNumberChange(int groupPosition, int childPosition, int number) {
                                 adapter.subAddSelect(groupPosition,childPosition,number);
                                 adapter.notifyDataSetChanged();
                                 refrestSelectAndNum();
                        }
                    });

                    for(int i=0;i<data.size();i++){
                        expand.expandGroup(i);
                    }
                }
            }
            @Override
            public void fail(Exception e) { }
        });
    }

    private void refrestSelectAndNum(){
        boolean allShopSelect = adapter.allShopSelect();
        qx_ck.setChecked(allShopSelect);
        int shopTotalNum = adapter.shopTotalNum();
        js_btn.setText("去结算"+shopTotalNum);
        float shopTotalPrice = adapter.shopTotalPrice();
        tv_qian.setText("总价"+shopTotalPrice);
    }

    private void initView() {
        expand = (ExpandableListView) findViewById(R.id.expand);
        qx_ck = (CheckBox) findViewById(R.id.qx_ck);
        tv = (TextView) findViewById(R.id.tv);
        tv_qian = (TextView) findViewById(R.id.tv_qian);
        js_btn = (Button) findViewById(R.id.js_btn);

        qx_ck.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qx_ck:
                boolean select = adapter.allShopSelect();
                adapter.changeAllProductsStatus(!select);
                adapter.notifyDataSetChanged();
                refrestSelectAndNum();
                break;
        }
    }
}

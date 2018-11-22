package com.example.a123.dian13;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class MyAdapter extends BaseExpandableListAdapter {
    private List<Bean.DataBean> mdata;

    public MyAdapter(List<Bean.DataBean> data) {
        this.mdata = data;
    }

    @Override
    public int getGroupCount() {
        return mdata == null ? 0 : mdata.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mdata.get(groupPosition).getList() == null ? 0 : mdata.get(groupPosition).getList().size();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Bean.DataBean dataBean = mdata.get(groupPosition);
        GroupViewHolder gholder;
        if(convertView==null){
            convertView = View.inflate(parent.getContext(), R.layout.layout_group, null);
            gholder = new GroupViewHolder();
            gholder.group_cb = convertView.findViewById(R.id.group_cb);
            gholder.group_tv = convertView.findViewById(R.id.group_tv);

            convertView.setTag(gholder);
        }else {
            gholder = (GroupViewHolder) convertView.getTag();
        }

         gholder.group_tv.setText(dataBean.getSellerName());
        //点击事件
        gholder.group_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listChangeListener!=null){
                    listChangeListener.onSellerCheckChange(groupPosition);
                }
            }
        });
          return convertView;
    }
   class GroupViewHolder{
        CheckBox group_cb;
        TextView group_tv;

   }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Bean.DataBean bean = mdata.get(groupPosition);
        List<Bean.DataBean.ListBean> beanList = bean.getList();
        Bean.DataBean.ListBean listBean = beanList.get(childPosition);
        ChildViewHolder cholder;
        if(convertView==null){
            convertView=View.inflate(parent.getContext(),R.layout.layout_child,null);
            cholder = new ChildViewHolder();
            cholder.child_cb = convertView.findViewById(R.id.child_cb);
            cholder.child_iv = convertView.findViewById(R.id.child_iv);
            cholder.child_tv = convertView.findViewById(R.id.child_tv);
            cholder.child_tv2 =  convertView.findViewById(R.id.child_tv2);
            cholder.child_view =  convertView.findViewById(R.id.child_view);

            convertView.setTag(cholder);
        }else{
            cholder = (ChildViewHolder) convertView.getTag();
        }
        cholder.child_tv.setText(listBean.getTitle());
        cholder.child_tv2.setText(listBean.getPrice()+"");
        cholder.child_view.setNum(listBean.getNum());
        cholder.child_cb.setChecked(listBean.getSelected()==1);
        //imageloader

        //加减监听
        cholder.child_view.setNumberListener(new AddSubView.NumberListener() {
            @Override
            public void onNumChange(int num) {
                if(listChangeListener!=null){
                    listChangeListener.onProductNumberChange(groupPosition,childPosition,num);
                }
            }
        });
        //复选框监听
        cholder.child_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listChangeListener!=null){
                    listChangeListener.onProductCheckChange(groupPosition,childPosition);
                }
            }
        });
        return convertView;
    }
   class ChildViewHolder{
        CheckBox child_cb;
        ImageView child_iv;
        TextView child_tv;
        TextView child_tv2;
        AddSubView child_view;
   }

   //A1  当前商品选择
   public boolean  isCurrentShopSelect(int groupPosition){
       Bean.DataBean bean = mdata.get(groupPosition);
       List<Bean.DataBean.ListBean> list = bean.getList();
       for(Bean.DataBean.ListBean listBean : list){
           if(listBean.getSelected()==0){
               return false;
           }
       }
       return true;
   }
    //A2所有商品选择  与 A3  A4  类比
    public boolean allShopSelect(){
        for(int i=0;i<mdata.size();i++){
            Bean.DataBean dataBean = mdata.get(i);
            List<Bean.DataBean.ListBean> beanList = dataBean.getList();
            for(int j=0;j<beanList.size();j++){
                if(beanList.get(j).getSelected()==0){
                    return false;
                }
            }
        }
        return true;
    }
    //A3商品数量
    public int shopTotalNum(){
        int toltalNum=0;
        for(int i=0;i<mdata.size();i++){
            Bean.DataBean dataBean = mdata.get(i);
            List<Bean.DataBean.ListBean> beanList = dataBean.getList();
            for(int j=0;j<beanList.size();j++){
                if(beanList.get(j).getSelected()==1){
                   int num = beanList.get(j).getNum();
                    toltalNum+=num;
                }
            }
        }
       return toltalNum;
    }

    //A4  totalPrice
    public float shopTotalPrice(){
        float totalPrice=0;
        for(int i=0;i<mdata.size();i++){
            Bean.DataBean dataBean = mdata.get(i);
            List<Bean.DataBean.ListBean> beanList = dataBean.getList();
            for(int j=0;j<beanList.size();j++){
                //选中 1
                if(beanList.get(j).getSelected()==1){
                   float price = beanList.get(j).getPrice();
                    int num = beanList.get(j).getNum();
                    totalPrice += price*num;
                }
            }
        }
        return totalPrice;
    }
    //A5 选择商家
    public void groupSelect(int groupPosition,boolean isSelect){
        Bean.DataBean bean = mdata.get(groupPosition);
        List<Bean.DataBean.ListBean> list = bean.getList();
        for(int i=0;i<list.size();i++){
            list.get(i).setSelected(isSelect?1:0);
        }
    }
    //A6  选择商品
    public void childSelect(int groupPosition,int childPosition){
        Bean.DataBean bean = mdata.get(groupPosition);
        List<Bean.DataBean.ListBean> list = bean.getList();
        Bean.DataBean.ListBean listBean = list.get(childPosition);
        listBean.setSelected(listBean.getSelected()==0?1:0);
    }
    //A7 商品选中 商家状态
    public void changeAllProductsStatus(boolean isSelect){
        for(int i=0;i<mdata.size();i++){
            Bean.DataBean bean = mdata.get(i);
            List<Bean.DataBean.ListBean> list = bean.getList();
            for(int j=0;j<list.size();j++){
                list.get(j).setSelected(isSelect?1:0);
            }
        }

    }
    //A8 加减
    public void subAddSelect(int groupPosition,int childPosition,int number){
        Bean.DataBean bean = mdata.get(groupPosition);
        List<Bean.DataBean.ListBean> list = bean.getList();
        Bean.DataBean.ListBean listBean = list.get(childPosition);
        listBean.setNum(number);
    }


    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }


    public interface ListChangeListener{
        void onSellerCheckChange(int groupPosition);
        void onProductCheckChange(int groupPosition,int childPosition);
        void onProductNumberChange(int groupPosition,int childPosition,int number);
    }
    ListChangeListener listChangeListener;

    public void setListChangeListener(ListChangeListener listChangeListener) {
        this.listChangeListener = listChangeListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}

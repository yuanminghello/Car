package com.example.a123.dian13;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddSubView extends LinearLayout implements View.OnClickListener {
    private TextView sub;
    private TextView some;
    private TextView add;
    int num =1;

    public AddSubView(Context context) {
        this(context, null);
    }

    public AddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.addsub_layout, this);
        sub = view.findViewById(R.id.sub);
        add = view.findViewById(R.id.add);
        some = view.findViewById(R.id.some);

        sub.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                if(num>1){
                    --num;
                    some.setText(num+"");
                    if(NumberListener!=null){
                        NumberListener.onNumChange(num);
                    }
                }
                break;
            case R.id.sub:
                    ++num;
                    some.setText(num+"");
                if(NumberListener!=null){
                    NumberListener.onNumChange(num);
                }

                break;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        some.setText(num+"");
    }

    public interface NumberListener{
        void onNumChange(int num);
    }
    NumberListener NumberListener;

    public void setNumberListener(NumberListener NumberListener) {
        this.NumberListener = NumberListener;
    }
}

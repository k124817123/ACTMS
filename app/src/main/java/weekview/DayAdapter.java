package weekview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DayAdapter extends BaseAdapter {
    private List<DayBean> list;
    private Context context;

    public DayAdapter(List<DayBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DayBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TextView textView;
        // 使用缓存机制提高利用率
        if (view == null) {
            textView = new TextView(context);
            textView.setPadding(5, 5, 5, 5);
            view = textView;
        } else {
            textView = (TextView) view;
        }

        DayBean bean = getItem(position);

        textView.setText(bean.getDay() + "");
        textView.getPaint().setFlags(0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (bean.isCurrentDay()) {
            //textView.setBackgroundColor(Color.parseColor("#fd5f00"));
            textView.setBackgroundColor(Color.parseColor("#00FFFF"));
//            textView.setTextColor(Color.RED);
            textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下劃線
            textView.getPaint().setAntiAlias(true);//抗鋸齒
        } else if (bean.isCurrentMonth()) {

            textView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
        } else {
            // 通过 parseColor 方法得到的颜色不可以简写，必须写满六位
            textView.setBackgroundColor(Color.parseColor("#aaaaaa"));
            textView.setTextColor(Color.BLACK);
        }
        if (bean.isHastask()) {
            textView.setTextColor(Color.RED);
        }
        // 返回 view 或 textView 都行，因为都是同一个对象
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                for(int i=0;i<parent.getChildCount();i++){
                    DayBean bean = getItem(i);
                    if (bean.isCurrentMonth()) {
                        parent.getChildAt(i).setBackgroundColor(Color.WHITE);

                    } else {
                        // 通过 parseColor 方法得到的颜色不可以简写，必须写满六位
                        parent.getChildAt(i).setBackgroundColor(Color.parseColor("#aaaaaa"));

                    }
                }
                textView.setBackgroundColor(Color.parseColor("#00FFFF"));
//                textView.setTextColor(Color.WHITE);
                return false;
            }
        });
        return textView;
    }

}

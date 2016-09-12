package com.humanheima.citylistdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.humanheima.citylistdemo.adapter.CityAdapter;
import com.humanheima.citylistdemo.bean.City;
import com.humanheima.citylistdemo.util.LogUtil;
import com.humanheima.citylistdemo.util.PinyinComparator;
import com.humanheima.citylistdemo.util.T;
import com.humanheima.citylistdemo.util.TransfomPinYin;
import com.humanheima.citylistdemo.widget.CircleTextView;
import com.humanheima.citylistdemo.widget.MySlideView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.my_slide_view)
    MySlideView mySlideView;//侧边A-Z导航栏
    @BindView(R.id.rv_sticky_example)
    RecyclerView recyclerView;
    @BindView(R.id.tv_sticky_header_view)
    TextView tvStickyHeaderView;//头部悬停view
    @BindView(R.id.my_circle_view)
    CircleTextView circleTextView;//触摸侧边栏出现的原extView
    public static String[] stringCitys = new String[]{
            "合肥", "张家界", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州",
            "马鞍山", "芜湖", "铜陵", "安庆", "安阳", "黄山", "六安", "巢湖",
            "池州", "宣城", "亳州", "明光", "天长", "桐城", "宁国",
            "徐州", "连云港", "宿迁", "淮安", "盐城", "扬州", "泰州",
            "南通", "镇江", "常州", "无锡", "苏州", "江阴", "宜兴",
            "邳州", "新沂", "金坛", "溧阳", "常熟", "张家港", "太仓",
            "昆山", "吴江", "如皋", "通州", "海门", "启东", "大丰",
            "东台", "高邮", "仪征", "江都", "扬中", "句容", "丹阳",
            "兴化", "姜堰", "泰兴", "靖江", "福州", "南平", "三明",
            "复兴", "高领", "共兴", "柯家寨", "匹克", "匹夫", "旗舰", "启航",
            "如阳", "如果", "科比", "韦德", "诺维斯基", "麦迪", "乔丹", "姚明"
    };
    private List<City> cityList;
    private Set<String> firstPinYinSet;//不允许拼音重复
    public static List<String> pinyinList;
    private PinyinComparator pinyinComparator;//拼音比较器，用来进行排序
    private CityAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        bindEvent();
    }


    private void initData() {
        cityList = new ArrayList<>();
        firstPinYinSet = new LinkedHashSet<>();
        pinyinList = new ArrayList<>();
        pinyinComparator = new PinyinComparator();
        for (int i = 0; i < stringCitys.length; i++) {
            City city = new City();
            city.setCityName(stringCitys[i]);
            city.setCityPinyin(TransfomPinYin.transform(stringCitys[i]));
            cityList.add(city);
        }//排序
        Collections.sort(cityList, pinyinComparator);
        //把首字母放入set集合中
        for (City city : cityList) {
            firstPinYinSet.add(city.getFirstPinYin());
        }

        for (String letter : firstPinYinSet) {
            pinyinList.add(letter);
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CityAdapter(getApplicationContext(), cityList);
        recyclerView.setAdapter(adapter);


    }

    private void bindEvent() {
        //触摸侧边栏
        mySlideView.setTouchListener(new MySlideView.onTouchListener() {
            @Override
            public void showTextView(String textView, boolean dismiss) {
                if (dismiss) {
                    circleTextView.setVisibility(View.GONE);
                } else {
                    circleTextView.setVisibility(View.VISIBLE);
                    circleTextView.setText(textView);
                }
                int selectPosition = 0;
                for (int i = 0; i < cityList.size(); i++) {
                    if (cityList.get(i).getFirstPinYin().equals(textView)) {
                        selectPosition = i;
                        break;
                    }
                }
                //recyclerView滚到对应的位置
                scroll2Position(selectPosition);
            }
        });

        //recyclerView的点击事件
        adapter.setOnItemListener(new CityAdapter.onItemClickListener() {
            @Override
            public void itemClick(int position) {
                T.showToast(MainActivity.this, "你选择了:" + cityList.get(position).getCityName());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.e("recyclerView", "onScrolled dy=" + dy);
                /**
                 * Find the topmost view under the given point.
                 *
                 * @param x Horizontal position in pixels to search
                 * @param y Vertical position in pixels to search
                 * @return The child view under (x, y) or null if no matching child is found
                 */
                //public View findChildViewUnder(float x, float y)
                View stickyInfoView = recyclerView.findChildViewUnder(tvStickyHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }

                View transInfoView = recyclerView.findChildViewUnder(
                        tvStickyHeaderView.getMeasuredWidth() / 2, tvStickyHeaderView.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tvStickyHeaderView.getMeasuredHeight();
                    if (transViewStatus == CityAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            tvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            tvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == CityAdapter.NONE_STICKY_VIEW) {
                        tvStickyHeaderView.setTranslationY(0);
                    }
                }
            }
        });
    }

    private void scroll2Position(int index) {
        int firstPosition = layoutManager.findFirstVisibleItemPosition();

        int lastPosition = layoutManager.findLastVisibleItemPosition();
        LogUtil.e("scroll2Position", "index=" + index + ",firstPosition=" + firstPosition + ",lastPosition=" + lastPosition);
        if (index <= firstPosition) {
            recyclerView.scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = recyclerView.getChildAt(index - firstPosition).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(index);
        }
    }

}

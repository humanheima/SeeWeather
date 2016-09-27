package com.humanheima.hmweather.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.ui.fragment.SetFragment;

import butterknife.BindView;

public class SetActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private SetFragment setFragment;
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }*/

    @Override
    protected int bindLayout() {
        return R.layout.activity_reset;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setFragment = new SetFragment();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.rl_content, setFragment)
                .commit();
    }

    @Override
    protected void bindEvent() {

    }
}

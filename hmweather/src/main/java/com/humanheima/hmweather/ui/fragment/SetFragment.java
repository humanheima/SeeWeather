package com.humanheima.hmweather.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.service.AutoUpdateService;
import com.humanheima.hmweather.utils.SPUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String tag = SetFragment.class.getSimpleName();
    private Preference changeUpdateTime;//改变自动更新的时间
    private Preference clearCache;//清除缓存

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.set_preferences);
        changeUpdateTime = findPreference(SPUtil.AUTO_UPDATE);
        clearCache = findPreference(SPUtil.CLEAR_CACHE);
        changeUpdateTime.setSummary(SPUtil.getInstance().getAutoUpdate() == 0 ? "禁止自动刷新" : "每" + SPUtil.getInstance().getAutoUpdate() + "小时更新");
        changeUpdateTime.setOnPreferenceClickListener(this);
        clearCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (changeUpdateTime == preference) {
            //显示对话框
            showChangeUpdateTimeDialog();
        } else if (clearCache == preference) {
            Snackbar.make(getView(), "没有写入缓存", Snackbar.LENGTH_SHORT).show();
        }
        return true;
    }

    private void showChangeUpdateTimeDialog() {
        //将 SeekBar 放入 Dialog 的方案 http://stackoverflow.com/questions/7184104/how-do-i-put-a-seek-bar-in-an-alert-dialog
        View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.update_dialog, (ViewGroup) getActivity().findViewById(R.id.dialog_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        final SeekBar mSeekBar = (SeekBar) dialogLayout.findViewById(R.id.time_seekbar);
        final TextView tvShowHour = (TextView) dialogLayout.findViewById(R.id.tv_showhour);
        TextView tvDone = (TextView) dialogLayout.findViewById(R.id.done);

        mSeekBar.setMax(24);
        mSeekBar.setProgress(SPUtil.getInstance().getAutoUpdate());
        tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
        alertDialog.show();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.getInstance().putAutoUpdate(mSeekBar.getProgress());
                changeUpdateTime.setSummary(SPUtil.getInstance().getAutoUpdate() == 0 ? "禁止自动刷新" : "每" + SPUtil.getInstance().getAutoUpdate() + "小时更新");
                //需要再调用一次才能生效设置 不会重复的执行onCreate()， 而是会调用onStart()和onStartCommand()。
                getActivity().startService(new Intent(getActivity(), AutoUpdateService.class));
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}

package fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.humanweather.R;

import util.Constants;

/**
 * Created by dumingwei on 2016/4/13.
 * 定位城市成功的时候显示的对话框
 */
public class LocateDialogFragment extends DialogFragment {

    private AlertDialog alertDialog;
    //private static final String ARG_WEAID = "weaid",ARG_CITYNM = "citynm";
    private String weaid, citynm;

    private TextView tvLocateCity, tvCityNm;

    public LocateDialogFragment() {

    }

    public static LocateDialogFragment newInstance(String citynm, String weaid) {

        LocateDialogFragment dialogFragment = new LocateDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_WEAID, weaid);
        args.putString(Constants.ARG_CITYNM, citynm);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weaid = getArguments().getString(Constants.ARG_WEAID);
            citynm = getArguments().getString(Constants.ARG_CITYNM);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoglayout, null);
        tvLocateCity = (TextView) view.findViewById(R.id.tvLocateCity);
        tvCityNm = (TextView) view.findViewById(R.id.tvCityNm);
        tvCityNm.setText(citynm);
        alertDialog = new AlertDialog.Builder(getActivity()).setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.putExtra(Constants.ARG_WEAID, weaid);
                        dialog.dismiss();
                        getActivity().setResult(Activity.RESULT_OK, i);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).create();
        return alertDialog;
        //return builder.create();

       /* alertDialog=new AlertDialog.Builder(getActivity())
               .setTitle(R.string.loccity)
               .setMessage(citynm)
               .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent i = new Intent();
                       i.putExtra(Constants.ARG_WEAID,weaid);
                       dialog.dismiss();
                       getActivity().setResult(Activity.RESULT_OK, i);
                       getActivity().finish();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       alertDialog.dismiss();
                   }
               })
               .create();

        return alertDialog;*/
    }
}

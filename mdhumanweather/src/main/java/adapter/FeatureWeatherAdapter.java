package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.humanweather.R;

import java.util.List;

import model.FeatureWeatherModel;
public class FeatureWeatherAdapter extends ArrayAdapter<FeatureWeatherModel> {

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private int resourceId;

	public FeatureWeatherAdapter(Context context, int resource,
			List<FeatureWeatherModel> objects) {
		super(context, resource, objects);
		resourceId = resource;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FeatureWeatherModel featureWeathermodel = getItem(position);
		View view;
		ViewHolder viewHolder;

		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_itemcitynm = (TextView) view.findViewById(R.id.tv_itemcitynm);
			viewHolder.tv_itemdays = (TextView) view.findViewById(R.id.tv_itemdays);
			viewHolder.tv_itemweek = (TextView) view.findViewById(R.id.tv_itemweek);
			viewHolder.tv_itemtemperature = (TextView) view.findViewById(R.id.tv_itemtemperature);
			viewHolder.tv_itemweather = (TextView) view.findViewById(R.id.tv_itemweather);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_itemcitynm.setText(featureWeathermodel.getCitynm());
		viewHolder.tv_itemdays.setText(featureWeathermodel.getDays());
		viewHolder.tv_itemweek.setText(featureWeathermodel.getWeek());
		viewHolder.tv_itemtemperature.setText(featureWeathermodel.getTemperature());
		viewHolder.tv_itemweather.setText(featureWeathermodel.getWeather());
		return view;
	}

	class ViewHolder {
		TextView tv_itemcitynm;
		TextView tv_itemdays;
		TextView tv_itemweek;
		TextView tv_itemtemperature;
		TextView tv_itemweather;
	}

}

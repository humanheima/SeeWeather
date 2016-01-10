package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.humanweather.R;

import java.util.List;

import model.WeatherCityModel;

public class CityListViewAdapter extends BaseAdapter implements SectionIndexer {

    private List<WeatherCityModel> list = null;
    private Context mContext;

    public CityListViewAdapter(Context mContext, List<WeatherCityModel> list) {
        this.mContext = mContext;
        this.list = list;
    }
    /**
     * called when listview's dataset changed to update ui
     * @param list
     */
    public void updateListView(List<WeatherCityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder = null;
        final WeatherCityModel model = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.citylvitem, null);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(model.getCityno().toUpperCase()
                    .substring(0, 1));
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(model.getCitynm());

        return convertView;
    }

    @Override
    public int getPositionForSection(int section) {

        for (int i = 0; i < getCount(); i++) {
            String cityno = list.get(i).getCityno().toUpperCase();

            char firstChar = cityno.charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {

        String cityno = list.get(position).getCityno().toUpperCase();

        return cityno.charAt(0);
    }
    @Override
    public Object[] getSections() {
        return null;
    }

    final static class ViewHolder {

        TextView tvLetter;
        TextView tvTitle;
    }

}

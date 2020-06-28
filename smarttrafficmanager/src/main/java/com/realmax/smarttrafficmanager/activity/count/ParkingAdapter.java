package com.realmax.smarttrafficmanager.activity.count;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.realmax.smarttrafficmanager.R;
import com.realmax.smarttrafficmanager.bean.ParkingBean;

import java.util.ArrayList;

public class ParkingAdapter extends BaseAdapter {

    private ArrayList<ParkingBean> parkingBeans;
    private CountLogic.CountUiRefresh countUiRefresh;
    private ImageView ivIcon;
    private TextView tvPosition;

    public ParkingAdapter(ArrayList<ParkingBean> parkingBeans, CountLogic.CountUiRefresh countUiRefresh) {
        this.parkingBeans = parkingBeans;
        this.countUiRefresh = countUiRefresh;
    }

    public void setParkingBeans(ArrayList<ParkingBean> parkingBeans) {
        this.parkingBeans = parkingBeans;
    }

    @Override
    public int getCount() {
        return parkingBeans.size();
    }

    @Override
    public ParkingBean getItem(int position) {
        return parkingBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(countUiRefresh.getActivity(), R.layout.item_park, null);
        } else {
            view = convertView;
        }
        initView(view);
        tvPosition.setText(String.valueOf(position + 1));
        int i = Integer.parseInt(getItem(position).getSignalValue());
        ivIcon.setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        return view;
    }

    private void initView(View view) {
        ivIcon = view.findViewById(R.id.ivIcon);
        tvPosition = view.findViewById(R.id.tvPosition);
    }
}

package org.whymca.dive;

import org.whymca.dive.model.Dive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiveAdapter extends ArrayAdapter<Dive>{

	public DiveAdapter(Context context) {
		//ArrayAdapter want to have a layout reference to the
		//text field that should handle by default the elements.
		//We are going to override getView anyway, so it doesn't really matter
		super(context, R.layout.dive_item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout rl = null;
		//convertView is passed again to the Adapter if we already inflated and
		//returned a View object no need to reinflate
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = rl = (RelativeLayout)li.inflate(R.layout.dive_item, null);
		} else {
			rl = (RelativeLayout) convertView;
		}
		
		//get the model object for position 
		Dive model = getItem(position);
		
		//recover view objects from layout
		TextView coeff = (TextView) rl.findViewById(R.id.coeff);
		TextView avgMark = (TextView) rl.findViewById(R.id.mark);
		ImageView img = (ImageView) rl.findViewById(R.id.snapshot);

		//setting correct values
		coeff.setText(String.valueOf(model.getCoeff()));
		avgMark.setText(String.valueOf(model.getAverageMark()));
		if (model.getCoeff() > 2.0f) {
			//it's a really difficult dive! worth a dolphin
			img.setImageResource(R.drawable.dolphin_dive_snapshot);
		} else {
			img.setImageResource(R.drawable.default_dive_snapshot);
		}
		return convertView;
	}
}

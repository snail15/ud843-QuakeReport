package com.example.android.ud843_quakereport;

/**
 * Created by OWNER on 7/22/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OWNER on 7/21/2016.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Activity context, List<Earthquake> earthquakes){
        super(context,0,earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list,parent,false);
        }

        Earthquake currentEarthquake = getItem(position);

        long timeInMilliseconds = currentEarthquake.getDate();
        Date dateObject = new Date(timeInMilliseconds);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormatter.format(dateObject);

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeTextView.setText(String.valueOf(currentEarthquake.getMagnitude()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        TextView relativeLocationTextView = (TextView) listItemView.findViewById(R.id.relative_location);
        relativeLocationTextView.setText(currentEarthquake.getRelativeLocation());

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
        locationTextView.setText(currentEarthquake.getLocation());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(dateToDisplay);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(timeToDisplay);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

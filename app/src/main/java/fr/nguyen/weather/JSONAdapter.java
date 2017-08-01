/**
 * Created by NGUYEN TIEN HAI on 01/08/2017.
 */

package fr.nguyen.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class JSONAdapter extends BaseAdapter {

    private static final String IMAGE_URL_BASE = "http://openweathermap.org/img/w/";

    Context _context;
    LayoutInflater _inflater;
    JSONObject _jsonObject;

    public JSONAdapter(Context context,
                       LayoutInflater inflater) {

        _context = context;
        _inflater = inflater;
        _jsonObject = new JSONObject();
    }

    /**
     * Update the adapter's dataset
     *
     * @param jsonObject
     */
    public void updateData(JSONObject jsonObject) {
        _jsonObject = jsonObject;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        try {
            JSONArray list = new JSONArray(_jsonObject.get("list").toString());
            return list.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            JSONArray list = new JSONArray(_jsonObject.get("list").toString());
            return list.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position,
                      View convertView,
                      ViewGroup parent) {

      ViewHolder holder;

      // check if the view already exists
      // if so, no need to inflate and findViewById again!
      if (convertView == null) {

          // Inflate the custom row layout from your XML.
          convertView = _inflater.inflate(R.layout.row_weather, null);

          // create a new "Holder" with subviews
          holder = new ViewHolder();
          holder._weatherImageView =
                  (ImageView) convertView
                          .findViewById(R.id.img_weather);
          holder._dateTextView =
                  (TextView) convertView
                          .findViewById(R.id.text_date);
          holder._infoTextView =
                  (TextView) convertView
                          .findViewById(R.id.text_info);

          // hang onto this holder for future recyclage
          convertView.setTag(holder);
      } else {

          // skip all the expensive inflation/findViewById
          // and just get the holder you already made
          holder = (ViewHolder) convertView.getTag();
      }

      // Get the current data in JSON form
      JSONObject jsonObject = getItem(position);

      // See if there is a cover ID in the Object
      if (jsonObject.has("weather")) {
          JSONArray weatherList = jsonObject.optJSONArray("weather");
          JSONObject weather = weatherList.optJSONObject(0);

          // If so, grab the Cover ID out from the object
          String imageID = weather.optString("icon");

          // Construct the image URL (specific to API)
          String imageURL = IMAGE_URL_BASE
                  + imageID
                  + ".png";

          // Use Picasso to load the image
          // Temporarily have a placeholder in case it's slow to load
          Picasso.with(_context)
                  .load(imageURL)
                  .placeholder(R.mipmap.ic_launcher)
                  .into(holder._weatherImageView);
      } else {

          // If there is no cover ID in the object, use a placeholder
          holder._weatherImageView
                  .setImageResource(R.mipmap.ic_launcher);
      }

      // Grab the info from the JSON
      String weatherDate = "";
      String weatherInfo = "";

      if (jsonObject.has("dt")) {
          int dt = jsonObject.optInt("dt");
          Date date = new Date(dt * 1000L);
          DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
          format.setTimeZone(TimeZone.getTimeZone("UTC"));
          weatherDate = format.format(date);
      }

      if (jsonObject.has("temp")) {
          JSONObject temp = jsonObject.optJSONObject("temp");
          weatherInfo = String.valueOf(Math.round(temp.optInt("day"))) + "°C";
      }

      // Send these Strings to the TextViews for display
      holder._dateTextView.setText(weatherDate);
      holder._infoTextView.setText(weatherInfo);

      return convertView;
  }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView _weatherImageView;
        public TextView _dateTextView;
        public TextView _infoTextView;
    }
}


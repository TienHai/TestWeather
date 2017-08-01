/**
 * Created by NGUYEN TIEN HAI on 01/08/2017.
 */

package fr.nguyen.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Access to widget defined in layout XML
        TextView txtWeatherInfo = (TextView) findViewById(R.id.txt_weather_info);

        // Unpack weatherInfo from Intent
        String weatherInfo = this.getIntent().getExtras().getString("weatherInfo");

        // Test if there is a valid weatherInfo
        String txt = "";
        if (weatherInfo != null && weatherInfo.length() > 0) {
            JSONObject weather = null;
            try {
                weather = new JSONObject(weatherInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (weather.has("dt")) {
                int dt = weather.optInt("dt");
                Date date = new Date(dt * 1000L);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                txt += format.format(date) + "\n";
            }
            if (weather.has("temp")) {
                JSONObject temp = weather.optJSONObject("temp");
                String day = "Températue : " + String.valueOf(Math.round(temp.optInt("day"))) + "°C";
                String min = "min : " + String.valueOf(Math.round(temp.optInt("min"))) + "°C";
                String max = "max : " + String.valueOf(Math.round(temp.optInt("max"))) + "°C";
                txt += String.format(Locale.FRANCE, "%s (%s, %s)\n", day, min, max);
            }
            if (weather.has("humidity")) {
                int humidity = weather.optInt("humidity");
                txt += String.format(Locale.FRANCE, "Taux d'humidité : %d%% \n", humidity);
            }
            if (weather.has("pressure")) {
                double pressure = weather.optDouble("pressure");
                txt += String.format(Locale.FRANCE, "Pression atmosphérique : %.2f hPa \n", pressure);
            }
            if (weather.has("weather")) {
                JSONArray weatherList = weather.optJSONArray("weather");
                JSONObject wInfo = weatherList.optJSONObject(0);
                String main = wInfo.optString("main");
                String description = wInfo.optString("description");
                txt += String.format(Locale.FRANCE, "\n\n %s : %s", main, description);
            }
            txtWeatherInfo.setText(txt);
        }
    }
}

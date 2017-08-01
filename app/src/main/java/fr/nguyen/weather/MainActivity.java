/**
 * Created by NGUYEN TIEN HAI on 01/08/2017.
 */

package fr.nguyen.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String WEATHER_APP_API = "8194ea842a9aef80a798c8ac0c320ec4";
    private String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Paris&units=metric&cnt=5&appid=";
    ListView _mainListview;
    ProgressBar _progressSpinner;
    JSONAdapter _JSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Access to widget defined in layout XML
        _progressSpinner = (ProgressBar) findViewById(R.id.progress_spinner);
        _mainListview = (ListView) findViewById(R.id.main_listview);

        // Set this activity to react to list items being pressed
        _mainListview.setOnItemClickListener(this);

        // Create a JSONAdapter for the ListView
        _JSONAdapter = new JSONAdapter(this, getLayoutInflater());

        // Set the ListView to use the ArrayAdapter
        _mainListview.setAdapter(_JSONAdapter);

        // Launch query
        queryWeather();
    }

    /**
     * Item list pressed
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Get item pressed data
        JSONObject weatherInfo = _JSONAdapter.getItem(position);

        // Create an Intent for pass data to DetailActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("weatherInfo", weatherInfo.toString());

        // Start DetailActivity using Intent
        startActivity(detailIntent);
    }

    /**
     * Query weather for 5 days
     */
    private void queryWeather() {

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Start progress bar
        _progressSpinner.setVisibility(View.VISIBLE);

        // Client get a JSONObject of response and define how to respond
        client.get(BASE_URL + WEATHER_APP_API, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                // Stop progress bar
                _progressSpinner.setVisibility(View.GONE);

                // update the data.
                _JSONAdapter.updateData(response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                // Stop progress bar
                _progressSpinner.setVisibility(View.GONE);

                // Log error message
                // to help solve any problems
                Log.e("WEATHER", statusCode + " " + throwable.getMessage());
            }
        });
    }
}

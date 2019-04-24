package edu.illinois.cs.cs125.spring2019.lab12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        startAPICall();
        Button bigRedButton = findViewById((R.id.BigRedButton));
        bigRedButton.setOnClickListener(v -> {
            Log.e(TAG, "Big Red Button clicked");
            printJson();
        });
    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     *Printing JSON File.
     *
     * @return json string
     */
    protected String printJson() {
        HttpHandler sh = new HttpHandler();

        String url = "https://api.wheretheiss.at/v1/satellites/25544";
        String jsonStr = sh.makeServiceCall(url);

        return jsonStr;
    }
    /**
     * Make a call to the IP geolocation API.
     *
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.wheretheiss.at/v1/satellites/25544",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            Log.i(TAG, response.get("name").toString());
            TextView ipInfo = findViewById(R.id.JsonInfo);
            ipInfo.setText(response.toString());
            ipInfo.setVisibility(View.VISIBLE);
            Log.i(TAG, response.getString("latitude"));

        } catch (JSONException ignored) {
        }
    }
}

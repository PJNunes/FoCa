package pt.ua.foca;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Pedro Nunes
 */

public class FoodParser {

    //cache
    String jsonStr = null;
    /* The date/time conversion code is going to be moved outside the asynctask later,
        * so for convenience we're breaking it out into its own method now.
        */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    public String[][] getData() throws JSONException {
        //cache
        if (jsonStr==null)
            jsonStr = callAPI();
        JSONObject json = new JSONObject(jsonStr);
        JSONArray menu = json.getJSONObject("menus").getJSONArray("menu");

        String[] content = new String[menu.length()];
        String[] titles = new String[menu.length()];

        for (int i=0;i<menu.length();i++) {
            JSONObject entry = menu.getJSONObject(i);

            //titles
            String canteen = entry.getJSONObject("@attributes").getString("canteen");
            String day = entry.getJSONObject("@attributes").getString("weekday");
            String meal = entry.getJSONObject("@attributes").getString("meal");

            titles[i] = day+" - "+canteen+" ("+meal+")";

            //content
            String disabled = entry.getJSONObject("@attributes").getString("disabled");

            //sem ementa
            if (!disabled.equals("0")) {
                content[i] = disabled;
            } else {
                content[i] = "";
                JSONArray items = entry.getJSONObject("items").getJSONArray("item");
                for (int x=0;x<items.length();x++) {
                    if (!items.getString(x).contains("{"))
                        content[i] += items.getString(x)+"\n\n";
                }
            }
        }

        String[][] resultStrings = new String[2][menu.length()];
        resultStrings[0] = titles;
        resultStrings[1] = content;
        Log.v("FP","test");
        return resultStrings;

    }

    public String callAPI() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String foodJsonStr = null;

        try {
            URL url = new URL("http://services.web.ua.pt/sas/ementas?date=week&format=json");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                foodJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                foodJsonStr = null;
            }
            foodJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            foodJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return foodJsonStr;
    }
}


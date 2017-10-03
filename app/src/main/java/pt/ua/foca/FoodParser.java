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

/**
 * Created by Pedro Nunes
 */

class FoodParser {

    //cache
    private String jsonStr = null;

    public Tuple[] getData() throws JSONException {
        //cache
        if (jsonStr==null)
            jsonStr = callAPI();
        JSONObject json = new JSONObject(jsonStr);
        JSONArray menu = json.getJSONObject("menus").getJSONArray("menu");

        int j=0;
        int k=2;
        Tuple[] results=new Tuple[menu.length()*2/5];
        Canteen[] almoco = new Canteen[3];
        Canteen[] jantar = new Canteen[3];
        String lastDay="";
        String day="";
        String title;
        String content;

        for (int i=0;i<menu.length();i++) {
            JSONObject entry = menu.getJSONObject(i);

            day = entry.getJSONObject("@attributes").getString("weekday");
            if(lastDay.compareTo("")!=0 && day.compareTo(lastDay)!=0){
                title=translate(lastDay) +": Almoço";
                results[j]= new Tuple<>(title,almoco);
                title=translate(lastDay) +": Jantar";
                results[++j]= new Tuple<>(title,jantar);
                j++;
                almoco = new Canteen[3];
                jantar = new Canteen[3];
            }
            //titles
            String canteen = entry.getJSONObject("@attributes").getString("canteen");
            //content
            String disabled = entry.getJSONObject("@attributes").getString("disabled");

            //sem ementa
            if (!disabled.equals("0")) {
                content = disabled;
            } else {
                content = "";
                JSONArray items = entry.getJSONObject("items").getJSONArray("item");
                for (int x=0;x<items.length();x++) {
                    if (!items.getString(x).contains("{"))
                        content += items.getString(x)+"\n\n";
                }
            }
            Log.v("FP",canteen+" "+day);
            if(entry.getJSONObject("@attributes").getString("meal").compareTo("Almoço")==0) {
                almoco[k = getNext(k)] = new Canteen(canteen, content);
                Log.v("FP", k + " alm");
            }
            else {
                jantar[k] = new Canteen(canteen, content);
                Log.v("FP", k+" jan");
            }
            lastDay=day;
        }

        title=translate(day) +": Almoço";
        results[j]= new Tuple<>(title,almoco);
        title=translate(day) +": Jantar";
        results[++j]= new Tuple<>(title,jantar);

        return results;
    }

    private String callAPI() {
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
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                foodJsonStr = null;
            }
            assert inputStream != null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
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

    private String translate(String day){
        switch (day) {
            case "Monday":
                return "Segunda";
            case "Tuesday":
                return "Terça";
            case "Wednesday":
                return "Quarta";
            case "Thursday":
                return "Quinta";
            case "Friday":
                return "Sexta";
            case "Saturday":
                return "Sábado";
            default:
                return "Domingo";
        }
    }

    private int getNext(int a){
        if(a==2)
            return 0;
        return a+1;
    }
}



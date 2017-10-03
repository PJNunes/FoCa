package pt.ua.foca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ItemsListActivity  extends AppCompatActivity implements ItemsListFragment.OnItemSelectedListener {
    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FoodParser parser = new FoodParser();

        try {
            Tuple[] s = parser.getData();
            if(s==null)
                s=readFile();
            else
                saveFile(s);
            for (Tuple value : s) {
                Item.addItem((String) value.getTitle(), (Canteen[]) value.getBody());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        determinePaneLayout();
    }

    private Tuple[] readFile() {
        String FILENAME = "cache.txt";
        FileInputStream in = null;
        try
        {
            in = openFileInput(FILENAME);
        }
        catch(IOException e1){}
        try
        {
            byte[] buffer = new byte[4096]; // Read 4K characters at a time.
            int len;
            String title;
            Canteen[] body;

            while((len = in.read(buffer)) != -1)
            {

                Log.v("ILA",new String(buffer, 0, len));

            }
        }
        catch(IOException e){}
        finally
        {
            try
            {
                if(in != null) in.close();
            }
            catch(IOException e){}
        }
        return null;
    }

    private void saveFile(Tuple[] s) {
        try {
            String FILENAME = "cache.txt";
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            for (Tuple value : s) {
                fos.write(((String) value.getTitle()).getBytes());
                for(Canteen canteen : (Canteen[]) value.getBody()) {
                    if(canteen==null){
                        fos.write("null".getBytes());
                        fos.write("null".getBytes());
                    }else {
                        fos.write(canteen.getTitle().getBytes());
                        fos.write(canteen.getBody().getBytes());
                    }
                }
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            Log.v("FP", "failed save");
            e.printStackTrace();
        }
    }

    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.flDetailContainer);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
            ItemsListFragment fragmentItemsList =
                    (ItemsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList);
            fragmentItemsList.setActivateOnItemClick(true);
        }
    }

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(Item item) {
        if (isTwoPane) { // single activity with list and detail
            // Replace framelayout with new detail fragment
            ItemDetailFragment fragmentItem = ItemDetailFragment.newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.commit();
        } else { // go to separate activity
            // launch detail activity using intent
            Intent i = new Intent(this, ItemDetailActivity.class);
            i.putExtra("item", item);
            startActivity(i);
        }
    }
}

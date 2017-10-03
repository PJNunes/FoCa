package pt.ua.foca;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import org.json.JSONException;

public class ItemsListActivity  extends AppCompatActivity implements ItemsListFragment.OnItemSelectedListener {
    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FoodParser parser = new FoodParser();

        try {
            Tuple[] s = parser.getData();
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

package pt.ua.foca;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pedro Nunes
 */

public class ItemDetailActivity extends AppCompatActivity{
    ItemDetailFragment fragmentItemDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Item item = (Item) getIntent().getSerializableExtra("item");
        if (savedInstanceState == null) {
            fragmentItemDetail = ItemDetailFragment.newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItemDetail);
            ft.commit();
        }
    }
}
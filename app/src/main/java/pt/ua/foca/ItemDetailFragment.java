package pt.ua.foca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Pedro Nunes
 */

public class ItemDetailFragment extends Fragment {
    private Item item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail,
                container, false);
        TextView tvTitle1 = (TextView) view.findViewById(R.id.tvTitle1);
        TextView tvBody1 = (TextView) view.findViewById(R.id.tvBody1);
        TextView tvTitle2 = (TextView) view.findViewById(R.id.tvTitle2);
        TextView tvBody2 = (TextView) view.findViewById(R.id.tvBody2);
        TextView tvTitle3 = (TextView) view.findViewById(R.id.tvTitle3);
        TextView tvBody3 = (TextView) view.findViewById(R.id.tvBody3);

        Canteen[] info=item.getBody();
        tvTitle1.setText(info[0].getTitle());
        tvBody1.setText(info[0].getBody());

        tvTitle2.setText(info[1].getTitle());
        tvBody2.setText(info[1].getBody());

        if(info[2]==null){
            tvTitle3.setVisibility(View.GONE);
            tvBody3.setVisibility(View.GONE);
        }
        else {
            tvTitle3.setVisibility(View.VISIBLE);
            tvBody3.setVisibility(View.VISIBLE);
            tvTitle3.setText(info[2].getTitle());
            tvBody3.setText(info[2].getBody());
        }
        return view;
    }

    // ItemDetailFragment.newInstance(item)
    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragmentDemo = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
}


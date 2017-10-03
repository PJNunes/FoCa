package pt.ua.foca;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Pedro Nunes
 */

public class Item implements Serializable {
    private static final long serialVersionUID = -1213949467658913456L;
    private String title;
    private String body;
    private static ArrayList<Item> items = new ArrayList<>();

    public Item(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static void addItem(String day, String content) {
        items.add(new Item(day, content));
    }
}

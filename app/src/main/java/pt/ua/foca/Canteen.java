package pt.ua.foca;

import java.io.Serializable;

/**
 * Created by Pedro Nunes.
 */

class Canteen implements Serializable{
    private static final long serialVersionUID = -1213949467658913456L;
    private String title;
    private String body;

    Canteen(String title, String body) {
        this.title = title;
        this.body = body;
    }

    String getTitle() {
        return title;
    }

    String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}

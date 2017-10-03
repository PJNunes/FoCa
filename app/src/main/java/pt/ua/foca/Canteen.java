package pt.ua.foca;

/**
 * Created by Pedro Nunes.
 */

class Canteen {
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

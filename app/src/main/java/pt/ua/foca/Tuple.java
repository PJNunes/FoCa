package pt.ua.foca;

/**
 * Created by Pedro Nunes.
 */

class Tuple<X, Y> {
    private X x;
    private Y y;

    Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    X getTitle() {
        return x;
    }

    Y getBody() {
        return y;
    }
}

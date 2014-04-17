package george;

public class Event {
    public final int type;
    public final KeyEvent k;
    public final MouseEvent m;

    public final static int KEY_EVENT = -1;
    public final static int MOUSE_EVENT = 1;

    public boolean isMouseEvent() {
        return this.type == Event.MOUSE_EVENT;
    }

    public boolean isKeyEvent() {
        return this.type == Event.KEY_EVENT;
    }

    private Event(int t, KeyEvent key, MouseEvent e) {
        type = t;
        k = key;
        m = e;
    }

    public static Event onKeyPress(char c) {
        return new Event(Event.KEY_EVENT, new KeyEvent(false, true, c), null);
    }

    public static Event onKeyUp(char c) {
        return new Event(Event.KEY_EVENT, new KeyEvent(true, false, c), null);
    }

    public static Event onKeyDown(char c) {
        return new Event(Event.KEY_EVENT, new KeyEvent(false, false, c), null);
    }

    public static Event onMouseMove(int x, int y) {
        return new Event(Event.MOUSE_EVENT, null, new MouseEvent(true, true, x, y));
    }

    public static class KeyEvent {
        final public boolean isUp;
        final public boolean isPress;
        final public char key;

        public KeyEvent(boolean up, boolean press, char key) {
            isUp = up;
            isPress = press;
            this.key = key;
        }
    }

    //TODO: Check that all this is semantically correct.
    public static class MouseEvent {
        public final boolean isLUp;
        public final boolean isRUp;
        public final int x, y;

        public MouseEvent(boolean lup, boolean rup, int x, int y) {
            isLUp = lup;
            isRUp = rup;
            this.x = x;
            this.y = y;
        }
    }
}

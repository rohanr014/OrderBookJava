public class BBO {
    int bid_price;
    int bid_size;
    int ask_price;
    int ask_size;

    public BBO(int bid_price, int bid_size, int ask_price, int ask_size) {
        this.bid_price = bid_price;
        this.bid_size = bid_size;
        this.ask_price = ask_price;
        this.ask_size = ask_size;
    }

    @Override
    public String toString() {
        String s = "" + bid_price;
        s += ",";
        s += bid_size;
        s += ",";
        s += ask_price;
        s += ",";
        s += ask_size;
        return s;
    }
}
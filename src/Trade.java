public class Trade {
    int price;
    int size;
    int buyID;
    int sellID;

    public Trade(int price, int size, int buyID, int sellID) {
        this.price = price;
        this.size = size;
        this.buyID = buyID;
        this.sellID = sellID;
    }

    @Override
    public String toString() {
        String s = "" + price + ",";
        s += size;
        s += ",";
        s += buyID;
        s += ",";
        s += sellID;
        return s;
    }

}

public class Order {
    private boolean buyOrSell;
    private int timestamp;
    private int orderID;
    private int price;
    private int size;
    public Order nextOrder;
    public Order prevOrder;

    public Order(String side, int timestamp, int orderID, int price, int size) {
        if (side.equals("buy")) {
            this.buyOrSell = true;
        } else if (side.equals("sell")) {
            this.buyOrSell = false;
        }
        this.timestamp = timestamp;
        this.orderID = orderID;
        this.price = price;
        this.size = size;
    }

    public Order(int timestamp, int orderID) {
        this.timestamp = timestamp;
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        String s = "" + timestamp;
        s += ",";
        s += orderID;
        s += ",";
        s += "$" + price;
        s += ",";
        s += size;
        return s;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return (this.orderID == order.getOrderID());

    }

    public void setSize(int newSize) {
        size = newSize;
    }

    public int getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public boolean isBuy() {
        return buyOrSell;
    }

    public boolean isSell() {
        return (!(buyOrSell));
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getOrderID() {
        return orderID;
    }
}

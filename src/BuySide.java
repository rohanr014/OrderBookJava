import java.util.*;


public class BuySide {
    private boolean buyOrSell;
    private Order bestBid;

    public BuySide(String side) {
        if (side.equals("buy")) {
            this.buyOrSell = true;
        } else if (side.equals("sell")) {
            this.buyOrSell = false;
        }
    }

    public void addOrder(Order bid) {
        // if buy side of the book is empty:
        if (bestBid == null) {
            bestBid = bid;
        } else if (bid.getPrice() > bestBid.getPrice()) {
            // new best bid
            bestBid.nextOrder = bid;
            bid.prevOrder = bestBid;
            bestBid = bid;
        } else {
            // insertion sort on price, then timestamp
            Order pointer = bestBid; // bestBid acts as tail of linked list
            while (pointer.prevOrder != null && pointer.prevOrder.getPrice() > bid.getPrice()) {
                pointer = pointer.prevOrder;
            }
            if (pointer.prevOrder == null) {
                pointer.prevOrder = bid;
                bid.nextOrder = pointer;
            } else {
                // This should handle tiebreaking, since the condition on line 28 is strict inequality,
                // later timestamped orders will appear farther back in the order book. So earlier
                // timestamped orders will get filled first. Not 100% sure.
                pointer.prevOrder.nextOrder = bid;
                bid.prevOrder = pointer.prevOrder;
                pointer.prevOrder = bid;
                bid.nextOrder = pointer;
            }
        }
    }

    public void cancelOrder(Order order) {
        if (order.equals(bestBid)) {
            bestBid = bestBid.prevOrder;
            bestBid.nextOrder = null;
        } else {
            Order pointer = bestBid;
            while (pointer != null && !(pointer.equals(order))) {
                pointer = pointer.prevOrder;
            }
            if (pointer == null) {
                return;
            }
            pointer.nextOrder.prevOrder = pointer.prevOrder;
            pointer.prevOrder.nextOrder = pointer.nextOrder;
        }
    }

    public List<Trade> fillOrder(Order ask) {
        List<Trade> trades = new ArrayList<>();
        Order pointer = bestBid;
        while (ask.getSize() > 0 && pointer != null && pointer.getPrice() >= ask.getPrice()) {
            Trade trade;
            if (pointer.getSize() > ask.getSize()) {
                // ask is completely filled, but bid stays on the book
                trade = new Trade(ask.getPrice(), ask.getSize(), pointer.getOrderID(), ask.getOrderID());
                trades.add(trade);
                pointer.setSize(pointer.getSize() - ask.getSize());
                ask.setSize(0);
                break;

            } else if (pointer.getSize() < ask.getSize()) {
                // ask is partially filled with this bid, continue
                trade = new Trade(ask.getPrice(), pointer.getSize(), pointer.getOrderID(), ask.getOrderID());
                trades.add(trade);

                ask.setSize(ask.getSize() - pointer.getSize());
                pointer = pointer.prevOrder;
                bestBid = pointer;
                bestBid.nextOrder = null;

            } else {
                // the ask size matches this bid's size
                trade = new Trade(ask.getPrice(), ask.getSize(), pointer.getOrderID(), ask.getOrderID());
                trades.add(trade);
                ask.setSize(0);
                bestBid = pointer.prevOrder;
                bestBid.nextOrder = null;
                break;
            }
        }
        return trades;
    }

    public Order getBestBid() {
        // could consist of multiple orders
        return bestBid;
    }

    // gives price and net size
    public int[] getBBO() {
        int[] best = new int[2];

        if (bestBid == null) {
            best[0] = 0;
            best[1] = 0;
            return best;
        }
        best[0] = bestBid.getPrice();
        int netSize = 0;
        Order pointer = bestBid;
        while (pointer != null && pointer.getPrice() == bestBid.getPrice()) {
            netSize += pointer.getSize();
            pointer = pointer.prevOrder;
        }
        best[1] = netSize;
        return best;
    }
}

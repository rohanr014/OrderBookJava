import java.util.*;


public class SellSide {
    private boolean buyOrSell;
    private Order bestOffer;

    public SellSide(String side) {
        if (side.equals("buy")) {
            this.buyOrSell = true;
        } else if (side.equals("sell")) {
            this.buyOrSell = false;
        }
    }

    public void addOrder(Order ask) {

        // if sell side of the book is empty:
        if (bestOffer == null) {
            bestOffer = ask;
        } else if (ask.getPrice() < bestOffer.getPrice()) {
            // new best offer
            bestOffer.prevOrder = ask;
            ask.nextOrder = bestOffer;
            bestOffer = ask;
        } else {
            // insertion sort on price, then timestamp
            Order pointer = bestOffer; // bestOffer acts as head of linked list
            while (pointer.nextOrder != null && pointer.nextOrder.getPrice() < ask.getPrice()) {
                pointer = pointer.nextOrder;
            }
            if (pointer.nextOrder == null) {
                pointer.nextOrder = ask;
                ask.prevOrder = pointer;
            } else {
                // This should handle tiebreaking, since the condition on line 28 is strict inequality,
                // later timestamped orders will appear farther back in the order book. So earlier
                // timestamped orders will get filled first. Not 100% sure.
                pointer.nextOrder.prevOrder = ask;
                ask.nextOrder = pointer.nextOrder;
                pointer.nextOrder = ask;
                ask.prevOrder = pointer;
            }
        }
    }

    public void cancelOrder(Order order) {
        if (order.equals(bestOffer)) {
            bestOffer = bestOffer.nextOrder;
            bestOffer.prevOrder = null;
        } else {
            Order pointer = bestOffer;
            while (pointer != null && !(pointer.equals(order))) {
                pointer = pointer.nextOrder;
            }
            if (pointer == null) {
                return;
            }
            pointer.nextOrder.prevOrder = pointer.prevOrder;
            pointer.prevOrder.nextOrder = pointer.nextOrder;
        }
    }

    public List<Trade> fillOrder(Order bid) {
        List<Trade> trades = new ArrayList<>();
        Order pointer = bestOffer;

        while (bid.getSize() > 0 && pointer != null && pointer.getPrice() <= bid.getPrice()) {
            Trade trade;
            if (pointer.getSize() > bid.getSize()) {
                // bid was completely filled, but ask stays on the book
                trade = new Trade(pointer.getPrice(), bid.getSize(), bid.getOrderID(), pointer.getOrderID());
                trades.add(trade);

                pointer.setSize(pointer.getSize() - bid.getSize());
                bid.setSize(0);
                break;

            } else if (pointer.getSize() < bid.getSize()) {
                // bid was partially filled with this ask, so continue
                trade = new Trade(pointer.getPrice(), pointer.getSize(), bid.getOrderID(), pointer.getOrderID());
                trades.add(trade);

                bid.setSize(bid.getSize() - pointer.getSize());
                pointer = pointer.nextOrder;
                bestOffer = pointer;
                bestOffer.prevOrder = null;

            } else {
                // both the ask and the bid are completely filled
                trade = new Trade(pointer.getPrice(), bid.getSize(), bid.getOrderID(), pointer.getOrderID());
                trades.add(trade);
                bid.setSize(0);
                bestOffer = pointer.nextOrder;
                bestOffer.prevOrder = null;
                break;
            }
        }
        return trades;
    }

    public Order getBestOffer() {
        // could consist of multiple orders
        return bestOffer;
    }

    // gives price and net size
    public int[] getBBO() {
        int[] best = new int[2];

        if (bestOffer == null) {
            best[0] = 0;
            best[1] = 0;
            return best;
        }

        best[0] = bestOffer.getPrice();
        int netSize = 0;
        Order pointer = bestOffer;
        while (pointer != null && pointer.getPrice() == bestOffer.getPrice()) {
            netSize += pointer.getSize();
            pointer = pointer.nextOrder;
        }
        best[1] = netSize;
        return best;
    }
}

import java.util.*;


public class OrderBook {

    private BuySide bids;
    private SellSide asks;


    public OrderBook(String bids, String asks) {
        this.bids = new BuySide(bids);
        this.asks = new SellSide(asks);
    }

    private List<Trade> insertBid(Order bid) {
        List<Trade> trades = new ArrayList<>();

        // check if we can fill/partially fill the bid
        if (asks.getBestOffer() != null && asks.getBestOffer().getPrice() <= bid.getPrice()) {

            trades = asks.fillOrder(bid);
            if (bid.getSize() > 0) { // bid was partially filled
                bids.addOrder(bid);
            }
        } else {
            bids.addOrder(bid);
        }
        return trades;
    }

    private List<Trade> insertAsk(Order ask) {

        List<Trade> trades = new ArrayList<>();

        // check if we can fill/partially fill the ask
        if (bids.getBestBid() != null && bids.getBestBid().getPrice() >= ask.getPrice()) {

            trades = bids.fillOrder(ask);
            if (ask.getSize() > 0) {
                asks.addOrder(ask);
            }
        } else {

            asks.addOrder(ask);
        }
        return trades;
    }

    public BBO getCurrentBBO() {
        int[] bb = bids.getBBO();
        int[] bo = asks.getBBO();
        BBO bbo = new BBO(bb[0], bb[1], bo[0], bo[1]);
        return bbo;
    }

    private void cancelBid(Order bid) {
        bids.cancelOrder(bid);
    }

    private void cancelAsk(Order ask) {
        asks.cancelOrder(ask);
    }

    public List<Trade> insertOrder(Order order) {
        if (order.isBuy()) {
            return insertBid(order);
        } else if (order.isSell()) {
            return insertAsk(order);
        }
        return null;
    }

    public void cancelOrder(Order order) {
        // we aren't told if the cancelled order is a buy or a sell
        cancelBid(order);
        cancelAsk(order);
//        if (order.isBuy()) {
//            cancelBid(order);
//        } else if (order.isSell()) {
//            cancelAsk(order);
//        }
    }
}
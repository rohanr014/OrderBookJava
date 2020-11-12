import java.util.Scanner;
import java.util.*;
import java.io.*;

public class App {

    private OrderBook orderBook;

    public static void main(String[] args) {
        App test = new App();
        StringBuilder bboPath = new StringBuilder("");
        StringBuilder tradePath = new StringBuilder("");
        bboPath.append("output/bbos.csv");
        tradePath.append("output/trades.csv");
        test.readCSV("test3.csv", bboPath, tradePath);
    }

    // Your application must define a class named App with a method named run. This
    // method will be imported by the test runner. The run method receives the following
    // arguments:
    // * inputPath: Path to the input CSV file
    // * bboPath: Path to the file you construct that contains BBO CSV output
    // * tradePath: Path to the file you construct that contains trades CSV output
    public void run(String inputPath, StringBuilder bboPath, StringBuilder tradePath) {
        bboPath.append("/output/bbos.csv");
        tradePath.append("/output/trades.csv");
        readCSV(inputPath, bboPath, tradePath);
    }

    public void readCSV(String inputPath, StringBuilder bboPath, StringBuilder tradePath) {
        orderBook = new OrderBook("buy", "sell");
        List<Trade> trades = new ArrayList<>();
        List<BBO> bbos = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(inputPath));
            sc.next();

            while (sc.hasNext()) {
                String[] orderParameters = sc.next().split(",");
                int timestamp = Integer.parseInt(orderParameters[0]);
                String action = orderParameters[1];
                int orderID = Integer.parseInt(orderParameters[2]);
                String side = "";
                int price = -1;
                int size = -1;

                if (orderParameters.length == 6) {
                    side = orderParameters[3];
                    price = Integer.parseInt(orderParameters[4]);
                    size = Integer.parseInt(orderParameters[5]);
                }

                Order order;
                if (action.equals("insert")) {
                    order = new Order(side, timestamp, orderID, price, size);
                    trades.addAll(orderBook.insertOrder(order));
                } else if (action.equals("cancel")) {
                    order = new Order(timestamp, orderID);
                    orderBook.cancelOrder(order);
                }
                bbos.add(orderBook.getCurrentBBO());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        generateBBOCSV(bbos, bboPath);
        generateTradesCSV(trades, tradePath);
    }

    public void generateBBOCSV(List<BBO> bbos, StringBuilder bboPath) {
        try {
            FileWriter csvWriter = new FileWriter(bboPath.toString());
            csvWriter.append("bid_price");
            csvWriter.append(",");
            csvWriter.append("bid_size");
            csvWriter.append(",");
            csvWriter.append("ask_price");
            csvWriter.append(",");
            csvWriter.append("ask_size");
            csvWriter.append("\n");

            for (BBO bbo : bbos) {
                csvWriter.append(bbo.bid_price + "");
                csvWriter.append(",");
                csvWriter.append(bbo.bid_size + "");
                csvWriter.append(",");
                csvWriter.append(bbo.ask_price + "");
                csvWriter.append(",");
                csvWriter.append(bbo.ask_size + "");
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateTradesCSV(List<Trade> trades, StringBuilder tradePath) {
        try {
            FileWriter csvWriter = new FileWriter(tradePath.toString());
            csvWriter.append("trade_price");
            csvWriter.append(",");
            csvWriter.append("trade_size");
            csvWriter.append(",");
            csvWriter.append("buy_order_id");
            csvWriter.append(",");
            csvWriter.append("sell_order_id");
            csvWriter.append("\n");

            for (Trade trade : trades) {
                csvWriter.append(trade.price + "");
                csvWriter.append(",");
                csvWriter.append(trade.size + "");
                csvWriter.append(",");
                csvWriter.append(trade.buyID + "");
                csvWriter.append(",");
                csvWriter.append(trade.sellID + "");
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package DaoHang.utils;

import DaoHang.entity.Stock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewStockSystemUtil {
    public static List<Stock> readStocksFromCSV(String filename) {
        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    String[] value = line.split("\t");
                    if (value.length == 4) {
                        String code = value[0];
                        String productName = value[1];
                        String market =value[2];
                        String sharesIssued = value[3];
                        stocks.add(new Stock(code, productName, market, sharesIssued));
                    }
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("ファイル見つからない、終了する");
            System.exit(0);
        }catch (IOException e){
            System.out.println("異常あり、終了する");
            System.exit(0);
        }
        return stocks;
    }

    public static void showList(List<Stock>stocks) {
        System.out.println("|=====================================================================|");
        System.out.println("|code        |product-name               |market    |shareIssued      |");
        System.out.println("|------------+---------------------------+----------+-----------------|");

        for(Stock stock:stocks){
            System.out.printf("|    %-8s| %-25s | %-8s | %-15s | %n",
                    stock.getCode(),
                    stock.getProductName().length()>20?
                    stock.getProductName().substring(0,20)+"...":
                    stock.getProductName(),
                    stock.getMarket(),
                    String.format("%,15d",Integer.parseInt(
                            String.valueOf(stock.getSharesIssued())))
            );
        }
        System.out.println("|=====================================================================|");
    }

    }

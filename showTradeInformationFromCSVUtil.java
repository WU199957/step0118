package DaoHang.utils;

import DaoHang.entity.TradeInformation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class showTradeInformationFromCSVUtil {

    public static List<TradeInformation> readTradeInformationFromCSV(String filename){
        List<TradeInformation> tradeInfoList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 5) {
                    LocalDateTime tradeDateTime = LocalDateTime.parse(parts[0], formatter);
                    String productName = parts[1];
                    String buyOrSell = parts[2];
                    BigDecimal quantity = new BigDecimal(parts[3]);
                    BigDecimal unitPrice = new BigDecimal(parts[4]);

                    TradeInformation tradeInfo = new TradeInformation(tradeDateTime, productName,
                            buyOrSell, quantity, unitPrice);
                    tradeInfoList.add(tradeInfo);
                }
            }

            tradeInfoList.sort(Comparator.comparing(
                    TradeInformation::getTradeDateTime,Comparator.reverseOrder()));
            printTradeInformation(tradeInfoList);
        }catch (FileNotFoundException e){
            System.out.println("見つかりません");
        }catch (IOException e){
            System.out.println("error");
        }

        return tradeInfoList;
    }
    public static void printTradeInformation(List<TradeInformation>tradeInfoList){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        System.out.println("|日　　　　　　　　　　　\t|銘柄            \t|           売買\t|          数量\t|         取引単価｜");
        System.out.println("--------------------------------------------------------------------------------------------");
        tradeInfoList.forEach(s ->
                System.out.printf("|%-20s\t|%-15s\t|%13s\t|%13s\t|%15s| %n",
                        s.getTradeDateTime().format(formatter),
                        s.getProductName(),
                        s.getBuyOrSell(),
                        s.getQuantity(),
                        s.getUnitPrice()
                ));
    }
}

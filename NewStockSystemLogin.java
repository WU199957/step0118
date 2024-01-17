package DaoHang.service;

import DaoHang.entity.Stock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class NewStockSystemLogin {
    public final static String ouptput = "E:\\testXiangmu\\DaoHang\\src\\DaoHang\\static\\output.csv";
    public static void login(List<Stock> stocks) throws IOException {
        Scanner Sc = new Scanner(System.in);
        System.out.println("新規株式登録");
        String code = null;
        String productName = null;
        String market = null;
        String sharesIssued = null;

        //输入コード
        boolean validCode = false;
        while (!validCode) {
            System.out.print("銘柄コード＞");
            code = Sc.next();
            if (NewStockSystemLogin.isValidCode(code) && isDuplicate(stocks, code)) {
                validCode = true;
            } else {
                System.out.println("输入错误");
                continue;
            }
        }
        // 入力code
        while (true) {
            System.out.print("銘柄名＞ ");
            productName = Sc.next();
            if (productName.isBlank()) {
                System.out.println("銘柄名は空白にできません。再度入力してください。");
                continue;
            } else {
                break;
            }
        }

        //输入市场
        while (true) {
            System.out.println("市场＞");
            market = Sc.next();
            market = market.toUpperCase();
            if (isValidMarket(market)) {
                break;
            } else {
                System.out.println("输入错误");
            }
        }



        boolean validSharesIssued = false;
        while (!validSharesIssued) {
            System.out.println("発行すみ株式数＞");
            sharesIssued = Sc.next();
            if (isValidSharesIssued(sharesIssued)) {
                validSharesIssued = true;
            } else {
                System.out.println("请输入正确数字！");
                continue;
            }
        }
        Stock newStock = new Stock(code, productName, market, sharesIssued);
        stocks.add(newStock);
        Path path = Paths.get(ouptput);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(String.format("%s\t%s\t%s\t%s\n",
                    code, productName, market, sharesIssued));
        }

    }


    public static boolean isValidCode(String code) {
        if (code.length() == 4 && code.matches("[1-9][0-9]{3}")) {
            return true;
        }
        return false;
    }

    public static boolean isValidMarket(String Market) {
        if (Market.equalsIgnoreCase("PRIME") || Market.equalsIgnoreCase("STANDARD") || Market.equalsIgnoreCase("GROWTH")) {
            return true;
        }
        return false;
    }

    public static boolean isValidSharesIssued(String SharesIssued) {
        try {
            Double.parseDouble(SharesIssued);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean containsWhitespace(String str) {
        return Pattern.compile("\\s").matcher(str).find();
    }

    public static boolean isDuplicate(List<Stock> stocks, String code) {
        for (Stock stock : stocks) {
            if (stock.getCode().equals(code)) {
                return false;
            }
        }
        return true;
    }
}

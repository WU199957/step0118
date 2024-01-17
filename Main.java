package DaoHang;

import DaoHang.entity.Stock;
import DaoHang.entity.TradeInformation;
import DaoHang.service.NewSaveTradeInformation;
import DaoHang.service.NewStockSystemLogin;
import DaoHang.service.newPB;
import DaoHang.utils.NewStockSystemUtil;
import DaoHang.utils.showTradeInformationFromCSVUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public final static String ouptput = "E:\\testXiangmu\\DaoHang\\src\\DaoHang\\static\\output.csv";
    public final static String information = "E:\\testXiangmu\\DaoHang\\src\\DaoHang\\static\\trade_information.csv";

    public static void main(String[] args) throws IOException {
        Map<String, BigDecimal> newMarketPrices = null;
        List<Stock> stocks = NewStockSystemUtil.readStocksFromCSV(ouptput);
        newPB portfolio = new newPB(information);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("欢迎来到模拟证券系统");
            System.out.println("1. 股票信息展示");
            System.out.println("2. 股票信息登录");
            System.out.println("3. 买卖");
            System.out.println("4. 买卖查询");
            System.out.println("9. 退出程序");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    NewStockSystemUtil.showList(stocks);
                    break;
                case 2:
                    NewStockSystemLogin.login(stocks);
                    break;
                case 3:
                    // 返回了当前用户输入的参数,之前的修改,再去读取一边cvs,内存里进行修改
                    TradeInformation tradeInformation = NewSaveTradeInformation.recordTradeInformation(stocks);
                    portfolio.update(newMarketPrices,tradeInformation);

                    break;
                case 4:
                    showTradeInformationFromCSVUtil.readTradeInformationFromCSV(information);
                    break;
                case 5:
                    newPB.print(portfolio);
                    break;
                case 6:
                    newMarketPrices = portfolio.getMarketPrice();
                    portfolio.updateMarketPrice(newMarketPrices,null);
                    break;
                case 9:
                    // 退出程序
                    System.out.println("感谢使用模拟证券系统，再见！");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效的选择，请重新选择！");
                    break;
            }
        }
    }
}

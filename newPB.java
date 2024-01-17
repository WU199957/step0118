package DaoHang.service;

import DaoHang.entity.TradeInformation;
import DaoHang.entity.Position;
import DaoHang.entity.Stock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import static DaoHang.utils.NewStockSystemUtil.readStocksFromCSV;
import static DaoHang.utils.showTradeInformationFromCSVUtil.readTradeInformationFromCSV;

public class newPB {

    public final static String ouptput = "E:\\testXiangmu\\DaoHang\\src\\DaoHang\\static\\output.csv";
    private static HashMap<String,Position> allPositions;
    private static List<TradeInformation> tradeRecordList;
    private static Map<String, BigDecimal> marketPriceMap;
    private static List<Stock> stocks;


    // 最开始程序运行的时候执行的方法
    public newPB(String csvFilePath) {
        // 读取cvs文件
        this.tradeRecordList = readTradeInformationFromCSV(csvFilePath); // 正确初始化类成员变量
        this.marketPriceMap = new HashMap<>();
        this.allPositions = new HashMap<>();
        Portfolio(tradeRecordList,null,null);
    }

    // 当按6的时候, 100 100 newMarketPrices
    public void update(Map<String, BigDecimal> newMarketPrices,TradeInformation tradeInformation) {
        // 直接拿内存里的值
        Portfolio(tradeRecordList,newMarketPrices,tradeInformation);
    }

    // 从trade csv文件中读取所有的trade: tradeRecordList <- 假设你已经读取好了
    public void Portfolio(List<TradeInformation> tradeRecordList,Map<String, BigDecimal> newMarketPrices,
                          TradeInformation tradeInformation) {

        // if 没有输入,程序刚启动
        if(newMarketPrices != null){
            // 修改数量和平均价格
            update(tradeInformation);
            // 计算评价额和评价损益
            updateMarketPrice(newMarketPrices,tradeInformation);
        }else {
            query(tradeRecordList);
        }
    }

    public void query(List<TradeInformation> tradeRecordList){
        // 提取出所有的trade name 然后用set去重
        HashSet<String> allName = new HashSet<>();
        for (TradeInformation tradeInformation : tradeRecordList) {
            allName.add(tradeInformation.getProductName());
        }
        // 遍历set<trade name>
        for (String tradeName : allName) {
            // 提取交易记录中名字是遍历中name的所有交易记录 -> 按照时间排序(sorted中自己写)
            List<TradeInformation> neededTradeList = tradeRecordList.stream()
                    .filter(a -> a.getProductName().equals(tradeName))
                    .sorted(Comparator.comparing(TradeInformation::getTradeDateTime))
                    .toList();
            // 遍历交易记录
            Position position = new Position(tradeName);
            for (TradeInformation tradeInformation : neededTradeList) {
                String type = tradeInformation.getBuyOrSell();
                BigDecimal quantity = tradeInformation.getQuantity();
                BigDecimal unitPrice = tradeInformation.getUnitPrice();
                calculate(type,position,quantity,unitPrice);
            }
            position.setValuation(BigDecimal.ZERO);
            position.setUnrealizedProfitAndLoss(BigDecimal.ZERO);
            String company = codeToName(tradeName);
            position.setCompany(company);
            allPositions.put(tradeName,position);
        }
    }

    public void update(TradeInformation tradeInformation){
        Position position = allPositions.get(tradeInformation.getProductName());
        String type = tradeInformation.getBuyOrSell();
        BigDecimal quantity = tradeInformation.getQuantity();
        BigDecimal unitPrice = tradeInformation.getUnitPrice();
        calculate(type,position,quantity,unitPrice);
    }



    //计算数量，单价，损益
    public void calculate (String type,Position position,BigDecimal quantity,BigDecimal price){
        if (type.equals("买")) {
            BigDecimal temp1 = position.getQuantity().multiply(position.getAveragePrice());
            BigDecimal temp2 = quantity.multiply(price);
            BigDecimal temp3 = temp1.add(temp2);
            BigDecimal temp4 = quantity.add(position.getQuantity());
            position.setAveragePrice(temp3.divide(temp4, 2, RoundingMode.HALF_UP));
            position.setQuantity(position.getQuantity().add(quantity));
        } else {
            position.setRealizedProfitLoss(position.getRealizedProfitLoss().
                    add(quantity.multiply(price.subtract(position.getAveragePrice()))));
            position.setQuantity(position.getQuantity().subtract(quantity));
            if (position.getQuantity().intValue() == 0) {
                position.setAveragePrice(null);
            }
        }
    }

    public HashSet<String> rightTradeName() {
        HashSet<String> allName = new HashSet<>();
        for (TradeInformation tradeInformation : tradeRecordList) {
            allName.add(tradeInformation.getProductName());
        }

        return allName;
    }
    public Map<String, BigDecimal> getMarketPrice() {
        Scanner sc = new Scanner(System.in);
        Map<String, BigDecimal> marketPriceMap = new HashMap<>();
        HashSet<String> validTradeNames = rightTradeName();

        for (String tradeName : validTradeNames) {
            System.out.println("请输入 " + tradeName + " 的市场价格:");

            BigDecimal marketPrice;
            while (true) {
                try {
                    marketPrice = sc.nextBigDecimal();
                    break;  // 如果输入有效，退出循环
                } catch (InputMismatchException e) {
                    System.out.println("无效的输入，请输入一个数值:");
                    sc.next();  // 清除无效输入
                }
            }
            marketPriceMap.put(tradeName, marketPrice);
        }

        return marketPriceMap;
    }
    public String codeToName(String code){
        this.stocks=readStocksFromCSV(ouptput);
        Map<String,String>codeAndName=new HashMap<>();
        for (Stock stock : stocks) {
            String stockCode = stock.getCode(); // 使用局部变量stockCode
            String name = stock.getProductName();
            codeAndName.put(stockCode, name);
        }
        return codeAndName.get(code);
    }

    //如果有市场价格的话，计算评价额与评价损益
    public void updateMarketPrice(Map<String, BigDecimal> newMarketPriceMap,
                                  TradeInformation tradeInformation) {
        // tradeInformation 用户输入的值.更新,否则查询, newMarketPriceMap 100
        if(tradeInformation != null){
            Position position = allPositions.get(tradeInformation.getProductName());
            BigDecimal bigDecimal = newMarketPriceMap.get(tradeInformation.getProductName());
            if (bigDecimal != null) {
                // 计算
                BigDecimal multiply = bigDecimal.multiply(position.getQuantity());
                position.setValuation(multiply);
                BigDecimal acquisitionCost = position.getQuantity().multiply(position.getAveragePrice());
                BigDecimal UnrealizedProfitAndLoss = position.getValuation().subtract(acquisitionCost);
                position.setUnrealizedProfitAndLoss(UnrealizedProfitAndLoss);
                allPositions.put(tradeInformation.getProductName(),position);
            } else {
                // 处理 bigDecimal 为 null 的情况
                position.setValuation(BigDecimal.ZERO);
                position.setUnrealizedProfitAndLoss(BigDecimal.ZERO);
            }
        }else {
            // 第一次查询走下面
            allPositions.forEach((k, v) -> {
                BigDecimal bigDecimal = newMarketPriceMap.get(v.getProductName());
                if (bigDecimal != null) {
                    BigDecimal multiply = bigDecimal.multiply(v.getQuantity());
                    v.setValuation(multiply);
                    BigDecimal acquisitionCost = v.getQuantity().multiply(v.getAveragePrice());
                    BigDecimal UnrealizedProfitAndLoss = v.getValuation().subtract(acquisitionCost);
                    v.setUnrealizedProfitAndLoss(UnrealizedProfitAndLoss);
                } else {
                    // 处理 bigDecimal 为 null 的情况
                    v.setValuation(BigDecimal.ZERO);
                    v.setUnrealizedProfitAndLoss(BigDecimal.ZERO);
                }
            });
        }
    }


    public static void print(newPB portfolio){
        System.out.println("code---公司名字-----持有数量----平均取得单价-----实现损益----评价额----评价损益");
        portfolio.allPositions.forEach((k,v) -> {
            System.out.println(
                    k +"--|"+
                            v.getCompany()+"----|"+
                            v.getQuantity() +"---|"+
                            v.getAveragePrice() +"---|"+ v.getRealizedProfitLoss()+"---|"+
                            v.getValuation()+ "---|"+ v.getUnrealizedProfitAndLoss());
        });
    }
}


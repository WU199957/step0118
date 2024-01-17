package DaoHang.service;

import DaoHang.entity.TradeInformation;
import DaoHang.entity.Stock;
import DaoHang.utils.showTradeInformationFromCSVUtil;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class NewSaveTradeInformation {
    public final static String information = "E:\\testXiangmu\\DaoHang\\src\\DaoHang\\static\\trade_information.csv";


    public static TradeInformation recordTradeInformation(List<Stock> stocks) {
        Scanner scanner = new Scanner(System.in);
        try {
            String nextDate;
            LocalDateTime tradeDateTime;
            while (true) {
                // 使用DateTimeFormatter来获取当前日期和时间，精确到分钟
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
                System.out.print("请输入当前年月(例如: 2023-09-22-12:53): ");
                nextDate = scanner.next();
                if (!isValidDateFormat(nextDate)) {
                    System.out.println("请输入标准时间");
                    continue;
                } else {
                    // 获取当前时间
                    LocalDateTime currentDate = LocalDateTime.now();
                    try {
                        tradeDateTime = LocalDateTime.parse(nextDate, dateFormat);
                    } catch (DateTimeParseException e) {
                        System.out.println("日期格式错误");
                        continue;
                    }

                    // 检查输入的时间是否在未来
                    if (tradeDateTime.isAfter(currentDate)) {
                        System.out.println("错误：输入的时间不能是未来时间！");
                        continue;
                    }

                    // 检查输入的时间是否在周一到周五的9:00到15:00之间
                    int dayOfWeek = tradeDateTime.getDayOfWeek().getValue();
                    int hourOfDay = tradeDateTime.getHour();

                    if (dayOfWeek < 1 || dayOfWeek > 5 || hourOfDay < 9 || hourOfDay >= 15) {
                        System.out.println("错误：只能输入周一到周五的9:00到15:00之间的时间！");
                        continue;
                    } else {
                        break;
                    }
                }
            }

            // 检查输入的股票代码是否存在于现有股票列表中
            String code;
            while (true) {
                System.out.print("名字: ");
                code = scanner.next();
                if (isDuplicatName(stocks, code)) {
                    break;
                } else {
                    System.out.println("错误：指定的code不存在于现有股票列表中！");
                    continue;
                }
            }

            String buyOrSell;
            while (true) {
                System.out.print("买卖区分（买/卖）: ");
                buyOrSell = scanner.next();
                if (!buyOrSell.equalsIgnoreCase("买") && !buyOrSell.equalsIgnoreCase("卖")) {
                    System.out.println("错误：只能输入'买'或'卖'！");
                } else {
                    break;
                }
            }

            BigDecimal quantity;
            while (true) {
                System.out.print("数量（多少股）: ");
                try {
                    quantity = new BigDecimal(scanner.next());
                    BigDecimal remainder = quantity.remainder(BigDecimal.valueOf(100));
                    if (!(quantity.compareTo(BigDecimal.ZERO) != 0 || quantity.scale() <= 0)) {
                        System.out.println("输入整数");
                        continue;
                    } else if (!(quantity.intValue() % 100 == 0)) {
                        System.out.println("数字是以100为单位");
                        continue;
                    } else {//增加的地方
                        BigDecimal totalQuantity = getTotalBoughtQuantity(code);
                        if (buyOrSell.equals("卖") && quantity.intValue()>totalQuantity.intValue()) {
                            System.out.println("持有额度不够");
                        } else {
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("请输入有效的数字");
                }
            }


            BigDecimal unitPrice;
            while (true) {
                System.out.println("请输入价格: ");
                if (scanner.hasNextBigDecimal()) { // 检查输入是否可以解析为BigDecimal
                    unitPrice = scanner.nextBigDecimal(); // 读取BigDecimal值
                    unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
                    if (unitPrice.compareTo(BigDecimal.ZERO) > 0) { // 检查输入是否为正数
                        break; // 输入有效，退出循环
                    } else {
                        System.out.println("错误：请输入正数！");
                    }
                } else {
                    System.out.println("错误：请输入有效的数字！");
                    scanner.next(); // 丢弃无效输入
                }
            }

            // 创建 TradeInformation 对象并保存到CSV文件
            TradeInformation tradeInfo = new TradeInformation(tradeDateTime,
                    code, buyOrSell, quantity, unitPrice);
            saveTradeInformationToCSV(tradeInfo, information);

            System.out.println("买卖情报录入成功！");
            return tradeInfo;
        } catch (Exception e) {
            System.out.println("录入买卖情报时发生错误：" + e.getMessage());
        }
        return null;
    }


    private static boolean isDuplicatName(List<Stock> stocks, String code) {
        for (Stock stock : stocks) {
            if (stock.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }


    private static void saveTradeInformationToCSV(TradeInformation tradeInfo, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            boolean hasHeader = false; // 标记文件是否已经有头部行

            // 检查文件是否已经包含头部行
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String firstLine = reader.readLine();

            }
            // 如果文件没有头部行，则添加头部行

            // 使用制表符分隔字段并写入文件
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
            writer.printf("%s\t%s\t%s\t%s\t%s\n",
                    tradeInfo.getTradeDateTime().format(formatter),
                    tradeInfo.getProductName(),
                    tradeInfo.getBuyOrSell(),
                    tradeInfo.getQuantity(),
                    tradeInfo.getUnitPrice());
            System.out.println("买卖情报已成功保存到 " + filePath);
        } catch (IOException e) {
            System.out.println("保存买卖情报到CSV文件时发生错误：" + e.getMessage());
        }
    }

    private static boolean isValidDateFormat(String date) {
        // 使用正则表达式检查日期格式
        String pattern = "\\d{4}-\\d{2}-\\d{2}-\\d{2}:\\d{2}";
        return date.matches(pattern);
    }

    private static boolean containsOnlyDigitsAndHyphen(String input) {
        // 使用正则表达式检查输入是否只包含数字和连字符
        String pattern = "^[0-9-]+$";
        return input.matches(pattern);
    }
//改动的地方
    private static BigDecimal getTotalBoughtQuantity(String code) throws FileNotFoundException {
        List<TradeInformation> trades =
                showTradeInformationFromCSVUtil.readTradeInformationFromCSV(information);
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (TradeInformation trade : trades) {
            if (trade.getProductName().equals(code) && trade.getBuyOrSell().equalsIgnoreCase("买")) {
                totalQuantity = totalQuantity.add(trade.getQuantity());
            } else if (trade.getProductName().equals(code) && "卖".equals(trade.getBuyOrSell())) {
                totalQuantity = totalQuantity.subtract(trade.getQuantity());
            }
        }

        return totalQuantity;
    }


}




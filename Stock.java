package DaoHang.entity;

public class Stock {
    private String code;
    private String productName;
    private String market;
    private String sharesIssued;

    public Stock(String code, String productName, String market, String sharesIssued) {
        this.code = code;
        this.productName = productName;
        this.market = market;
        this.sharesIssued = sharesIssued;
    }

    public String getCode() {
        return code;
    }

    public String getProductName() {
        return productName;
    }

    public String getMarket() {
        return market;
    }

    public String getSharesIssued() {
        return sharesIssued;
    }
}

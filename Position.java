package DaoHang.entity;

import java.math.BigDecimal;

public class Position {
    private String ProductName;
    private String company;
    private BigDecimal quantity;
    private BigDecimal AveragePrice;
    private BigDecimal realizedProfitLoss;
    private BigDecimal valuation;
    private BigDecimal UnrealizedProfitAndLoss;

    public Position(String ProductName) {
        this.ProductName = ProductName;
        this.company=company;
        this.quantity = BigDecimal.ZERO;
        this.AveragePrice = BigDecimal.ZERO;
        this.realizedProfitLoss = BigDecimal.ZERO;
        this.valuation=BigDecimal.ZERO;
        this.UnrealizedProfitAndLoss=BigDecimal.ZERO;
    }

    public Position(String productName, String company,BigDecimal quantity, BigDecimal AveragePrice, BigDecimal valuation) {
        ProductName = productName;
        company=company;
        this.quantity = quantity;
        this.AveragePrice = AveragePrice;
        this.valuation=valuation;
        this.UnrealizedProfitAndLoss=UnrealizedProfitAndLoss;
    }

    public BigDecimal getRealizedProfitLoss() {
        return realizedProfitLoss;
    }

    public void setRealizedProfitLoss(BigDecimal realizedProfitLoss) {
        this.realizedProfitLoss = realizedProfitLoss;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAveragePrice() {
        return AveragePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.AveragePrice = averagePrice;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public void setValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }

    public BigDecimal getUnrealizedProfitAndLoss() {
        return UnrealizedProfitAndLoss;
    }

    public void setUnrealizedProfitAndLoss(BigDecimal unrealizedProfitAndLoss) {
        UnrealizedProfitAndLoss = unrealizedProfitAndLoss;
    }
}



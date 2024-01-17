package DaoHang.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TradeInformation {
    private LocalDateTime tradeDateTime;
    private String productName;
    private String buyOrSell;
    private BigDecimal quantity;
    private BigDecimal unitPrice;

    public TradeInformation(LocalDateTime tradeDateTime, String productName, String buyOrSell, BigDecimal quantity, BigDecimal unitPrice) {
        this.tradeDateTime = tradeDateTime;
        this.productName = productName;
        this.buyOrSell = buyOrSell;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getTradeDateTime() {
        return tradeDateTime;
    }

    public void setTradeDateTime(LocalDateTime tradeDateTime) {
        this.tradeDateTime = tradeDateTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

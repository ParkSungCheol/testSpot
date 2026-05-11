package com.example.testspot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Upbit API에서 반환하는 틱 정보 (가격, 거래량 등)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpbitTicker {
    @JsonProperty("market")
    private String market;

    @JsonProperty("trade_price")
    private Double tradePrice;

    @JsonProperty("prev_closing_price")
    private Double prevClosingPrice;

    @JsonProperty("acc_trade_price_24h")
    private Double accTradePrice24h;

    @JsonProperty("acc_trade_volume_24h")
    private Double accTradeVolume24h;

    @JsonProperty("highest_52_week_price")
    private Double highest52WeekPrice;

    @JsonProperty("lowest_52_week_price")
    private Double lowest52WeekPrice;

    @JsonProperty("change_percent")
    private Double changePercent;
}

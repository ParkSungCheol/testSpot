package com.example.testspot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 암호화폐 상세 정보 (Upbit + CoinGecko 통합)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoInfo {
    // Upbit 정보
    private String market;
    private String koreanName;
    private String englishName;
    private Double tradePrice;
    private Double accTradePrice24h;
    private Double accTradeVolume24h;
    private Double highest52WeekPrice;
    private Double lowest52WeekPrice;
    private Double changePercent;

    // CoinGecko 정보
    private String coinId;
    private String symbol;
    private String name;
    private Double marketCap;
    private Integer marketCapRank;
    private Double totalVolume;
    private Double high24h;
    private Double low24h;
    private Double priceChange24h;
    private Double priceChangePercentage24h;
    private Double circulatingSupply;
    private Double totalSupply;
    private Double ath;
    private Double atl;
}

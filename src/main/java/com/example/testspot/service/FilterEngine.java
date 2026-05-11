package com.example.testspot.service;

import com.example.testspot.model.FilterCondition;
import com.example.testspot.model.CryptoInfo;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 필터 엔진 - 조건에 맞는 종목을 필터링
 */
@Component
public class FilterEngine {

    /**
     * 주어진 조건들과 암호화폐 정보 목록으로 필터링 수행
     */
    public List<CryptoInfo> applyFilters(List<CryptoInfo> cryptoList, List<FilterCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return cryptoList;
        }

        return cryptoList.stream()
                .filter(crypto -> matchesAllConditions(crypto, conditions))
                .collect(Collectors.toList());
    }

    /**
     * 단일 암호화폐가 모든 조건을 만족하는지 확인
     */
    private boolean matchesAllConditions(CryptoInfo crypto, List<FilterCondition> conditions) {
        return conditions.stream()
                .allMatch(condition -> matchesCondition(crypto, condition));
    }

    /**
     * 단일 암호화폐가 하나의 조건을 만족하는지 확인
     */
    private boolean matchesCondition(CryptoInfo crypto, FilterCondition condition) {
        Double value = getPropertyValue(crypto, condition.getFilterType());

        if (value == null || condition.getValue() == null) {
            return false;
        }

        String operator = condition.getOperator();
        Double conditionValue = condition.getValue();

        switch (operator) {
            case "GREATER_THAN":
                return value > conditionValue;
            case "LESS_THAN":
                return value < conditionValue;
            case "GREATER_THAN_OR_EQUAL":
                return value >= conditionValue;
            case "LESS_THAN_OR_EQUAL":
                return value <= conditionValue;
            case "EQUALS":
                return value.equals(conditionValue);
            case "NOT_EQUALS":
                return !value.equals(conditionValue);
            default:
                return false;
        }
    }

    /**
     * 필터 타입에 따라 CryptoInfo에서 해당 값을 추출
     */
    private Double getPropertyValue(CryptoInfo crypto, String filterType) {
        switch (filterType) {
            case "MARKET_CAP":
                return crypto.getMarketCap() != null ? crypto.getMarketCap() : 0.0;
            case "TRADE_VOLUME":
                return crypto.getTotalVolume() != null ? crypto.getTotalVolume() : 0.0;
            case "PRICE_CHANGE_24H":
                return crypto.getPriceChange24h() != null ? crypto.getPriceChange24h() : 0.0;
            case "PRICE_CHANGE_PERCENTAGE_24H":
                return crypto.getPriceChangePercentage24h() != null ? crypto.getPriceChangePercentage24h() : 0.0;
            case "TRADE_PRICE":
                return crypto.getTradePrice() != null ? crypto.getTradePrice() : 0.0;
            case "ACC_TRADE_PRICE_24H":
                return crypto.getAccTradePrice24h() != null ? crypto.getAccTradePrice24h() : 0.0;
            case "ACC_TRADE_VOLUME_24H":
                return crypto.getAccTradeVolume24h() != null ? crypto.getAccTradeVolume24h() : 0.0;
            case "MARKET_CAP_RANK":
                return crypto.getMarketCapRank() != null ? crypto.getMarketCapRank().doubleValue() : 0.0;
            case "HIGHEST_52_WEEK_PRICE":
                return crypto.getHighest52WeekPrice() != null ? crypto.getHighest52WeekPrice() : 0.0;
            case "LOWEST_52_WEEK_PRICE":
                return crypto.getLowest52WeekPrice() != null ? crypto.getLowest52WeekPrice() : 0.0;
            case "CIRCULATING_SUPPLY":
                return crypto.getCirculatingSupply() != null ? crypto.getCirculatingSupply() : 0.0;
            case "TOTAL_SUPPLY":
                return crypto.getTotalSupply() != null ? crypto.getTotalSupply() : 0.0;
            case "ATH":
                return crypto.getAth() != null ? crypto.getAth() : 0.0;
            case "ATL":
                return crypto.getAtl() != null ? crypto.getAtl() : 0.0;
            default:
                return 0.0;
        }
    }
}

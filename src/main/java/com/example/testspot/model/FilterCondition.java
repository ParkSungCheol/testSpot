package com.example.testspot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자가 설정한 필터 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCondition {
    // 필터 타입: MARKET_CAP, TRADE_VOLUME, PRICE_CHANGE, ETC
    private String filterType;

    // 연산자: GREATER_THAN, LESS_THAN, EQUALS, ETC
    private String operator;

    // 필터 값
    private Double value;

    // 필터 설명
    private String description;
}

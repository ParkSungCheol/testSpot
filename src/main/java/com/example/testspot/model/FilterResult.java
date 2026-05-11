package com.example.testspot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 필터링 결과 응답
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterResult {
    // 조건을 만족하는 종목 리스트
    private List<String> matchingMarkets;

    // 필터링된 데이터 상세 정보
    private List<CryptoInfo> details;
}

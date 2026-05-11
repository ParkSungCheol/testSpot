package com.example.testspot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 클라이언트에서 받는 필터 요청
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    // 적용할 필터 조건 목록
    private List<FilterCondition> conditions;
}

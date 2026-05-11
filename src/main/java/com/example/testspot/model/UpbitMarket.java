package com.example.testspot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Upbit API에서 반환하는 마켓 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpbitMarket {
    @JsonProperty("market")
    private String market;

    @JsonProperty("korean_name")
    private String koreanName;

    @JsonProperty("english_name")
    private String englishName;

    @JsonProperty("market_warning")
    private String marketWarning;
}

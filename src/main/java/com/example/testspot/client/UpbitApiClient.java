package com.example.testspot.client;

import com.example.testspot.model.UpbitMarket;
import com.example.testspot.model.UpbitTicker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Upbit API 클라이언트
 * API 키는 환경변수로 설정: UPBIT_API_KEY
 */
@Component
public class UpbitApiClient {
    private static final String UPBIT_BASE_URL = "https://api.upbit.com/v1";
    // TODO: API 키 설정 - 환경변수 또는 application.properties에서 가져오기
    // private String apiKey = System.getenv("UPBIT_API_KEY");

    private final RestTemplate restTemplate;

    public UpbitApiClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 모든 KRW 마켓 조회
     */
    public List<UpbitMarket> getAllKrwMarkets() {
        try {
            String url = UPBIT_BASE_URL + "/market/all";
            UpbitMarket[] markets = restTemplate.getForObject(url, UpbitMarket[].class);

            if (markets != null) {
                return Arrays.stream(markets)
                        .filter(market -> market.getMarket().startsWith("KRW-"))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Upbit API 호출 실패: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * 특정 마켓의 현재 가격 정보 조회
     */
    public UpbitTicker getTicker(String market) {
        try {
            String url = UPBIT_BASE_URL + "/ticker?markets=" + market;
            UpbitTicker[] tickers = restTemplate.getForObject(url, UpbitTicker[].class);

            if (tickers != null && tickers.length > 0) {
                return tickers[0];
            }
        } catch (Exception e) {
            System.err.println("Upbit Ticker 조회 실패: " + e.getMessage());
        }
        return null;
    }

    /**
     * 여러 마켓의 가격 정보 조회
     */
    public List<UpbitTicker> getTickers(List<String> markets) {
        try {
            String marketString = String.join(",", markets);
            String url = UPBIT_BASE_URL + "/ticker?markets=" + marketString;
            UpbitTicker[] tickers = restTemplate.getForObject(url, UpbitTicker[].class);

            if (tickers != null) {
                return Arrays.asList(tickers);
            }
        } catch (Exception e) {
            System.err.println("Upbit Tickers 조회 실패: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * Upbit API 헤더 설정
     * TODO: API 키를 사용하여 인증 헤더 설정
     */
    private HttpEntity<?> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        // headers.set("Authorization", "Bearer " + apiKey);
        return new HttpEntity<>(headers);
    }
}

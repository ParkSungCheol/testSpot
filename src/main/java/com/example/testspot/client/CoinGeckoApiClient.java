package com.example.testspot.client;

import com.example.testspot.model.CoinGeckoData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

/**
 * CoinGecko Demo API 클라이언트
 * Demo API는 인증이 필요없음
 */
@Component
public class CoinGeckoApiClient {
    private static final String COINGECKO_BASE_URL = "https://api.coingecko.com/api/v3";
    // TODO: Pro API 사용 시 API 키 설정
    // private String apiKey = System.getenv("COINGECKO_API_KEY");

    private final RestTemplate restTemplate;

    public CoinGeckoApiClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 상위 N개의 암호화폐 정보 조회
     */
    public List<CoinGeckoData> getTopCryptos(int limit) {
        try {
            String url = COINGECKO_BASE_URL + "/coins/markets?"
                    + "vs_currency=krw"
                    + "&order=market_cap_desc"
                    + "&per_page=" + limit
                    + "&page=1"
                    + "&sparkline=false";

            CoinGeckoData[] cryptos = restTemplate.getForObject(url, CoinGeckoData[].class);
            return cryptos != null ? Arrays.asList(cryptos) : List.of();
        } catch (Exception e) {
            System.err.println("CoinGecko API 호출 실패: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * 특정 암호화폐 정보 조회
     */
    public CoinGeckoData getCryptoData(String cryptoId) {
        try {
            String url = COINGECKO_BASE_URL + "/coins/" + cryptoId
                    + "?localization=false"
                    + "&tickers=false"
                    + "&market_data=true"
                    + "&community_data=false"
                    + "&developer_data=false";

            return restTemplate.getForObject(url, CoinGeckoData.class);
        } catch (Exception e) {
            System.err.println("CoinGecko 상세 조회 실패: " + e.getMessage());
        }
        return null;
    }

    /**
     * 검색어로 암호화폐 검색
     */
    public List<CoinGeckoData> searchCryptos(String query, int limit) {
        try {
            String url = COINGECKO_BASE_URL + "/coins/markets?"
                    + "vs_currency=krw"
                    + "&order=market_cap_desc"
                    + "&per_page=" + limit
                    + "&page=1"
                    + "&sparkline=false";

            CoinGeckoData[] cryptos = restTemplate.getForObject(url, CoinGeckoData[].class);
            return cryptos != null ? Arrays.asList(cryptos) : List.of();
        } catch (Exception e) {
            System.err.println("CoinGecko 검색 실패: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * 사용 가능한 모든 암호화폐 목록 조회
     */
    public List<CoinGeckoData> getAllCryptos(int limit) {
        return getTopCryptos(limit);
    }
}

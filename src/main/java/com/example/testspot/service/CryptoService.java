package com.example.testspot.service;

import com.example.testspot.client.UpbitApiClient;
import com.example.testspot.client.CoinGeckoApiClient;
import com.example.testspot.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 암호화폐 데이터 관리 및 필터링 서비스
 */
@Service
public class CryptoService {
    private final UpbitApiClient upbitApiClient;
    private final CoinGeckoApiClient coinGeckoApiClient;
    private final FilterEngine filterEngine;

    // 캐시: 마켓 정보
    private List<UpbitMarket> cachedMarkets;
    private List<CoinGeckoData> cachedCoinGeckoData;
    private List<CryptoInfo> cachedCryptoInfoList;
    private long lastFetchTime = 0;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5분

    public CryptoService(UpbitApiClient upbitApiClient, CoinGeckoApiClient coinGeckoApiClient, FilterEngine filterEngine) {
        this.upbitApiClient = upbitApiClient;
        this.coinGeckoApiClient = coinGeckoApiClient;
        this.filterEngine = filterEngine;
    }

    /**
     * Upbit의 모든 KRW 종목 조회
     */
    public List<UpbitMarket> getAllKrwMarkets() {
        if (isCacheValid() && cachedMarkets != null) {
            return cachedMarkets;
        }
        cachedMarkets = upbitApiClient.getAllKrwMarkets();
        return cachedMarkets;
    }

    /**
     * CoinGecko 데이터 조회
     */
    public List<CoinGeckoData> getCoinGeckoData(int limit) {
        if (isCacheValid() && cachedCoinGeckoData != null) {
            return cachedCoinGeckoData;
        }
        cachedCoinGeckoData = coinGeckoApiClient.getTopCryptos(limit);
        return cachedCoinGeckoData;
    }

    /**
     * 필터링 가능한 조건 목록 반환
     */
    public Map<String, String> getAvailableFilters() {
        Map<String, String> filters = new LinkedHashMap<>();
        filters.put("MARKET_CAP", "시가총액");
        filters.put("TRADE_VOLUME", "거래량 (24h)");
        filters.put("PRICE_CHANGE_24H", "가격 변화 (24h)");
        filters.put("PRICE_CHANGE_PERCENTAGE_24H", "가격 변화율 (24h %)");
        filters.put("TRADE_PRICE", "현재 거래가");
        filters.put("ACC_TRADE_PRICE_24H", "누적 거래금액 (24h)");
        filters.put("ACC_TRADE_VOLUME_24H", "누적 거래량 (24h)");
        filters.put("MARKET_CAP_RANK", "시가총액 순위");
        filters.put("HIGHEST_52_WEEK_PRICE", "52주 최고가");
        filters.put("LOWEST_52_WEEK_PRICE", "52주 최저가");
        filters.put("CIRCULATING_SUPPLY", "유통 공급량");
        filters.put("TOTAL_SUPPLY", "총 공급량");
        filters.put("ATH", "역대 최고가");
        filters.put("ATL", "역대 최저가");
        return filters;
    }

    /**
     * 모든 암호화폐 정보 통합 조회 (Upbit + CoinGecko)
     */
    public List<CryptoInfo> getAllCryptoInfo(int coinGeckoLimit) {
        if (isCacheValid() && cachedCryptoInfoList != null) {
            return cachedCryptoInfoList;
        }

        List<UpbitMarket> upbitMarkets = getAllKrwMarkets();
        List<CoinGeckoData> coinGeckoDataList = getCoinGeckoData(coinGeckoLimit);

        // Symbol을 기준으로 매핑
        Map<String, CoinGeckoData> coinGeckoMap = coinGeckoDataList.stream()
                .collect(Collectors.toMap(
                        data -> (data.getSymbol() != null ? data.getSymbol().toUpperCase() : ""),
                        data -> data,
                        (existing, replacement) -> existing
                ));

        List<CryptoInfo> result = new ArrayList<>();

        for (UpbitMarket market : upbitMarkets) {
            try {
                CryptoInfo info = new CryptoInfo();
                info.setMarket(market.getMarket());
                info.setKoreanName(market.getKoreanName());
                info.setEnglishName(market.getEnglishName());

                // Upbit Ticker 정보 조회
                UpbitTicker ticker = upbitApiClient.getTicker(market.getMarket());
                if (ticker != null) {
                    info.setTradePrice(ticker.getTradePrice());
                    info.setAccTradePrice24h(ticker.getAccTradePrice24h());
                    info.setAccTradeVolume24h(ticker.getAccTradeVolume24h());
                    info.setHighest52WeekPrice(ticker.getHighest52WeekPrice());
                    info.setLowest52WeekPrice(ticker.getLowest52WeekPrice());
                    info.setChangePercent(ticker.getChangePercent());
                }

                // CoinGecko 정보 병합
                String symbol = market.getEnglishName() != null ?
                        market.getEnglishName().toUpperCase() : "";
                if (coinGeckoMap.containsKey(symbol)) {
                    CoinGeckoData geckoData = coinGeckoMap.get(symbol);
                    info.setCoinId(geckoData.getId());
                    info.setSymbol(geckoData.getSymbol());
                    info.setName(geckoData.getName());
                    info.setMarketCap(geckoData.getMarketCap());
                    info.setMarketCapRank(geckoData.getMarketCapRank());
                    info.setTotalVolume(geckoData.getTotalVolume());
                    info.setHigh24h(geckoData.getHigh24h());
                    info.setLow24h(geckoData.getLow24h());
                    info.setPriceChange24h(geckoData.getPriceChange24h());
                    info.setPriceChangePercentage24h(geckoData.getPriceChangePercentage24h());
                    info.setCirculatingSupply(geckoData.getCirculatingSupply());
                    info.setTotalSupply(geckoData.getTotalSupply());
                    info.setAth(geckoData.getAth());
                    info.setAtl(geckoData.getAtl());
                }

                result.add(info);
            } catch (Exception e) {
                System.err.println("암호화폐 정보 통합 실패: " + market.getMarket() + " - " + e.getMessage());
            }
        }

        cachedCryptoInfoList = result;
        lastFetchTime = System.currentTimeMillis();
        return result;
    }

    /**
     * 필터 조건 적용하여 결과 반환
     */
    public FilterResult applyFilters(FilterRequest filterRequest) {
        List<CryptoInfo> allCryptos = getAllCryptoInfo(250);
        List<CryptoInfo> filtered = filterEngine.applyFilters(allCryptos, filterRequest.getConditions());

        FilterResult result = new FilterResult();
        result.setMatchingMarkets(filtered.stream()
                .map(CryptoInfo::getMarket)
                .collect(Collectors.toList()));
        result.setDetails(filtered);

        return result;
    }

    /**
     * 캐시 유효성 확인
     */
    private boolean isCacheValid() {
        return (System.currentTimeMillis() - lastFetchTime) < CACHE_DURATION;
    }

    /**
     * 캐시 초기화
     */
    public void clearCache() {
        cachedMarkets = null;
        cachedCoinGeckoData = null;
        cachedCryptoInfoList = null;
        lastFetchTime = 0;
    }
}

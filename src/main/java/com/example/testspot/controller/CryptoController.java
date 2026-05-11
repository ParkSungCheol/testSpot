package com.example.testspot.controller;

import com.example.testspot.model.*;
import com.example.testspot.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 암호화폐 데이터 및 필터링 API 컨트롤러
 */
@RestController
@RequestMapping("/api/crypto")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * Upbit의 모든 KRW 마켓 조회
     */
    @GetMapping("/markets/krw")
    public ResponseEntity<List<UpbitMarket>> getKrwMarkets() {
        List<UpbitMarket> markets = cryptoService.getAllKrwMarkets();
        return ResponseEntity.ok(markets);
    }

    /**
     * CoinGecko 데이터 조회
     */
    @GetMapping("/coingecko")
    public ResponseEntity<List<CoinGeckoData>> getCoinGeckoData(
            @RequestParam(defaultValue = "250") int limit) {
        List<CoinGeckoData> data = cryptoService.getCoinGeckoData(limit);
        return ResponseEntity.ok(data);
    }

    /**
     * 필터링 가능한 모든 조건 목록 조회
     */
    @GetMapping("/filters/available")
    public ResponseEntity<Map<String, String>> getAvailableFilters() {
        Map<String, String> filters = cryptoService.getAvailableFilters();
        return ResponseEntity.ok(filters);
    }

    /**
     * 모든 암호화폐 정보 조회 (Upbit + CoinGecko 통합)
     */
    @GetMapping("/all")
    public ResponseEntity<List<CryptoInfo>> getAllCryptoInfo(
            @RequestParam(defaultValue = "250") int limit) {
        List<CryptoInfo> cryptoInfoList = cryptoService.getAllCryptoInfo(limit);
        return ResponseEntity.ok(cryptoInfoList);
    }

    /**
     * 필터 조건 적용하여 종목 필터링
     */
    @PostMapping("/filter")
    public ResponseEntity<FilterResult> applyFilters(@RequestBody FilterRequest filterRequest) {
        FilterResult result = cryptoService.applyFilters(filterRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * 캐시 초기화
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        cryptoService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }
}

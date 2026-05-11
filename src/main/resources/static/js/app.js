const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            // 시장 데이터
            upbitMarkets: [],
            filteredUpbitMarkets: [],
            upbitSearchQuery: '',
            availableFilters: {},
            allCryptoInfo: [],
            selectedCryptoInfo: null,
            highlightedMarkets: [],

            // 필터 관련
            selectedFilters: [],
            filterResult: null,
            showFilterModal: false,
            filterModalIndex: -1,
            filterModalData: {
                filterType: '',
                operator: 'GREATER_THAN',
                value: null,
                description: ''
            },
            draggedFilterType: null
        };
    },
    computed: {},
    methods: {
        // API 호출
        async fetchUpbitMarkets() {
            try {
                const response = await fetch('/api/crypto/markets/krw');
                this.upbitMarkets = await response.json();
                this.filteredUpbitMarkets = this.upbitMarkets;
                console.log('Upbit Markets loaded:', this.upbitMarkets.length);
            } catch (error) {
                console.error('Failed to fetch Upbit markets:', error);
                alert('업비트 종목을 불러오는데 실패했습니다.');
            }
        },

        async fetchAvailableFilters() {
            try {
                const response = await fetch('/api/crypto/filters/available');
                this.availableFilters = await response.json();
                console.log('Available filters loaded:', this.availableFilters);
            } catch (error) {
                console.error('Failed to fetch filters:', error);
            }
        },

        async fetchAllCryptoInfo() {
            try {
                const response = await fetch('/api/crypto/all?limit=250');
                this.allCryptoInfo = await response.json();
                console.log('All crypto info loaded:', this.allCryptoInfo.length);
            } catch (error) {
                console.error('Failed to fetch crypto info:', error);
            }
        },

        async executeFilters() {
            if (this.selectedFilters.length === 0) {
                alert('필터 조건을 설정해주세요.');
                return;
            }

            try {
                const request = {
                    conditions: this.selectedFilters
                };

                const response = await fetch('/api/crypto/filter', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(request)
                });

                this.filterResult = await response.json();
                this.highlightedMarkets = this.filterResult.matchingMarkets;
                console.log('Filter result:', this.filterResult);
            } catch (error) {
                console.error('Failed to apply filters:', error);
                alert('필터 실행 중 오류가 발생했습니다.');
            }
        },

        // UI 이벤트
        filterUpbitMarkets() {
            const query = this.upbitSearchQuery.toLowerCase();
            this.filteredUpbitMarkets = this.upbitMarkets.filter(market => 
                market.koreanName.toLowerCase().includes(query) ||
                market.englishName.toLowerCase().includes(query) ||
                market.market.toLowerCase().includes(query)
            );
        },

        startDrag(event, filterType) {
            this.draggedFilterType = filterType;
            event.dataTransfer.effectAllowed = 'copy';
            event.target.classList.add('dragging');
        },

        handleDrop(event) {
            event.preventDefault();
            if (this.draggedFilterType) {
                const newFilter = {
                    filterType: this.draggedFilterType,
                    operator: 'GREATER_THAN',
                    value: null,
                    description: this.availableFilters[this.draggedFilterType] || ''
                };
                this.selectedFilters.push(newFilter);
                this.draggedFilterType = null;
            }
        },

        editFilter(index) {
            this.filterModalIndex = index;
            const filter = this.selectedFilters[index];
            this.filterModalData = {
                filterType: filter.filterType,
                operator: filter.operator || 'GREATER_THAN',
                value: filter.value || null,
                description: filter.description || ''
            };
            this.showFilterModal = true;
        },

        closeFilterModal() {
            this.showFilterModal = false;
            this.filterModalIndex = -1;
            this.filterModalData = {
                filterType: '',
                operator: 'GREATER_THAN',
                value: null,
                description: ''
            };
        },

        saveFilterModal() {
            if (this.filterModalIndex >= 0) {
                this.selectedFilters[this.filterModalIndex] = {
                    ...this.filterModalData
                };
            }
            this.closeFilterModal();
        },

        removeFilter(index) {
            this.selectedFilters.splice(index, 1);
        },

        resetFilters() {
            this.selectedFilters = [];
            this.filterResult = null;
            this.highlightedMarkets = [];
        },

        selectMarket(market) {
            const cryptoInfo = this.allCryptoInfo.find(info => info.market === market.market);
            if (cryptoInfo) {
                this.selectedCryptoInfo = cryptoInfo;
            } else {
                // API에서 다시 조회
                this.selectedCryptoInfo = market;
            }
        },

        formatNumber(value, decimals = 0) {
            if (value === null || value === undefined) return 'N/A';
            return new Intl.NumberFormat('ko-KR', {
                minimumFractionDigits: decimals,
                maximumFractionDigits: decimals
            }).format(value);
        }
    },
    mounted() {
        console.log('App mounted');
        this.fetchUpbitMarkets();
        this.fetchAvailableFilters();
        this.fetchAllCryptoInfo();

        // 마켓 클릭 이벤트
        this.$watch(() => this.filteredUpbitMarkets, () => {
            this.$nextTick(() => {
                const marketItems = document.querySelectorAll('.market-item');
                marketItems.forEach(item => {
                    item.addEventListener('click', (e) => {
                        const market = this.upbitMarkets.find(
                            m => m.market === item.querySelector('.market-code').textContent
                        );
                        if (market) {
                            this.selectMarket(market);
                        }
                    });
                });
            });
        }, { deep: true });
    }
});

app.mount('#app');

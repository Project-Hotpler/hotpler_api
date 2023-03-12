package com.example.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SearchDto {

    @Getter
    @NoArgsConstructor
    public static class SearchRequestDto{
        private int page;
        private int recordSize;
        private String keyword;
        private String searchType;

        @Builder
        public SearchRequestDto(int page, int recordSize, String keyword, String searchType) {
            this.page = page;
            this.recordSize = recordSize;
            this.keyword = keyword;
            this.searchType = searchType;
        }

        public int getOffset() {
            return (page - 1) * recordSize;
        }
    }

}

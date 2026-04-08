package com.horizonx.recruiting_microservices.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private long totalRecords;
    private int totalPages;
}

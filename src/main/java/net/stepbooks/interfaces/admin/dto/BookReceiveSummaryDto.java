package net.stepbooks.interfaces.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)

public class BookReceiveSummaryDto {

    private List<BookSeriesDto> series;

    private List<BookDto> books;

}

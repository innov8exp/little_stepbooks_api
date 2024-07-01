package net.stepbooks.domain.wdt.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WdtTrade {

    private String recId;
    private String shopNo;
    private String tid;

    private String logisticsId;
    private String logisticsType;
    private String logisticsName;
    private String logisticsNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consignTime;

}

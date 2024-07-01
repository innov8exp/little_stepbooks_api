package net.stepbooks.domain.wdt.service.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WdtLogisticsSyncResponse extends WdtBasicResponse {

    private List<WdtTrade> trades;
}

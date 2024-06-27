package net.stepbooks.domain.wdt.service.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WdtGoodsSpecPushResponse extends WdtBasicResponse {

    private Integer newCount;

    private Integer changeCount;
}

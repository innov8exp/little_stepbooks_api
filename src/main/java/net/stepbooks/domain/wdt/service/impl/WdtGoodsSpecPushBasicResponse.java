package net.stepbooks.domain.wdt.service.impl;

import lombok.Data;

@Data
public class WdtGoodsSpecPushBasicResponse extends WdtBasicResponse {

    private int newCount;

    private int changeCount;
}

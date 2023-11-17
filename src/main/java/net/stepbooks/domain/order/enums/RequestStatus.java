package net.stepbooks.domain.order.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    PENDING, APPROVED, REJECTED, CANCELLED;
}

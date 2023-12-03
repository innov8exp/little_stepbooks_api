package net.stepbooks.infrastructure.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseDto implements Serializable {

    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}

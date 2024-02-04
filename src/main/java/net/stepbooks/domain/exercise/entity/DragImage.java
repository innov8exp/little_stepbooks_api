package net.stepbooks.domain.exercise.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DragImage {

    int id;
    private String img;
    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal mintrueX;
    private BigDecimal maxtrueX;
    private BigDecimal mintrueY;
    private BigDecimal maxtrueY;
    private Boolean isRight;

}

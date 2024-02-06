package net.stepbooks.domain.exercise.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DragImage {

    private int id;
    private String img;
    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal oldX;
    private BigDecimal oldY;
    private BigDecimal trueX;
    private BigDecimal trueY;
    private BigDecimal mintrueX;
    private BigDecimal maxtrueX;
    private BigDecimal mintrueY;
    private BigDecimal maxtrueY;

    private BigDecimal mintrueX1;
    private BigDecimal maxtrueX1;

    private Boolean isRight;

}

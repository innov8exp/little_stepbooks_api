package net.stepbooks.domain.exercise.entity;

import lombok.Data;

@Data
public class DragImage {

    private int id;
    private String img;

    //假设终点图片是旋转的，那么需要设置trueImg，当拖拽到目的地的时候，原始图片消失，改用本图片绘制
//    private String trueImg;
//    private Integer trueWidth;
//    private Integer trueHeight;

    //拖拽图片的大小(px)
    private int width;
    private int height;
    //启始位置，
    private int posX;
    private int posY;
//    private int oldX;
//    private int oldY;
    //终点位置
    private Integer trueX;
    private Integer trueY;

    //如果isMulti=true，表示可以被复用，点击移动的时候原来的图标不消失
//    private Boolean isMulti;

    //多个终点位置（仅仅在isMulti=true时生效）
//    private List<Integer> multiX;
//    private List<Integer> multiY;

    //按照拖拽图标的50%误差作为磁吸范围，不需要额外定义
    //比如小图标是100X100像素，那么上下左右50像素范围内都认为OK
//    private int mintrueX;
//    private int maxtrueX;
//    private int mintrueY;
//    private int maxtrueY;
//
//    private int mintrueX1;
//    private int maxtrueX1;
    //统一以左上角坐标作为锚点，isRight总是false，删除
//    private Boolean isRight;
}

package net.stepbooks.domain.exercise.entity;

import lombok.Data;

@Data
public class DragImage {

    private String url;
    /**
     * 图片自身宽度
     */
    private int width;
    /**
     * 图片自身高度
     */
    private int height;

    //图片放置位置
    private int startX;
    private int startY;

    //答案位置
    private int endX;
    private int endY;
}

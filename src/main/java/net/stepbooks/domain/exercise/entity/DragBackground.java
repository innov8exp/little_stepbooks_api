package net.stepbooks.domain.exercise.entity;

import lombok.Data;

/**
 * 拖拽型练习的背景图
 */
@Data
public class DragBackground {


    private String url;

    /**
     * 图片自身宽度
     */
    private int width;

    /**
     * 图片自身高度
     */
    private int height;

}

package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 拖拽型练习。
 * <p>
 * 1）背景图的左下角坐标为x=0,y=0
 * 2) 答案图的startX,startY,endX,endY为左下角坐标
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DragExercise extends AbstractExercise {

    /**
     * 背景图
     */
    private DragBackground dragBackground;

    /**
     * 答案图
     */
    private List<DragImage> dragImages;

    public DragExercise() {
        this.setType(ExerciseType.DRAG);
    }

    public static void main(String[] args) {
        DragExercise dragExercise = new DragExercise();
        dragExercise.setPoint(20);
        DragBackground dragBackground = new DragBackground();
        dragBackground.setUrl("https://xxx.xxx.xxx/bg.jpg");
        dragBackground.setWidth(1000);
        dragBackground.setHeight(600);
        dragExercise.setDragBackground(dragBackground);
        List<DragImage> dragImages = new ArrayList<>();
        DragImage dragImage = new DragImage();
        dragImage.setUrl("https://xxx.xxx.xxx/drag1.jpg");
        dragImage.setWidth(20);
        dragImage.setHeight(20);
        dragImage.setStartX(100);
        dragImage.setStartY(100);
        dragImage.setEndX(300);
        dragImage.setEndY(400);
        dragImages.add(dragImage);

        dragImage = new DragImage();
        dragImage.setUrl("https://xxx.xxx.xxx/drag2.jpg");
        dragImage.setWidth(20);
        dragImage.setHeight(20);
        dragImage.setStartX(150);
        dragImage.setStartY(100);
        dragImage.setEndX(350);
        dragImage.setEndY(400);
        dragImages.add(dragImage);

        dragExercise.setDragImages(dragImages);

        System.out.println(JsonUtils.toJson(dragExercise));
    }
}

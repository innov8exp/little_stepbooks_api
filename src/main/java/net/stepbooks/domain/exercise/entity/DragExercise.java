package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.util.JsonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 拖拽型练习。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DragExercise extends AbstractExercise {

    /**
     * 答案图
     */
    private List<DragImage>  dataList;

    public DragExercise() {
        this.setType(ExerciseType.DRAG);
    }

    public static void main(String[] args) {
        DragExercise dragExercise = new DragExercise();
        dragExercise.setPoint(20);
        List<DragImage> dragImages = new ArrayList<>();
        DragImage dragImage = new DragImage();
        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/book/d9ec4509-cb7a-46a9-86ad-0b921542369a.png");
        dragImage.setId(0);
        dragImage.setPosX(BigDecimal.valueOf(469.53));
        dragImage.setPosY(BigDecimal.valueOf(257.75));
        dragImage.setMintrueX(BigDecimal.valueOf(310));
        dragImage.setMaxtrueX(BigDecimal.valueOf(345));
        dragImage.setMintrueY(BigDecimal.valueOf(101));
        dragImage.setMaxtrueY(BigDecimal.valueOf(136));
        dragImage.setIsRight(false);
        dragImages.add(dragImage);


        dragImage = new DragImage();
        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/book/35842eab-78cc-4447-a619-5718b740bc0a.png");
        dragImage.setId(1);
        dragImage.setPosX(BigDecimal.valueOf(420.73));
        dragImage.setPosY(BigDecimal.valueOf(257.75));
        dragImage.setMintrueX(BigDecimal.valueOf(387));
        dragImage.setMaxtrueX(BigDecimal.valueOf(422));
        dragImage.setMintrueY(BigDecimal.valueOf(33));
        dragImage.setMaxtrueY(BigDecimal.valueOf(68));
        dragImage.setIsRight(false);
        dragImages.add(dragImage);

        dragImage = new DragImage();
        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/book/dba2b520-8f69-411e-ab72-41aa77dd036d.png");
        dragImage.setId(2);
        dragImage.setPosX(BigDecimal.valueOf(371.13));
        dragImage.setPosY(BigDecimal.valueOf(257.75));
        dragImage.setMintrueX(BigDecimal.valueOf(451));
        dragImage.setMaxtrueX(BigDecimal.valueOf(486));
        dragImage.setMintrueY(BigDecimal.valueOf(66));
        dragImage.setMaxtrueY(BigDecimal.valueOf(101));
        dragImage.setIsRight(false);
        dragImages.add(dragImage);

        dragImage = new DragImage();
        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/book/e72311b3-23e1-485b-83a0-ff580cd18548.png");
        dragImage.setId(3);
        dragImage.setPosX(BigDecimal.valueOf(322.74));
        dragImage.setPosY(BigDecimal.valueOf(257.75));
        dragImage.setMintrueX(BigDecimal.valueOf(499));
        dragImage.setMaxtrueX(BigDecimal.valueOf(534));
        dragImage.setMintrueY(BigDecimal.valueOf(129));
        dragImage.setMaxtrueY(BigDecimal.valueOf(164));
        dragImage.setIsRight(false);
        dragImages.add(dragImage);


        dragExercise.setDataList(dragImages);

        System.out.println(JsonUtils.toJson(dragExercise));
    }
}

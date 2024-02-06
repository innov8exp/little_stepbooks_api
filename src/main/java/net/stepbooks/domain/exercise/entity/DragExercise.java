package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;
//import net.stepbooks.infrastructure.util.JsonUtils;

//import java.math.BigDecimal;
//import java.util.ArrayList;
import java.util.List;

/**
 * 拖拽型练习。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DragExercise extends AbstractExercise {

    private String imgId;
    private String imgUrl;
    private String audioId;
    private String audioUrl;

    /**
     * 答案图
     */
    private List<DragImage> dataList;

    public DragExercise() {
        this.setType(ExerciseType.DRAG);
    }

//    private static void mock1() {
//        DragExercise dragExercise = new DragExercise();
//        dragExercise.setPoint(10);
//        dragExercise.setImgUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/f1ec2369-7db6-440b-bbbe-d3895b3c11fa.png");
//        dragExercise.setAudioUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/12c7c6cf-872f-4b79-854f-5da293dbfe06.mp3");
//
//        List<DragImage> dataList = new ArrayList<>();
//        DragImage dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/475080c5-d3e8-4f03-bb1c-cff727b8f22f.png");
//        dragImage.setId(0);
//        dragImage.setPosX(BigDecimal.valueOf(279));
//        dragImage.setPosY(BigDecimal.valueOf(245));
//
//        dragImage.setOldX(BigDecimal.valueOf(279));
//        dragImage.setOldY(BigDecimal.valueOf(245));
//
//        dragImage.setTrueX(BigDecimal.valueOf(428));
//        dragImage.setTrueY(BigDecimal.valueOf(130));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(401));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(451));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(115));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(165));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/48305e47-7a59-40c4-9721-ff9b5dab29a9.png");
//        dragImage.setId(1);
//        dragImage.setPosX(BigDecimal.valueOf(379));
//        dragImage.setPosY(BigDecimal.valueOf(245));
//
//        dragImage.setOldX(BigDecimal.valueOf(379));
//        dragImage.setOldY(BigDecimal.valueOf(245));
//
//        dragImage.setTrueX(BigDecimal.valueOf(318));
//        dragImage.setTrueY(BigDecimal.valueOf(130));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(283));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(333));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(115));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(165));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/466d810a-ce51-4076-8c27-0795fdfba50d.png");
//        dragImage.setId(2);
//        dragImage.setPosX(BigDecimal.valueOf(479));
//        dragImage.setPosY(BigDecimal.valueOf(245));
//
//        dragImage.setOldX(BigDecimal.valueOf(479));
//        dragImage.setOldY(BigDecimal.valueOf(245));
//
//        dragImage.setTrueX(BigDecimal.valueOf(531));
//        dragImage.setTrueY(BigDecimal.valueOf(130));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(501));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(551));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(115));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(165));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragExercise.setDataList(dataList);
//
//        System.out.println(JsonUtils.toJson(dragExercise));
//    }
//
//    private static void mock2() {
//        DragExercise dragExercise = new DragExercise();
//        dragExercise.setPoint(10);
//        dragExercise.setImgUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/19b51dd9-9f52-4dda-86e3-366fc16a5d3d.png");
//        dragExercise.setAudioUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/1e7dafbb-eac2-4ae0-8eb5-d81b5321c375.mp3");
//
//        List<DragImage> dataList = new ArrayList<>();
//        DragImage dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/d9ec4509-cb7a-46a9-86ad-0b921542369a.png");
//        dragImage.setId(0);
//        dragImage.setPosX(BigDecimal.valueOf(469.53));
//        dragImage.setPosY(BigDecimal.valueOf(257.75));
//
//        dragImage.setOldX(BigDecimal.valueOf(469.53));
//        dragImage.setOldY(BigDecimal.valueOf(257.75));
//
//        dragImage.setTrueX(BigDecimal.valueOf(324));
//        dragImage.setTrueY(BigDecimal.valueOf(126));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(310));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(345));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(101));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(136));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/35842eab-78cc-4447-a619-5718b740bc0a.png");
//        dragImage.setId(1);
//        dragImage.setPosX(BigDecimal.valueOf(420.73));
//        dragImage.setPosY(BigDecimal.valueOf(257.75));
//
//        dragImage.setOldX(BigDecimal.valueOf(420.73));
//        dragImage.setOldY(BigDecimal.valueOf(257.75));
//
//        dragImage.setTrueX(BigDecimal.valueOf(405));
//        dragImage.setTrueY(BigDecimal.valueOf(55));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(387));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(422));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(33));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(68));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/dba2b520-8f69-411e-ab72-41aa77dd036d.png");
//        dragImage.setId(2);
//        dragImage.setPosX(BigDecimal.valueOf(371.13));
//        dragImage.setPosY(BigDecimal.valueOf(257.75));
//
//        dragImage.setOldX(BigDecimal.valueOf(371.13));
//        dragImage.setOldY(BigDecimal.valueOf(257.75));
//
//        dragImage.setTrueX(BigDecimal.valueOf(472));
//        dragImage.setTrueY(BigDecimal.valueOf(86));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(451));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(486));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(66));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(101));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/e72311b3-23e1-485b-83a0-ff580cd18548.png");
//        dragImage.setId(3);
//        dragImage.setPosX(BigDecimal.valueOf(322.74));
//        dragImage.setPosY(BigDecimal.valueOf(257.75));
//
//        dragImage.setOldX(BigDecimal.valueOf(322.74));
//        dragImage.setOldY(BigDecimal.valueOf(257.75));
//
//        dragImage.setTrueX(BigDecimal.valueOf(510));
//        dragImage.setTrueY(BigDecimal.valueOf(158));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(499));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(534));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(129));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(174));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragExercise.setDataList(dataList);
//
//        System.out.println(JsonUtils.toJson(dragExercise));
//    }
//
//    private static void mock3() {
//        DragExercise dragExercise = new DragExercise();
//        dragExercise.setPoint(10);
//        dragExercise.setImgUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/b48e1d5c-802b-40f2-9437-6226625f93e0.png");
//        dragExercise.setAudioUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/0a261e20-c69b-4325-9e9e-c253e7dfc168.mp3");
//
//        List<DragImage> dataList = new ArrayList<>();
//        DragImage dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/a95d0976-d414-40b3-8519-363c13d873c8.png");
//        dragImage.setId(0);
//        dragImage.setPosX(BigDecimal.valueOf(280));
//        dragImage.setPosY(BigDecimal.valueOf(191));
//
//        dragImage.setOldX(BigDecimal.valueOf(280));
//        dragImage.setOldY(BigDecimal.valueOf(191));
//
//        dragImage.setTrueX(BigDecimal.valueOf(405));
//        dragImage.setTrueY(BigDecimal.valueOf(235));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(368));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(414));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(220));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(256));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/c2cd9490-a2dd-424a-bc52-d92c75f29e76.png");
//        dragImage.setId(1);
//        dragImage.setPosX(BigDecimal.valueOf(334));
//        dragImage.setPosY(BigDecimal.valueOf(191));
//
//        dragImage.setOldX(BigDecimal.valueOf(334));
//        dragImage.setOldY(BigDecimal.valueOf(191));
//
//        dragImage.setTrueX(BigDecimal.valueOf(326));
//        dragImage.setTrueY(BigDecimal.valueOf(235));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(296));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(350));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(220));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(256));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/c058f3b4-38d4-42cc-8a15-aa7132522884.png");
//        dragImage.setId(2);
//        dragImage.setPosX(BigDecimal.valueOf(388));
//        dragImage.setPosY(BigDecimal.valueOf(191));
//
//        dragImage.setOldX(BigDecimal.valueOf(388));
//        dragImage.setOldY(BigDecimal.valueOf(191));
//
//        dragImage.setTrueX(BigDecimal.valueOf(490));
//        dragImage.setTrueY(BigDecimal.valueOf(235));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(476));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(512));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(220));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(256));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/9ae07941-a6da-4177-9951-9d8d5695f61b.png");
//        dragImage.setId(3);
//        dragImage.setPosX(BigDecimal.valueOf(442));
//        dragImage.setPosY(BigDecimal.valueOf(191));
//
//        dragImage.setOldX(BigDecimal.valueOf(442));
//        dragImage.setOldY(BigDecimal.valueOf(191));
//
//        dragImage.setTrueX(BigDecimal.valueOf(440));
//        dragImage.setTrueY(BigDecimal.valueOf(235));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(422));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(458));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(220));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(256));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/ee7a47f4-c82a-40e1-965d-ab204d26286e.png");
//        dragImage.setId(4);
//        dragImage.setPosX(BigDecimal.valueOf(496));
//        dragImage.setPosY(BigDecimal.valueOf(191));
//
//        dragImage.setOldX(BigDecimal.valueOf(496));
//        dragImage.setOldY(BigDecimal.valueOf(191));
//
//        dragImage.setTrueX(BigDecimal.valueOf(373));
//        dragImage.setTrueY(BigDecimal.valueOf(235));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(360));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(396));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(220));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(256));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragExercise.setDataList(dataList);
//
//        System.out.println(JsonUtils.toJson(dragExercise));
//    }
//
//
//    private static void mock4() {
//        DragExercise dragExercise = new DragExercise();
//        dragExercise.setPoint(10);
//        dragExercise.setImgUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/4d465af1-19f3-464a-a938-3ffcaaf1479f.png");
//        dragExercise.setAudioUrl("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/9cbc43aa-e697-42bf-8b91-b94f373870a2.mp3");
//
//        List<DragImage> dataList = new ArrayList<>();
//        DragImage dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/6dfa6db9-e306-4693-8a52-72b129fefbea.png");
//        dragImage.setId(0);
//        dragImage.setPosX(BigDecimal.valueOf(286));
//        dragImage.setPosY(BigDecimal.valueOf(107));
//
//        dragImage.setOldX(BigDecimal.valueOf(286));
//        dragImage.setOldY(BigDecimal.valueOf(107));
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/2e22611f-a680-4e01-a6be-cc74dcb67b03.png");
//        dragImage.setId(1);
//        dragImage.setPosX(BigDecimal.valueOf(327));
//        dragImage.setPosY(BigDecimal.valueOf(66));
//
//        dragImage.setOldX(BigDecimal.valueOf(327));
//        dragImage.setOldY(BigDecimal.valueOf(66));
//
//        dragImage.setTrueX(BigDecimal.valueOf(431));
//        dragImage.setTrueY(BigDecimal.valueOf(207));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(401));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(441));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(192));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(232));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/86ee8579-c382-4a46-a513-cf0e9894a921.png");
//        dragImage.setId(2);
//        dragImage.setPosX(BigDecimal.valueOf(383));
//        dragImage.setPosY(BigDecimal.valueOf(52));
//
//        dragImage.setOldX(BigDecimal.valueOf(383));
//        dragImage.setOldY(BigDecimal.valueOf(52));
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/4f127c24-9771-436d-bc99-8601b016c77f.png");
//        dragImage.setId(3);
//        dragImage.setPosX(BigDecimal.valueOf(435));
//        dragImage.setPosY(BigDecimal.valueOf(73));
//
//        dragImage.setOldX(BigDecimal.valueOf(435));
//        dragImage.setOldY(BigDecimal.valueOf(73));
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/4fef33b0-dc11-4288-b4fa-48a10505fa6a.png");
//        dragImage.setId(4);
//        dragImage.setPosX(BigDecimal.valueOf(503));
//        dragImage.setPosY(BigDecimal.valueOf(54));
//
//        dragImage.setOldX(BigDecimal.valueOf(503));
//        dragImage.setOldY(BigDecimal.valueOf(54));
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/d4c38d37-0b80-476e-a14b-14be8191c395.png");
//        dragImage.setId(5);
//        dragImage.setPosX(BigDecimal.valueOf(333));
//        dragImage.setPosY(BigDecimal.valueOf(130));
//
//        dragImage.setOldX(BigDecimal.valueOf(333));
//        dragImage.setOldY(BigDecimal.valueOf(130));
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/6ad9497d-94c1-4558-80f5-d2d7c596ef8b.png");
//        dragImage.setId(6);
//        dragImage.setPosX(BigDecimal.valueOf(378));
//        dragImage.setPosY(BigDecimal.valueOf(99));
//
//        dragImage.setOldX(BigDecimal.valueOf(378));
//        dragImage.setOldY(BigDecimal.valueOf(99));
//
//        dragImage.setTrueX(BigDecimal.valueOf(389));
//        dragImage.setTrueY(BigDecimal.valueOf(207));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(349));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(399));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(192));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(232));
//
//        dragImage.setMintrueX1(BigDecimal.valueOf(453));
//        dragImage.setMaxtrueX1(BigDecimal.valueOf(493));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/0b2d6e07-31c9-4fc1-9620-2697b59ce18b.png");
//        dragImage.setId(7);
//        dragImage.setPosX(BigDecimal.valueOf(434));
//        dragImage.setPosY(BigDecimal.valueOf(130));
//
//        dragImage.setOldX(BigDecimal.valueOf(434));
//        dragImage.setOldY(BigDecimal.valueOf(130));
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/0ba34d32-746e-4ff0-872b-9fda5c323c03.png");
//        dragImage.setId(8);
//        dragImage.setPosX(BigDecimal.valueOf(478));
//        dragImage.setPosY(BigDecimal.valueOf(108));
//
//        dragImage.setOldX(BigDecimal.valueOf(478));
//        dragImage.setOldY(BigDecimal.valueOf(108));
//
//        dragImage.setTrueX(BigDecimal.valueOf(473));
//        dragImage.setTrueY(BigDecimal.valueOf(207));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(349));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(389));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(192));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(232));
//
//        dragImage.setMintrueX1(BigDecimal.valueOf(453));
//        dragImage.setMaxtrueX1(BigDecimal.valueOf(493));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//
//        dragImage = new DragImage();
//        dragImage.setImg("https://s3.cn-north-1.amazonaws.com.cn/stepbook-public/" +
//                "book/12b0520f-d311-409d-b112-813508dbc6cb.png");
//        dragImage.setId(9);
//        dragImage.setPosX(BigDecimal.valueOf(539));
//        dragImage.setPosY(BigDecimal.valueOf(93));
//
//        dragImage.setOldX(BigDecimal.valueOf(539));
//        dragImage.setOldY(BigDecimal.valueOf(93));
//
//        dragImage.setTrueX(BigDecimal.valueOf(512));
//        dragImage.setTrueY(BigDecimal.valueOf(207));
//
//        dragImage.setMintrueX(BigDecimal.valueOf(505));
//        dragImage.setMaxtrueX(BigDecimal.valueOf(545));
//
//        dragImage.setMintrueY(BigDecimal.valueOf(192));
//        dragImage.setMaxtrueY(BigDecimal.valueOf(232));
//
//        dragImage.setIsRight(false);
//
//        dataList.add(dragImage);
//
//        dragExercise.setDataList(dataList);
//
//        System.out.println(JsonUtils.toJson(dragExercise));
//    }
//
//
//    public static void main(String[] args) {
//        mock1();
//        mock2();
//        mock3();
//        mock4();
//    }
}

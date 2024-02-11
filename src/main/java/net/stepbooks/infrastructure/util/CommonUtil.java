package net.stepbooks.infrastructure.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class CommonUtil {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    //生成随机用户名，数字和字母组成,
    @SuppressWarnings("checkstyle:magicnumber")
    public static String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    /**
     * 输入"1,2,3,4,5" + "4,5,6,7,8"，输出"1,2,3,4,5,6,7,8"
     * <p>
     * 输入"*" + "1,2,3"，输出 "*"
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String combineCommaString(String str1, String str2) {
        if ("*".equals(str1) || "*".equals(str2)) {
            return "*";
        }

        if (str1 == null) {
            return str2;
        }

        if (str2 == null) {
            return str1;
        }

        String[] arr1 = str1.split(",");
        String[] arr2 = str2.split(",");

        ArrayList<String> resultList = new ArrayList<>();

        for (String element : arr1) {
            if (!resultList.contains(element)) {
                resultList.add(element);
            }
        }

        for (String element : arr2) {
            if (!resultList.contains(element)) {
                resultList.add(element);
            }
        }

        String result = String.join(",", resultList);

        return result;
    }
}

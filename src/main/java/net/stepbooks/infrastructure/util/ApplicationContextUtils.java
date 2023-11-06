package net.stepbooks.infrastructure.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * The type Application context utils.
 */
@UtilityClass
@Slf4j
public class ApplicationContextUtils {

    /**
     * Gets bean.
     *
     * @param name the name
     * @return the bean
     */
    public static Object getBean(String name) {
        return ApplicationContextHolder.applicationContext == null
                ? null : ApplicationContextHolder.applicationContext.getBean(name);
    }

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return ApplicationContextHolder.applicationContext == null
                ? null : ApplicationContextHolder.applicationContext.getBean(clazz);
    }

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> Optional<T> getBeanOptional(Class<T> clazz) {
        return Optional.ofNullable(getBean(clazz));
    }

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
     * @param name  the name
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return ApplicationContextHolder.applicationContext == null ? null
                : ApplicationContextHolder.applicationContext.getBean(name, clazz);
    }

    /**
     * Invoke method with bean that maintained in spring container
     *
     * @param beanName
     * @param methodName
     * @param parameterMap
     */
    public static void invoke(String beanName, String methodName, Map<String, Object> parameterMap) {
        try {
            if (StringUtils.isBlank(beanName) || StringUtils.isBlank(methodName)) {
                return;
            }
            Object targetBean = ApplicationContextHolder.applicationContext.getBean(beanName);
            Arrays.stream(targetBean.getClass().getDeclaredMethods())
                    .filter(method1 -> method1.getName().equals(methodName))
                    .findFirst()
                    .ifPresent(method -> ReflectionUtils.invokeMethod(method, targetBean, parameterMap));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Component
    public static class ApplicationContextHolder implements ApplicationContextAware {

        private static ApplicationContext applicationContext;

        private static void setFactory(ApplicationContext context) {
            ApplicationContextHolder.applicationContext = context;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            setFactory(applicationContext);
        }

    }
}

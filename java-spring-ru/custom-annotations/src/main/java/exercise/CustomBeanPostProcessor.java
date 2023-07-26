package exercise;

import java.lang.reflect.Proxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// BEGIN
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    Map<String, Class> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Inspect.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                String level = beanClass.getAnnotation(Inspect.class).level();
                Logger LOGGER = LoggerFactory.getLogger(beanClass);
                if (level.equals("info")) {
                    LOGGER.info("Was called method: " + method.getName() + "() with arguments: " + Arrays.toString(args));
                    return method.invoke(bean, args);
                } else if (level.equals("debug")) {
                    LOGGER.debug("Was called method: " + method.getName() + "() with arguments: " + Arrays.toString(args));
                    return method.invoke(bean, args);
                } else {
                    throw new RuntimeException("amongus");
                }
            });
        }
        return bean;
    }
}
// END

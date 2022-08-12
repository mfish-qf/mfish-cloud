package cn.com.mfish.common.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：qiufeng
 * @description：springboot升级后出现与swaggerfox3.0不兼容问题
 * 启动时错误问题，错误信息如下： Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException问题
 * stackoverflow中提供解决方案：在nacos配置中增加下面配置，并未解决。通过重写过滤null数据
 * mvc:
 * pathmatch:
 * matching-strategy: ant_path_matcher
 * @date ：2022/8/12 17:01
 */
@Component
public class SwaggerBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
            List<RequestMappingInfoHandlerMapping> list;
            try {
                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                field.setAccessible(true);
                list = (List<RequestMappingInfoHandlerMapping>) field.get(bean);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
            List<RequestMappingInfoHandlerMapping> filter = list.stream().filter(mapping -> mapping.getPatternParser() == null)
                    .collect(Collectors.toList());
            list.clear();
            list.addAll(filter);
        }
        return bean;
    }

}

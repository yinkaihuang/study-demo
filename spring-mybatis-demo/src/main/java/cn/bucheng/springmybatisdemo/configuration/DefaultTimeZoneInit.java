package cn.bucheng.springmybatisdemo.configuration;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.*;

/**
 * @author yinchong
 * @create 2020/2/12 13:03
 * @description
 */
public class DefaultTimeZoneInit implements EnvironmentPostProcessor, Ordered {


    public static final String CONFIGURATION_PROPERTIES = "configurationProperties";
    public static final String JDBC = "jdbc";
    public static final String SERVER_TIME_ZONE = "serverTimezone=";
    public static final String ASIA_SHANGHAI = "Asia/Shanghai";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        List<ReplaceEnvMap> replaceList = new LinkedList<>();
        MutablePropertySources propertySources = environment.getPropertySources();
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        String beforeName = "";
        while (iterator.hasNext()) {
            PropertySource<?> next = iterator.next();
            String name = next.getName();
            if (name.equals(CONFIGURATION_PROPERTIES)) {
                beforeName = name;
                continue;
            }
            Object source = next.getSource();
            if (!(source instanceof Map)) {
                beforeName = name;
                continue;
            }
            Map<String, Object> sourceMap = (Map<String, Object>) source;
            Map<String, Object> replaceMap = new LinkedHashMap<>();
            if (sourceMap == null || sourceMap.size() == 0) {
                beforeName = name;
                continue;
            }
            boolean replace = false;
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof OriginTrackedValue) {
                    OriginTrackedValue temp = (OriginTrackedValue) value;
                    replaceMap.put(key, temp.getValue());
                } else {
                    replaceMap.put(key, value);
                }
                if (!key.endsWith("url")) {
                    continue;
                }
                String valueStr = String.valueOf(value);
                if (!valueStr.toLowerCase().trim().startsWith(JDBC)) {
                    continue;
                }
                if (valueStr.contains(SERVER_TIME_ZONE)) {
                    continue;
                }
                if (valueStr.contains("?")) {
                    valueStr += "&" + SERVER_TIME_ZONE + ASIA_SHANGHAI;
                } else {
                    valueStr += "?" + SERVER_TIME_ZONE + ASIA_SHANGHAI;
                }
                replace = true;
                replaceMap.put(key, valueStr);
            }
            if (replace) {
                ReplaceEnvMap replaceEnvMap = new ReplaceEnvMap();
                replaceEnvMap.beforeName = beforeName;
                replaceEnvMap.name = name;
                replaceEnvMap.replaceMap = replaceMap;
                replaceList.add(replaceEnvMap);
            }
            beforeName = name;
        }

        for (ReplaceEnvMap envMap : replaceList) {
            propertySources.remove(envMap.name);
            MapPropertySource tempSource = new MapPropertySource(envMap.name, envMap.replaceMap);
            if (Strings.isBlank(envMap.beforeName)) {
                propertySources.addFirst(tempSource);
            } else {
                propertySources.addAfter(envMap.beforeName, tempSource);
            }
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    class ReplaceEnvMap {
        private String beforeName;
        private String name;
        private Map<String, Object> replaceMap;
    }
}

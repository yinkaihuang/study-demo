package cn.bucheng.simple.learn;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yinchong
 * @create 2020/5/10 12:07
 * @description
 */
public class ConfigurationUtils {

    public static Configuration loadPropertiesFromResources(String path)
            throws IOException {
        Configuration configuration = new Configuration();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(path);
        if (!resources.hasMoreElements()) {
            throw new IOException("Cannot locate " + path + " as a classpath resource.");
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            InputStream fin = url.openStream();
            Properties props = loadPropertiesFromInputStream(fin);
            loadProperties(props, configuration);
        }
        return configuration;
    }

    public static Properties loadPropertiesFromInputStream(InputStream fin) throws IOException {
        Properties props = new Properties();
        InputStreamReader reader = new InputStreamReader(fin, "UTF-8");
        try {
            props.load(reader);
            return props;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fin != null) {
                fin.close();
            }
        }
    }


    public static void loadProperties(Properties props, Configuration config) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            config.setProperty((String) entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public static class Configuration {
        Map<String, String> properties = new HashMap<>(50);

        public void setProperty(String key, String value) {
            properties.put(key, value);
        }

        public String getProperty(String key) {
            return properties.get(key);
        }
    }
}

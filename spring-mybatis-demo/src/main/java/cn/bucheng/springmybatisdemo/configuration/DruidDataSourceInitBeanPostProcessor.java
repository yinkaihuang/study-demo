package cn.bucheng.springmybatisdemo.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DruidDataSourceInitBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DruidDataSource) {
            applyFilterToDruid((DruidDataSource) bean);
        } else if (bean instanceof FactoryBean) {
            try {
                FactoryBean factory = (FactoryBean) bean;
                Object target = factory.getObject();
                if (target instanceof DruidDataSource) {
                    applyFilterToDruid((DruidDataSource) target);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }

    private void applyFilterToDruid(DruidDataSource bean) {
        DruidDataSource druidDataSource = bean;
        try {
            druidDataSource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
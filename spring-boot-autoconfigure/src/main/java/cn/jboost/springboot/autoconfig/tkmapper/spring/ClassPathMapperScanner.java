package cn.jboost.springboot.autoconfig.tkmapper.spring;

import cn.jboost.springboot.autoconfig.tkmapper.mapper.BaseMapper;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.spring.mapper.MapperFactoryBean;

import java.util.Arrays;
import java.util.Set;

public class ClassPathMapperScanner extends org.mybatis.spring.mapper.ClassPathMapperScanner {

    private MapperHelper mapperHelper = new MapperHelper();

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        doAfterScan(beanDefinitions);
        return beanDefinitions;
    }

    protected void doAfterScan(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (StringUtil.isNotEmpty(definition.getBeanClassName())
                    && definition.getBeanClassName().equals("org.mybatis.spring.mapper.MapperFactoryBean")) {
                definition.setBeanClass(MapperFactoryBean.class);
                definition.getPropertyValues().add("mapperHelper", this.mapperHelper);
            }
        }
    }

    /**
     * 从环境变量中获取 mapper 配置信息
     * 如果没有配置，则作默认处理
     * @param environment
     */
    public void setMapperProperties(Environment environment) {
        Config config = SpringBootBindUtil.bind(environment, Config.class, Config.PREFIX);
        if (config == null) {
            config = new Config();
        }
        //默认处理非简单类型，即List/Map等这种复杂类型属性都会与数据库映射，如果不需要映射，加@javax.persistence.Transient注解
        config.setUseSimpleType(false);
        config.setEnumAsSimpleType(true);

        //如果没有配置mappers，则默认注册BaseMapper
        if (config.getMappers() == null || config.getMappers().isEmpty()) {
            config.setMappers(Arrays.asList(BaseMapper.class));
        }
        mapperHelper.setConfig(config);
    }
}
package com.yuntao.zhushou.dal.config;

import com.yuntao.zhushou.dal.mapper.*;
import com.yuntao.zhushou.dal.mybatis.IdTypeHandler;
import com.yuntao.zhushou.model.domain.AuthRes;
import com.yuntao.zhushou.model.domain.Role;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

/**
 * 等价于spring-dao.xml
 */
@Configuration
@ImportResource("classpath:applicationContext-dal.xml")
public class DalConfig {

    @Value("classpath:mybatis-config.xml")
    Resource mybatisMapperConfig;

    @Autowired
    DataSource dataSource;

    @Bean
    public ConfigMapper configMapper() throws Exception {
        return newMapperFactoryBean(ConfigMapper.class).getObject();
    }

    @Bean
    public UserMapper userMapper() throws Exception {
        return newMapperFactoryBean(UserMapper.class).getObject();
    }

    @Bean
    public AppMapper appMapper() throws Exception {
        return newMapperFactoryBean(AppMapper.class).getObject();
    }


    @Bean
    public HostMapper hostMapper() throws Exception {
        return newMapperFactoryBean(HostMapper.class).getObject();
    }


    @Bean
    public DeployLogMapper deployLogMapper() throws Exception {
        return newMapperFactoryBean(DeployLogMapper.class).getObject();
    }

    @Bean
    public AtTemplateMapper atTemplateMapper() throws Exception {
        return newMapperFactoryBean(AtTemplateMapper.class).getObject();
    }

    @Bean
    public AtActiveMapper atActiveMapper() throws Exception {
        return newMapperFactoryBean(AtActiveMapper.class).getObject();
    }

    @Bean
    public AtParameterMapper atParameterMapper() throws Exception {
        return newMapperFactoryBean(AtParameterMapper.class).getObject();
    }
    @Bean
    public AtProcessInstMapper atProcessInstMapper() throws Exception {
        return newMapperFactoryBean(AtProcessInstMapper.class).getObject();
    }

    @Bean
    public AtActiveInstMapper atActiveInstMapper() throws Exception {
        return newMapperFactoryBean(AtActiveInstMapper.class).getObject();
    }

    @Bean
    public IdocUrlMapper idocUrlMapper() throws Exception {
        return newMapperFactoryBean(IdocUrlMapper.class).getObject();
    }

    @Bean
    public IdocParamMapper idocParamMapper() throws Exception {
        return newMapperFactoryBean(IdocParamMapper.class).getObject();
    }

    @Bean
    public RoleMapper roleMapper() throws Exception {
        return newMapperFactoryBean(RoleMapper.class).getObject();
    }


    @Bean
    public AuthResMapper authResMapper() throws Exception {
        return newMapperFactoryBean(AuthResMapper.class).getObject();
    }

    @Bean
    public RoleAuthResMapper roleAuthResMapper() throws Exception {
        return newMapperFactoryBean(RoleAuthResMapper.class).getObject();
    }

    @Bean
    public ReqContentMapper reqContentMapper() throws Exception {
        return newMapperFactoryBean(ReqContentMapper.class).getObject();
    }

    @Bean
    public ProxyContentMapper proxyContentMapper() throws Exception {
        return newMapperFactoryBean(ProxyContentMapper.class).getObject();
    }


    <T> MapperFactoryBean<T> newMapperFactoryBean(Class<T> clazz)
            throws Exception {
        MapperFactoryBean<T> b = new MapperFactoryBean<T>();
        b.setMapperInterface(clazz);
        b.setSqlSessionFactory(sqlSessionFactory());
        return b;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setConfigLocation(mybatisMapperConfig);
        fb.setDataSource(dataSource);
        fb.setTypeAliases(new Class<?>[]{IdTypeHandler.class});
        return fb.getObject();
    }
}

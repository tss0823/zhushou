package com.yuntao.zhushou.dal.config;

import com.yuntao.zhushou.dal.mapper.*;
import com.yuntao.zhushou.dal.mybatis.IdTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class DalConfig implements ApplicationContextAware {

    @Value("classpath:mybatis-config.xml")
    Resource mybatisMapperConfig;

    @Autowired
    DataSource dataSource;

    ApplicationContext applicationContext;

    @Bean
    public ConfigMapper configMapper() throws Exception {
        return newMapperFactoryBean(ConfigMapper.class).getObject();
    }

    @Bean
    public CompanyMapper companyMapper() throws Exception {
        return newMapperFactoryBean(CompanyMapper.class).getObject();
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
    public AppFrontMapper appFrontMapper() throws Exception {
        return newMapperFactoryBean(AppFrontMapper.class).getObject();
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

    @Bean
    public ShowResMapper showResMapper() throws Exception {
        return newMapperFactoryBean(ShowResMapper.class).getObject();
    }

    @Bean
    public AppVersionMapper appVersionMapper() throws Exception {
        return newMapperFactoryBean(AppVersionMapper.class).getObject();
    }

    @Bean
    public AppDownloadRecordsMapper appDownloadRecordsMapper() throws Exception {
        return newMapperFactoryBean(AppDownloadRecordsMapper.class).getObject();
    }

    @Bean
    public WarnEventMapper warnEventMapper() throws Exception {
        return newMapperFactoryBean(WarnEventMapper.class).getObject();
    }

    @Bean
    public WarnEventResultMapper warnEventResultMapper() throws Exception {
        return newMapperFactoryBean(WarnEventResultMapper.class).getObject();
    }

    @Bean
    public ProxyReqFilterMapper proxyReqFilterMapper() throws Exception {
        return newMapperFactoryBean(ProxyReqFilterMapper.class).getObject();
    }

    @Bean
    public ProxyReqFilterItemMapper proxyReqFilterItemMapper() throws Exception {
        return newMapperFactoryBean(ProxyReqFilterItemMapper.class).getObject();
    }

    @Bean
    public ProxyResRewriteMapper proxyResRewriteMapper() throws Exception {
        return newMapperFactoryBean(ProxyResRewriteMapper.class).getObject();
    }

    @Bean
    public AtVariableMapper atVariableMapper() throws Exception {
        return newMapperFactoryBean(AtVariableMapper.class).getObject();
    }

    @Bean
    public MarkMapper markMapper() throws Exception {
        return newMapperFactoryBean(MarkMapper.class).getObject();
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
//        fb.setPlugins(new Interceptor[] { new MyBatisInterceptor(this.applicationContext) });
        return fb.getObject();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }
}

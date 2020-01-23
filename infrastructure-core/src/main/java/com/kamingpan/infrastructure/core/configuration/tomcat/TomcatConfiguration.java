package com.kamingpan.infrastructure.core.configuration.tomcat;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * tomcat服务器配置
 *
 * @author kamingpan
 * @since 2018-12-24
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SystemProperties.class})
public class TomcatConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 定制重写tomcat服务器配置
     *
     * @param factory Servlet Web 服务器工厂
     */
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        // 如果不用重定向到https 或者 不能强制转换为tomcat服务器工厂，则直接结束
        if (!this.systemProperties.isRedirectToHttps() || !(factory instanceof TomcatServletWebServerFactory)) {
            return;
        }

        // 获取并强转tomcat服务器工厂
        TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) factory;

        // 设置定制化对象
        tomcatServletWebServerFactory.addConnectorCustomizers(new TomcatConfiguration.TheTomcatConnectorCustomizer());

        // 增加远程IP阀门
        // 相当于增加tomcat server.xml配置文件的 Service -> Engine -> Host -> Valve 节点
        RemoteIpValve remoteIpValve = new RemoteIpValve();
        remoteIpValve.setRemoteIpHeader("X-Forwarded-For");
        remoteIpValve.setProtocolHeader("X-Forwarded-Proto");
        remoteIpValve.setProtocolHeaderHttpsValue(FinalConstant.Scheme.HTTPS);
        remoteIpValve.setHttpServerPort(FinalConstant.DefaultPort.HTTPS);
        tomcatServletWebServerFactory.addEngineValves(remoteIpValve);

        log.info("配置tomcat服务器成功");
    }

    /**
     * tomcat connector 定制
     */
    private static class TheTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

        /**
         * 重写定制tomcat配置
         * 相当于更改tomcat server.xml配置文件的 Service -> Connector 节点
         *
         * @param connector 连接器
         */
        @Override
        public void customize(Connector connector) {
            connector.setScheme(FinalConstant.Scheme.HTTPS);
            connector.setSecure(false);
            connector.setRedirectPort(FinalConstant.DefaultPort.HTTPS);
            connector.setProxyPort(FinalConstant.DefaultPort.HTTPS);
        }
    }

}

package org.isd.tpgrpcservicehotelreservationclient.config;

import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public GlobalClientInterceptorConfigurer globalInterceptorConfigurerAdapter() {
        return registry -> registry.add(new MetadataClientInterceptor());
    }
}
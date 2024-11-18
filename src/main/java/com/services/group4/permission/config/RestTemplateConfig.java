// RestTemplateConfig.java
package com.services.group4.permission.config;

import com.services.group4.permission.Auth0Interceptor;
import com.services.group4.permission.CorrelationIdInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(Auth0Interceptor auth0Interceptor) {
    RestTemplate restTemplate = new RestTemplate();
    CorrelationIdInterceptor correlationIdInterceptor = new CorrelationIdInterceptor();
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(auth0Interceptor);
    interceptors.add(correlationIdInterceptor);
    restTemplate.setInterceptors(interceptors);

    return new RestTemplate();
  }
}

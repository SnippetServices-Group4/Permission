package com.services.group4.permission;

import static com.services.group4.permission.CorrelationIdFilter.CORRELATION_ID_HEADER;
import static com.services.group4.permission.CorrelationIdFilter.CORRELATION_ID_KEY;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(CorrelationIdInterceptor.class);

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    String correlationId = MDC.get(CORRELATION_ID_KEY);
    if (correlationId != null) {
      logger.info("(PermissionService) Adding Correlation ID to request: {}", correlationId);
      request.getHeaders().set(CORRELATION_ID_HEADER, correlationId);
    } else {
      logger.warn("(PermissionService) No Correlation ID found in MDC, not adding to request.");
    }
    return execution.execute(request, body);
  }
}

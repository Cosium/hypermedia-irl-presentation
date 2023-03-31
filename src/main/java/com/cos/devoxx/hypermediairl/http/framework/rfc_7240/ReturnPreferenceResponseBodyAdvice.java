package com.cos.devoxx.hypermediairl.http.framework.rfc_7240;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * @author Réda Housni Alaoui
 */
@ControllerAdvice
class ReturnPreferenceResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  private static final String PREFERENCE_APPLIED_PREFIX = "return=";

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {

    ReturnPreference returnPreference = ReturnPreferences.parse(request);
    if (returnPreference == ReturnPreference.UNDEFINED) {
      return body;
    }
    HttpHeaders responseHeaders = response.getHeaders();

    boolean headerAlreadyPresentInResponse =
        ofNullable(responseHeaders.get(Rfc7240Headers.PREFERENCE_APPLIED))
            .orElse(Collections.emptyList())
            .stream()
            .filter(Objects::nonNull)
            .anyMatch(value -> value.startsWith(PREFERENCE_APPLIED_PREFIX));
    if (headerAlreadyPresentInResponse) {
      return body;
    }

    responseHeaders.add(
        Rfc7240Headers.PREFERENCE_APPLIED,
        PREFERENCE_APPLIED_PREFIX + returnPreference.name().toLowerCase());
    return body;
  }
}

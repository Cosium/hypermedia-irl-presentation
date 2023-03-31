package com.cos.devoxx.hypermediairl.http.framework.rfc_7240;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Réda Housni Alaoui
 */
@Configuration
class Rfc7240Configuration implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean<VaryPreferFilter> rfc7240Filter(
      DispatcherServletRegistrationBean dispatcherServletRegistration) {
    FilterRegistrationBean<VaryPreferFilter> registration =
        new FilterRegistrationBean<>(new VaryPreferFilter());
    registration.addUrlPatterns(dispatcherServletRegistration.getServletUrlMapping());
    return registration;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new ReturnPreferenceMethodArgumentResolver());
  }
}

package com.cos.devoxx.hypermediairl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaMappingInformation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class HypermediaIrlApplication {

  public static void main(String[] args) {
    SpringApplication.run(HypermediaIrlApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("**")
            .exposedHeaders(HttpHeaders.LOCATION)
            .allowedMethods(
                Stream.of(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new));
        registry
            .addMapping("/items/**")
            .exposedHeaders(HttpHeaders.LOCATION)
            .allowedMethods(
                Stream.of(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new));
        registry
            .addMapping("/spectacles/**")
            .exposedHeaders(HttpHeaders.LOCATION)
            .allowedMethods(
                Stream.of(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new));
        registry
            .addMapping("/invoices/**")
            .exposedHeaders(HttpHeaders.LOCATION)
            .allowedMethods(
                Stream.of(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new));
      }
    };
  }

  @Bean
  public LinkRelationProvider linkRelationProvider() {
    return new FixedResourceNameRelationProvider();
  }

  @Configuration
  class HateoasConfiguration implements HypermediaMappingInformation, WebMvcConfigurer {

    /**
     * As explained in <a
     * href="https://github.com/spring-projects/spring-hateoas/issues/1541">https://github.com/spring-projects/spring-hateoas/issues/1541</a>,
     * a change removing application/json support for Spring HATEOAS was introduced between 1.1.0
     * and 1.3.1.
     *
     * <p>As per <a
     * href="https://github.com/spring-projects/spring-hateoas/issues/1541#issuecomment-857637821">https://github.com/spring-projects/spring-hateoas/issues/1541#issuecomment-857637821</a>,
     * this configuration allows maintaining Spring HATEOAS models (CollectionModel & cie) bare JSON
     * serialization behaviour.
     */
    @Override
    public List<MediaType> getMediaTypes() {
      return Collections.singletonList(MediaType.APPLICATION_JSON);
    }
  }
}

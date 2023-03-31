package com.cos.devoxx.hypermediairl.http.framework;

import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/void")
public abstract class VoidAffordance {

  public static Affordance create() {
    return WebMvcLinkBuilder.afford(methodOn(VoidAffordance.class).post());
  }

  @PostMapping
  public ResponseEntity<?> post() {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}

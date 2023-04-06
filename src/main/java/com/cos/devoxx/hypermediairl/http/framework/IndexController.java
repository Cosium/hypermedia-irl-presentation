package com.cos.devoxx.hypermediairl.http.framework;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.cos.devoxx.hypermediairl.http.item.ItemController;
import com.cos.devoxx.hypermediairl.http.spectacles.SpectaclesController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

  @Controller
  @RequestMapping("/")
  public class IndexController {

    private final WebMvcLinkBuilderFactory linkBuilders;

    public IndexController(WebMvcLinkBuilderFactory linkBuilders) {
      this.linkBuilders = requireNonNull(linkBuilders);
    }

    @GetMapping
    public ResponseEntity<RepresentationModel<?>> get() {
      List<Link> links = new ArrayList<>();
      links.add(linkBuilders.linkTo(methodOn(ItemController.class).list(null)).withRel("items"));
      links.add(
          linkBuilders.linkTo(methodOn(SpectaclesController.class).list(null)).withRel("spectacles"));
      return ResponseEntity.ok(new RepresentationModel<>(links));
    }
  }

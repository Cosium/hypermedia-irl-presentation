package com.cos.devoxx.hypermediairl.http.item;

import com.cos.devoxx.hypermediairl.core.item.Item;
import com.cos.devoxx.hypermediairl.core.item.ItemRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemController {
  private final ItemRepository itemRepository;
  private final WebMvcLinkBuilderFactory linkBuilders;

  public ItemController(ItemRepository itemRepository, WebMvcLinkBuilderFactory linkBuilders) {
    this.itemRepository = requireNonNull(itemRepository);
    this.linkBuilders = requireNonNull(linkBuilders);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    return ResponseEntity.of(
        itemRepository.findById(id).map(Representation::new).map(Representation::toEntityModel));
  }

  private class Representation {
    private final long id;
    private final Double price;
    private final String label;

    private Representation(Item item) {
      this.id = item.id();
      this.price = item.price();
      this.label = item.label();
    }

    @JsonProperty
    public Double price() {
      return price;
    }

    @JsonProperty
    public String label() {
      return label;
    }

    private EntityModel<Representation> toEntityModel() {
      return EntityModel.of(
          this, linkBuilders.linkTo(methodOn(ItemController.class).findById(id)).withSelfRel());
    }
  }
}

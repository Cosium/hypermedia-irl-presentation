package com.cos.devoxx.hypermediairl.http.item;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.cos.devoxx.hypermediairl.core.item.Item;
import com.cos.devoxx.hypermediairl.core.item.ItemRepository;
import com.cos.devoxx.hypermediairl.http.framework.VoidAffordance;
import com.cos.devoxx.hypermediairl.http.framework.rfc_7240.ReturnPreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
  private final ItemRepository itemRepository;
  private final WebMvcLinkBuilderFactory linkBuilders;

  public ItemController(ItemRepository itemRepository, WebMvcLinkBuilderFactory linkBuilders) {
    this.itemRepository = requireNonNull(itemRepository);
    this.linkBuilders = requireNonNull(linkBuilders);
    this.initializeDemoItems();
  }

  private void initializeDemoItems() {
    this.itemRepository.save(new Item(120.50, "Ray-Ban Devoxx"));
    this.itemRepository.save(new Item(220.75, "Verre droit Devoxx"));
    this.itemRepository.save(new Item(220.75, "Verre gauche Devoxx"));
  }

  @GetMapping
  public ResponseEntity<?> list(ReturnPreference returnPreference) {
    Link selfLink =
        linkBuilders
            .linkTo(methodOn(ItemController.class).list(null))
            .withSelfRel()
            .andAffordances(
                List.of(
                    VoidAffordance.create(), afford(methodOn(ItemController.class).create(null))));

    if (returnPreference == ReturnPreference.MINIMAL) {
      return ResponseEntity.ok(new RepresentationModel<>(selfLink));
    }

    List<EntityModel<Representation>> representations =
        StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .map(Representation::new)
            .map(Representation::toEntityModel)
            .toList();
    return ResponseEntity.ok(CollectionModel.of(representations, selfLink));
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody EditCommand command) {
    Item item = itemRepository.save(new Item(command.price(), command.label()));
    return ResponseEntity.created(
            linkBuilders.linkTo(methodOn(ItemController.class).findById(item.id())).toUri())
        .build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    return ResponseEntity.of(
        itemRepository.findById(id).map(Representation::new).map(Representation::toEntityModel));
  }

  private record EditCommand(
      @JsonProperty("label") String label, @JsonProperty("price") Double price) {}

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

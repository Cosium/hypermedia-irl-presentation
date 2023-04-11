package com.cos.devoxx.hypermediairl.http.spectacles;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.cos.devoxx.hypermediairl.core.invoice.Invoice;
import com.cos.devoxx.hypermediairl.core.invoice.InvoiceRepository;
import com.cos.devoxx.hypermediairl.core.item.Item;
import com.cos.devoxx.hypermediairl.core.item.ItemRepository;
import com.cos.devoxx.hypermediairl.core.spectacles.Spectacles;
import com.cos.devoxx.hypermediairl.core.spectacles.SpectaclesRepository;
import com.cos.devoxx.hypermediairl.http.framework.VoidAffordance;
import com.cos.devoxx.hypermediairl.http.framework.rfc_7240.ReturnPreference;
import com.cos.devoxx.hypermediairl.http.invoice.InvoiceController;
import com.cos.devoxx.hypermediairl.http.item.ItemController;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocks.spiffy.spring.hateoas.utils.uri.resolver.ControllerUriResolver;

@RestController
@RequestMapping("/spectacles")
public class SpectaclesController {

  private final WebMvcLinkBuilderFactory linkBuilders;
  private final SpectaclesRepository repository;
  private final ItemRepository itemRepository;
  private final InvoiceRepository invoiceRepository;
  private final SpectaclesACL spectaclesACL;

  public SpectaclesController(
      WebMvcLinkBuilderFactory linkBuilders,
      SpectaclesRepository repository,
      ItemRepository itemRepository,
      InvoiceRepository invoiceRepository,
      SpectaclesACL spectaclesACL) {
    this.linkBuilders = requireNonNull(linkBuilders);
    this.repository = requireNonNull(repository);
    this.itemRepository = requireNonNull(itemRepository);
    this.invoiceRepository = requireNonNull(invoiceRepository);
    this.spectaclesACL = requireNonNull(spectaclesACL);
  }

  // SpectaclesController.java
  @GetMapping
  public ResponseEntity<?> list(ReturnPreference returnPreference) {
    Link selfLink =
        linkBuilders
            .linkTo(methodOn(SpectaclesController.class).list(null))
            .withSelfRel()
            .andAffordances(
              List.of(
                VoidAffordance.create(),
                afford(methodOn(SpectaclesController.class).create())));

    if (returnPreference == ReturnPreference.MINIMAL) {
      return ResponseEntity.ok(new RepresentationModel<>(selfLink));
    }

    List<EntityModel<Representation>> representations =
        findAll().map(Representation::new).map(Representation::toEntityModel).toList();
    return ResponseEntity.ok(CollectionModel.of(representations, selfLink));
  }

  @PostMapping
  public ResponseEntity<?> create() {
    Spectacles spectacles = repository.save(new Spectacles());
    return ResponseEntity.created(
            linkBuilders
                .linkTo(methodOn(SpectaclesController.class).findById(spectacles.id()))
                .toUri())
        .build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    return ResponseEntity.of(
        this.repository.findById(id).map(Representation::new).map(Representation::toEntityModel));
  }

  @PutMapping("/{id}/frame")
  public ResponseEntity<?> selectFrame(
      @PathVariable("id") Long id, @RequestBody EditItemCommand command) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Item item = command.itemId().flatMap(itemRepository::findById).orElse(null);
    if (item == null) {
      return ResponseEntity.badRequest().build();
    }

    repository.save(spectacles.selectFrame(item));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/frame")
  public ResponseEntity<?> deleteFrame(@PathVariable("id") Long id) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    this.repository.save(spectacles.deleteFrame());
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/right-lens")
  public ResponseEntity<?> selectRightLens(
      @PathVariable("id") Long id, @RequestBody EditItemCommand command) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Item item = command.itemId().flatMap(itemRepository::findById).orElse(null);
    if (item == null) {
      return ResponseEntity.badRequest().build();
    }

    repository.save(spectacles.selectRightLens(item));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/right-lens")
  public ResponseEntity<?> deleteRightLens(@PathVariable("id") Long id) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    this.repository.save(spectacles.deleteRightLens());
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/left-lens")
  public ResponseEntity<?> selectLeftLens(
      @PathVariable("id") Long id, @RequestBody EditItemCommand command) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Item item = command.itemId().flatMap(itemRepository::findById).orElse(null);
    if (item == null) {
      return ResponseEntity.badRequest().build();
    }

    repository.save(spectacles.selectLeftLens(item));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/left-lens")
  public ResponseEntity<?> deleteLeftLens(@PathVariable("id") Long id) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.notFound().build();
    }

    if (!spectaclesACL.canUpdate(spectacles)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    this.repository.save(spectacles.deleteLeftLens());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/invoiced")
  public ResponseEntity<?> doInvoice(@PathVariable("id") Long id) {
    Spectacles spectacles = this.repository.findById(id).orElse(null);
    if (spectacles == null) {
      return ResponseEntity.badRequest().build();
    }
    Invoice invoice = spectacles.invoice(invoiceRepository);
    return ResponseEntity.created(
            linkBuilders.linkTo(methodOn(InvoiceController.class).findById(invoice.id())).toUri())
        .build();
  }

  private Stream<Spectacles> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false);
  }

  private record EditItemCommand(@JsonProperty("itemUri") String itemUri) {

    @JsonCreator
    private EditItemCommand {}

    Optional<Long> itemId() {
      return ControllerUriResolver.on(methodOn(ItemController.class).findById(null))
          .resolve(itemUri, "id")
          .map(Long::parseLong);
    }
  }

  private class Representation {

    // SpectaclesRepresentation.java
    private final long id;
    private final Spectacles spectacles;

    public EntityModel<Representation> toEntityModel() {
      List<Affordance> affordances = new ArrayList<>();
      affordances.add(VoidAffordance.create());
      if (spectaclesACL.canUpdate(spectacles)) {
        affordances.add(afford(methodOn(SpectaclesController.class).selectFrame(id, null)));
        affordances.add(afford(methodOn(SpectaclesController.class).selectRightLens(id, null)));
        affordances.add(afford(methodOn(SpectaclesController.class).selectLeftLens(id, null)));
        if (spectacles.frame().isPresent()) {
          affordances.add(afford(methodOn(SpectaclesController.class).deleteFrame(id)));
        }
        if (spectacles.rightLens().isPresent()) {
          affordances.add(afford(methodOn(SpectaclesController.class).deleteRightLens(id)));
        }
        if (spectacles.leftLens().isPresent()) {
          affordances.add(afford(methodOn(SpectaclesController.class).deleteLeftLens(id)));
        }
      }

      if (spectacles.isEligibleForInvoicing()) {
        affordances.add(afford(methodOn(SpectaclesController.class).doInvoice(id)));
      }
      return EntityModel.of(
          this,
          linkBuilders
              .linkTo(methodOn(SpectaclesController.class).findById(id))
              .addAffordances(affordances)
              .withSelfRel());
    }

    private Representation(Spectacles spectacles) {
      this.id = spectacles.id();
      this.spectacles = spectacles;
      this.frameLabel = spectacles.frame().map(Item::label).orElse(null);
      this.framePrice = spectacles.frame().map(Item::price).orElse(null);
      this.rightLensLabel = spectacles.rightLens().map(Item::label).orElse(null);
      this.rightLensPrice = spectacles.rightLens().map(Item::price).orElse(null);
      this.leftLensLabel = spectacles.leftLens().map(Item::label).orElse(null);
      this.leftLensPrice = spectacles.leftLens().map(Item::price).orElse(null);
    }

    private String frameLabel;
    private String rightLensLabel;
    private String leftLensLabel;
    private Double framePrice;
    private Double rightLensPrice;
    private Double leftLensPrice;

    @JsonProperty
    public String getFrameLabel() {
      return frameLabel;
    }

    @JsonProperty
    public Double getFramePrice() {
      return framePrice;
    }

    @JsonProperty
    public String getRightLensLabel() {
      return rightLensLabel;
    }

    @JsonProperty
    public Double getRightLensPrice() {
      return rightLensPrice;
    }

    @JsonProperty
    public String getLeftLensLabel() {
      return leftLensLabel;
    }

    @JsonProperty
    public Double getLeftLensPrice() {
      return leftLensPrice;
    }
  }
}

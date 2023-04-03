package com.cos.devoxx.hypermediairl.core.spectacles;

import com.cos.devoxx.hypermediairl.core.invoice.Invoice;
import com.cos.devoxx.hypermediairl.core.invoice.InvoiceRepository;
import com.cos.devoxx.hypermediairl.core.item.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Optional;

@Entity
public class Spectacles {
  @Id private long id;
  @ManyToOne private Item frame;
  @ManyToOne private Item rightLens;
  @ManyToOne private Item leftLens;

  public Spectacles updateFrame(Item frame) {
    this.frame = frame;
    return this;
  }

  public Spectacles deleteFrame() {
    this.frame = null;
    return this;
  }

  public Spectacles updateRightLens(Item rightLens) {
    this.rightLens = rightLens;
    return this;
  }

  public Spectacles deleteRightLens() {
    this.rightLens = null;
    return this;
  }

  public Spectacles updateLeftLens(Item leftLens) {
    this.leftLens = leftLens;
    return this;
  }

  public Spectacles deleteLeftLens() {
    this.leftLens = null;
    return this;
  }

  public Double totalPrice() {
    return frame().map(Item::price).orElse(0d)
        + rightLens().map(Item::price).orElse(0d)
        + leftLens().map(Item::price).orElse(0d);
  }

  public Invoice invoice(InvoiceRepository invoiceRepository) {
    return invoiceRepository.save(new Invoice(this));
  }

  public boolean isEligibleForInvoicing() {
    return frame != null && rightLens != null && leftLens != null;
  }

  public long id() {
    return id;
  }

  public Optional<Item> frame() {
    return Optional.ofNullable(frame);
  }

  public Optional<Item> rightLens() {
    return Optional.ofNullable(rightLens);
  }

  public Optional<Item> leftLens() {
    return Optional.ofNullable(leftLens);
  }
}

package com.cos.devoxx.hypermediairl.core.invoice;

import com.cos.devoxx.hypermediairl.core.spectacles.Spectacles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Invoice {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private long id;

  @ManyToOne private Spectacles spectacles;

  @Column Double total;

  @Deprecated(since = "now")
  protected Invoice() {}

  public Invoice(Spectacles spectacles) {
    this.spectacles = spectacles;
    this.total = spectacles.totalPrice();
  }

  public long id() {
    return id;
  }

  public Spectacles spectacles() {
    return spectacles;
  }

  public Double total() {
    return total;
  }
}

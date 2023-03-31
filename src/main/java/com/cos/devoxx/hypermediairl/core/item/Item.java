package com.cos.devoxx.hypermediairl.core.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {

  @Id private long id;

  @Column private Double price;

  @Column private String label;

  public Item(Double price, String label) {
    this.price = price;
    this.label = label;
  }

  @Deprecated(since = "now")
  protected Item() {}

  public long id() {
    return id;
  }

  public Double price() {
    return price;
  }

  public String label() {
    return label;
  }
}

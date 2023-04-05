package com.cos.devoxx.hypermediairl;

import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;

public class FixedResourceNameRelationProvider extends DefaultLinkRelationProvider {

  @Override
  public LinkRelation getCollectionResourceRelFor(Class<?> type) {
    return LinkRelation.of("content");
  }

  @Override
  public LinkRelation getItemResourceRelFor(Class<?> type) {
    return LinkRelation.of("content");
  }

  @Override
  public int getOrder() {
    return 0;
  }
}

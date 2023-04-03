package com.cos.devoxx.hypermediairl.http.spectacles;

import com.cos.devoxx.hypermediairl.core.spectacles.Spectacles;
import org.springframework.stereotype.Component;

@Component
public class SpectaclesACL {

  public boolean canUpdate(Spectacles spectacles) {
    return spectacles.id() != 2;
  }
}

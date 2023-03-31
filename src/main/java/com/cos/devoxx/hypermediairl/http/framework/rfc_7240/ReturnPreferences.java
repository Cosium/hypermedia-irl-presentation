package com.cos.devoxx.hypermediairl.http.framework.rfc_7240;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**
 * @author Réda Housni Alaoui
 */
class ReturnPreferences {

  private static final Logger LOG = LoggerFactory.getLogger(ReturnPreferences.class);

  private static final Pattern PREFER_RETURN_REQUEST_HEADER_PATTERN =
      Pattern.compile("return=(.*)");

  public static ReturnPreference parse(NativeWebRequest request) {
    String[] preferHeaderValues =
        ofNullable(request.getHeaderValues(Rfc7240Headers.PREFER)).orElse(new String[0]);
    return parse(preferHeaderValues);
  }

  public static ReturnPreference parse(ServerHttpRequest request) {
    String[] preferHeaderValues =
        ofNullable(request.getHeaders().get(Rfc7240Headers.PREFER))
            .orElse(Collections.emptyList())
            .toArray(new String[0]);
    return parse(preferHeaderValues);
  }

  private static ReturnPreference parse(String[] preferHeaderValues) {

    Collection<ReturnPreference> preferences =
        Stream.of(preferHeaderValues)
            .map(PREFER_RETURN_REQUEST_HEADER_PATTERN::matcher)
            .filter(Matcher::matches)
            .map(matcher -> matcher.group(1))
            .map(ReturnPreferences::parseReturnPreference)
            .collect(Collectors.toList());

    if (preferences.size() > 1) {
      LOG.warn("Found multiple return preferences");
      return ReturnPreference.UNDEFINED;
    }

    return preferences.stream().findFirst().orElse(ReturnPreference.UNDEFINED);
  }

  private static ReturnPreference parseReturnPreference(String returnValue) {
    try {
      return ReturnPreference.valueOf(returnValue.toUpperCase());
    } catch (IllegalArgumentException e) {
      return ReturnPreference.UNDEFINED;
    }
  }
}

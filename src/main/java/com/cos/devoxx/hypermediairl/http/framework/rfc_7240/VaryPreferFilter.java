package com.cos.devoxx.hypermediairl.http.framework.rfc_7240;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Réda Housni Alaoui
 */
class VaryPreferFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    response.addHeader(HttpHeaders.VARY, Rfc7240Headers.PREFER);

    filterChain.doFilter(request, response);
  }
}

package com.cos.devoxx.hypermediairl.http.framework.rfc_7240;

/**
 * https://datatracker.ietf.org/doc/html/rfc7240#section-4.2
 *
 * <p>The "return=representation" preference indicates that the client prefers that the server
 * include an entity representing the current state of the resource in the response to a successful
 * request.
 *
 * <p>The "return=minimal" preference, on the other hand, indicates that the client wishes the
 * server to return only a minimal response to a successful request. Typically, such responses would
 * utilize the 204 (No Content) status, but other codes MAY be used as appropriate, such as a 200
 * (OK) status with a zero-length response entity. The determination of what constitutes an
 * appropriate minimal response is solely at the discretion of the server.
 *
 * <p>ABNF:
 *
 * <p>return = "return" BWS "=" BWS ("representation" / "minimal")
 *
 * <p>When honoring the "return=representation" preference, the returned representation might not be
 * a representation of the effective request URI when the request is affecting another resource. In
 * such cases, the Content-Location header can be used to identify the URI of the returned
 * representation.
 *
 * <p>The "return=representation" preference is intended to provide a means of optimizing
 * communication between the client and server by eliminating the need for a subsequent GET request
 * to retrieve the current representation of the resource following a modification.
 *
 * <p>After successfully processing a modification request such as a POST or PUT, a server can
 * choose to return either an entity describing the status of the operation or a representation of
 * the modified resource itself. While the selection of which type of entity to return, if any at
 * all, is solely at the discretion of the server, the "return=representation" preference -- along
 * with the "return=minimal" preference defined below -- allow the server to take the client's
 * preferences into consideration while constructing the response.
 *
 * <p>An example request specifying the "return=representation" preference:
 *
 * <p>PATCH /item/123 HTTP/1.1 Host: example.org Content-Type: application/example-patch Prefer:
 * return=representation
 *
 * <p>1c1 < ABCDEFGHIJKLMNOPQRSTUVWXYZ --- > BCDFGHJKLMNPQRSTVWXYZ
 *
 * <p>An example response containing the resource representation:
 *
 * <p>HTTP/1.1 200 OK Content-Location: http://example.org/item/123 Content-Type: text/plain ETag:
 * "d3b07384d113edec49eaa6238ad5ff00"
 *
 * <p>BCDFGHJKLMNPQRSTVWXYZ
 *
 * <p>In contrast, the "return=minimal" preference can reduce the amount of data the server is
 * required to return to the client following a request. This can be particularly useful, for
 * instance, when communicating with limited-bandwidth mobile devices or when the client simply does
 * not require any further information about the result of a request beyond knowing if it was
 * successfully processed.
 *
 * <p>An example request specifying the "return=minimal" preference:
 *
 * <p>POST /collection HTTP/1.1 Host: example.org Content-Type: text/plain Prefer: return=minimal
 *
 * <p>{Data}
 *
 * <p>An example minimal response:
 *
 * <p>HTTP/1.1 201 Created Location: http://example.org/collection/123
 *
 * <p>The "return=minimal" and "return=representation" preferences are mutually exclusive
 * directives. It is anticipated that there will never be a situation where it will make sense for a
 * single request to include both preferences. Any such requests will likely be the result of a
 * coding error within the client. As such, a request containing both preferences can be treated as
 * though neither were specified.
 *
 * @author Réda Housni Alaoui
 */
public enum ReturnPreference {
  /**
   * The "return=representation" preference indicates that the client prefers that the server
   * include an entity representing the current state of the resource in the response to a
   * successful request.
   */
  REPRESENTATION,
  /**
   * The "return=minimal" preference, on the other hand, indicates that the client wishes the server
   * to return only a minimal response to a successful request. Typically, such responses would
   * utilize the 204 (No Content) status, but other codes MAY be used as appropriate, such as a 200
   * (OK) status with a zero-length response entity. The determination of what constitutes an
   * appropriate minimal response is solely at the discretion of the server.
   */
  MINIMAL,
  UNDEFINED
}

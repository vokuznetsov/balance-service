package cloud.evrone.balance.utils;

import java.net.URI;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@UtilityClass
public class WebClientUtils {

  public static <T, V> T makePostRequest(
      Function<UriBuilder, URI> uriFunction,
      WebTestClient client,
      V body,
      Class<V> bodyType,
      Class<T> returnType) {
    return client.mutate().responseTimeout(Duration.ofSeconds(30)).build()
        .post()
        .uri(uriFunction)
        .body(Mono.just(body), bodyType)
        .exchange()
        .expectStatus().isOk()
        .expectBody(returnType)
        .returnResult().getResponseBody();
  }

  public static <T, V> T makePutRequest(
      Function<UriBuilder, URI> uriFunction,
      WebTestClient client,
      V body,
      Class<V> bodyType,
      Class<T> returnType) {
    return client.mutate().responseTimeout(Duration.ofSeconds(30)).build()
        .put()
        .uri(uriFunction)
        .body(Mono.just(body), bodyType)
        .exchange()
        .expectStatus().isOk()
        .expectBody(returnType)
        .returnResult().getResponseBody();
  }

  public static <T> T makeGetRequest(
      Function<UriBuilder, URI> uriFunction,
      WebTestClient client,
      Class<T> returnType) {

    return makeGetRequest(uriFunction, (h) -> {}, client, returnType);
  }

  public static <T> T makeGetRequest(
      Function<UriBuilder, URI> uriFunction,
      Consumer<HttpHeaders> headers,
      WebTestClient client,
      Class<T> returnType) {

    return client.mutate().responseTimeout(Duration.ofSeconds(30)).build()
        .get()
        .uri(uriFunction)
        .headers(headers)
        .exchange()
        .expectStatus().isOk()
        .expectBody(returnType)
        .returnResult().getResponseBody();
  }

  public static <T> T makeDeleteRequest(
      Function<UriBuilder, URI> uriFunction,
      WebTestClient client,
      Class<T> returnType) {

    return client.mutate().responseTimeout(Duration.ofSeconds(30)).build()
        .delete()
        .uri(uriFunction)
        .exchange()
        .expectStatus().isOk()
        .expectBody(returnType)
        .returnResult().getResponseBody();
  }

}

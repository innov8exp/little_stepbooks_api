package co.botechservices.novlnovl.infrastructure.manager.webclient;

import co.botechservices.novlnovl.infrastructure.exception.APIResponseArrayException;
import co.botechservices.novlnovl.infrastructure.exception.APIResponseException;
import co.botechservices.novlnovl.infrastructure.model.APIResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class WebClientAbstract {

    public static final int TIMEOUT = 15000;

    static WebClient create(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
//                .proxy(typeSpec -> typeSpec.type(ProxyProvider.Proxy.SOCKS5).host("127.0.0.1").port(1080))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(baseUrl)
                .build();
    }

    @NotNull
    protected Mono<Throwable> getThrowableMono(ClientResponse response) {
        Mono<APIResponse> apiResponseMono = response.bodyToMono(APIResponse.class);
        return apiResponseMono.flatMap(apiResponse -> {
            APIResponseException apiResponseException = new APIResponseException(
                    response.rawStatusCode(), apiResponse.getMessage());
            return Mono.error(apiResponseException);
        });
    }

    @NotNull
    protected Mono<Throwable> getThrowableFlux(ClientResponse response) {
        Mono<ResponseEntity<List<APIResponse>>> apiResponseMono = response.toEntityList(APIResponse.class);
        return apiResponseMono.flatMap(apiResponse -> {
            List<APIResponse> body = apiResponse.getBody();
            APIResponseArrayException apiResponseArrayException = new APIResponseArrayException(body);
            return Mono.error(apiResponseArrayException);
        });
    }

}

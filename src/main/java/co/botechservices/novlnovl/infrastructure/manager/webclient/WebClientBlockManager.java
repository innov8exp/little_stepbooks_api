package co.botechservices.novlnovl.infrastructure.manager.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class WebClientBlockManager extends WebClientAbstract {

    private final WebClient webClient;

    public WebClientBlockManager(WebClient webClient) {
        this.webClient = webClient;
    }

    public static WebClientBlockManager build(String baseUrl) {
        WebClient webClient = create(baseUrl);
        return new WebClientBlockManager(webClient);
    }

    public <T> T get(String path, Class<T> returnType) {
        return webClient.get().uri(path).retrieve()
                .onStatus(HttpStatus::isError, this::getThrowableMono)
                .bodyToMono(returnType).block();
    }

    public <T> T get(URI uri, Class<T> returnType) {
        return webClient.get().uri(uri).retrieve().bodyToMono(returnType).block();
    }

    public <T> T post(String path, Object postBody, Class<T> returnType) {
        WebClient.RequestBodySpec requestBodySpec = webClient.post().uri(path);
        if (!ObjectUtils.isEmpty(postBody)) {
            requestBodySpec.body(Mono.just(postBody), Object.class);
        }
        return requestBodySpec.retrieve().bodyToMono(returnType).block();
    }

    public <T> T post(URI uri, Object postBody, Class<T> returnType) {
        WebClient.RequestBodySpec requestBodySpec = webClient.post().uri(uri);
        if (!ObjectUtils.isEmpty(postBody)) {
            requestBodySpec.body(Mono.just(postBody), Object.class);
        }
        return requestBodySpec.retrieve().bodyToMono(returnType).block();
    }

    public <T> T put(String path, Object postBody, Class<T> returnType) {
        return webClient.put().uri(path)
                .body(Mono.just(postBody), Object.class)
                .retrieve().bodyToMono(returnType).block();
    }

    public <T> T put(URI uri, Object postBody, Class<T> returnType) {
        return webClient.put().uri(uri)
                .body(Mono.just(postBody), Object.class)
                .retrieve().bodyToMono(returnType).block();
    }

    public <T> T delete(String path, Class<T> returnType) {
        return webClient.delete().uri(path)
                .retrieve().bodyToMono(returnType).block();
    }

    public <T> T delete(URI uri, Class<T> returnType) {
        return webClient.delete().uri(uri)
                .retrieve().bodyToMono(returnType).block();
    }

    public void upload(String path, String fieldName, MultipartFile file) {
        webClient.post().uri(path)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fieldName, file))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void upload(URI uri, String fieldName, MultipartFile file) {
        webClient.post().uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fieldName, file))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll() {
        log.debug("Request received to list all producers");

        var response = Producer.getProducers()
                .stream()
                .map(MAPPER::toProduceGetResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("filter")
    public ResponseEntity<ProducerGetResponse> filter(@RequestParam String name){
        log.debug("Request received to list all producers, param name '{}'", name);

        var response = Producer.getProducers().stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(MAPPER::toProduceGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found!"));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id){
        log.debug("Request to find producer by id: {}", id);

        var response = Producer.getProducers().stream()
                .filter( n -> n.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProduceGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found!"));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest){
        log.debug("Request to save producer: {}", producerPostRequest);

        var producer = MAPPER.toProducerPostRequest(producerPostRequest);
        var response = MAPPER.toProduceGetResponse(producer);
        Producer.getProducers().add(producer);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.debug("Request to delete producer by id: {}", id);

        var producer = Producer.getProducers().stream()
                .filter( n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found!"));

        Producer.getProducers().remove(producer);
        return ResponseEntity.noContent().build();
    }
}

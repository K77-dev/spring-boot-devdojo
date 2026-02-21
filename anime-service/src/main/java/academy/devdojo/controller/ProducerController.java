package academy.devdojo.controller;

import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerMapper mapper;
    private final ProducerService service;

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll() {
        log.debug("Request received to list all producers");

        var producers = service.findAll();
        var response = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("filter")
    public ResponseEntity<List<ProducerGetResponse>> findByName(@RequestParam String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = service.findByName(name);
        var response = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producer = service.findById(id);
        var response = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        log.debug("Request to save producer: {}", producerPostRequest);

        var producer = mapper.toProducer(producerPostRequest);
        var producerSaved = service.save(producer);
        var response = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        var producer = service.findById(id);
        service.delete(producer.getId());

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {
        log.debug("Request to update producer: {}", producerPutRequest);

        var producer = mapper.toProducer(producerPutRequest);
        service.update(producer);

        return ResponseEntity.noContent().build();
    }
}

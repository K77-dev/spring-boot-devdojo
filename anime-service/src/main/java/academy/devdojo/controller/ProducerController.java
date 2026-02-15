package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

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
        var response = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("filter")
    public ResponseEntity<List<ProducerGetResponse>> findByName(@RequestParam String name){
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = service.findByName(name);
        var response = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id){
        log.debug("Request to find producer by id: {}", id);

        var producer = service.findById(id);
        var response = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest){
        log.debug("Request to save producer: {}", producerPostRequest);

        var producer = MAPPER.toProducer(producerPostRequest);
        service.save(producer);
        var response = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.debug("Request to delete producer by id: {}", id);

        var producer = service.findById(id);
        service.delete(producer.getId());

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest){
        log.debug("Request to update producer: {}", producerPutRequest);

        var producer = MAPPER.toProducer(producerPutRequest);
        service.update(producer);

        return ResponseEntity.noContent().build();
    }
}

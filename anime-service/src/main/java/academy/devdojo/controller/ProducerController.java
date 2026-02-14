package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public List<Producer> listAll() {
        return Producer.getProducers();
    }

    @GetMapping("filter")
    public List<Producer> filter(@RequestParam String name){
        return Producer.getProducers().stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("/{id}")
    public Producer findById(@PathVariable Long id){
        return Producer.getProducers().stream()
                .filter( n -> n.getId().equals(id))
                .findFirst().orElse(null);
    }

    @PostMapping
    public ResponseEntity<Producer> save(@RequestBody Producer producer){
        producer.setId(ThreadLocalRandom.current().nextLong(1000_000));
        Producer.getProducers().add(producer);

        return ResponseEntity.ok(producer);
        //return ResponseEntity.status(HttpStatus.CREATED).body(producer);
    }
}

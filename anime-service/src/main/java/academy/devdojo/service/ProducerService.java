package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ProducerService {

    private ProducerHardCodedRepository repository;

    public ProducerService() {
        this.repository = new ProducerHardCodedRepository();
    }

    public List<Producer> findAll(){
        return repository.findAll();
    }

    public Producer findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found!"));
    }

    public List<Producer> findByName(String name){
        return repository.findByName(name);
    }

    public Producer save(Producer producer){
        return repository.save(producer);
    }

    public void delete(Long id){
        repository.delete(findById(id));
    }

    public void update(Producer producerToUpdate){
        var producer = findById(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
         repository.update(producerToUpdate);
    }
}

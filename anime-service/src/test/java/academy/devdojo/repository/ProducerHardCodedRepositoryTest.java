package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest  {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;
    private final List<Producer> producers = new ArrayList<>();

    @BeforeEach
    void init(){
        producers.addAll(
                List.of(
                        Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build(),
                        Producer.builder(  ).id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(3L).name("Studio Guibli ").createdAt(LocalDateTime.now()).build()
                )
        );
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    }

    @Test
    @DisplayName("findAll return a list with all producers")
    void findAll(){
        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producers);
    }

    @Test
    @DisplayName("findById returns producer with a given Id")
    void findById(){
        var exprectedProducer  = producers.getFirst();
        var producers = repository.findById(exprectedProducer.getId());
        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void findByName(){
        var exprectedProducer  = producers.getFirst();
        var producers = repository.findByName(exprectedProducer.getName());
        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void save(){
        var exprectedProducer  = producers.getFirst();
        var producers = repository.findByName(exprectedProducer.getName());
        Assertions.assertThat(producers).contains(exprectedProducer);
    }
}
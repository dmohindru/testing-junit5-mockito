package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.repositories.VetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetSDJpaServiceTest {

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private VetSDJpaService vetSDJpaService;

    @Test
    void findAll() {
        Set<Vet> vets = Set.of(new Vet(1L, "Dhruv", "Mohindru", null),
                new Vet(1L, "Dhruv", "Mohindru", null));
        when(vetSDJpaService.findAll()).thenReturn(vets);

        Set<Vet> returnedVets = vetSDJpaService.findAll();
        assertEquals(2, returnedVets.size());
    }

    @Test
    void findById() {
        Vet vet = new Vet(1L, "Dhruv", "Mohindru", null);
        when(vetSDJpaService.findById(1L)).thenReturn(vet);
        Vet returnedVet = vetSDJpaService.findById(1L);
        assertEquals(vet.getFirstName(), returnedVet.getFirstName());
        verify(vetRepository, atLeastOnce()).findById(anyLong());

    }

    @Test
    void save() {
        Vet vet = new Vet(1L, "Dhruv", "Mohindru", null);
        when(vetSDJpaService.save(vet)).thenReturn(vet);
        Vet returnedVet = vetSDJpaService.save(vet);
        assertEquals(vet.getFirstName(), returnedVet.getFirstName());
        verify(vetRepository, atLeastOnce()).save(any(Vet.class));
    }

    @Test
    void delete() {
        Vet vet = new Vet(1L, "Dhruv", "Mohindru", null);
        vetSDJpaService.delete(vet);
        verify(vetRepository,atLeastOnce()).delete(any(Vet.class));

    }

    @Test
    void deleteById() {
        vetSDJpaService.deleteById(1L);
        verify(vetRepository, times(1)).deleteById(1L);
    }
}
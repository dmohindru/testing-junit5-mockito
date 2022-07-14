package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService visitSDJpaService;

    @Test
    void findAll() {


        Set<Visit> visits = Set.of(new Visit(),
                new Visit());

        // Given
        given(visitRepository.findAll()).willReturn(visits);

        // When
        Set<Visit> returnedVisits = visitSDJpaService.findAll();

        // Then
        assertEquals(2, returnedVisits.size());
        then(visitRepository).should(atLeastOnce()).findAll();

    }

    @Test
    void findById() {

        // Given
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(new Visit()));

        // When
        Visit returnedVisit = visitSDJpaService.findById(1L);

        // Then
        assertNotNull(returnedVisit);
        then(visitRepository).should(atLeastOnce()).findById(anyLong());
    }

    @Test
    void save() {

        // Given
        given(visitRepository.save(any(Visit.class))).willReturn(new Visit());

        // When
        Visit returnedVisit = visitSDJpaService.save(new Visit());

        // Then
        assertNotNull(returnedVisit);
        then(visitRepository).should( atMost(1)).save(any(Visit.class));
    }

    @Test
    void delete() {

        // Given - none

        // When
        visitSDJpaService.delete(new Visit());

        // Then
        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {

        // Given - none

        // When
        visitSDJpaService.deleteById(1L);

        // Then
        then(visitRepository).should(atMost(2)).deleteById(anyLong());
    }
}
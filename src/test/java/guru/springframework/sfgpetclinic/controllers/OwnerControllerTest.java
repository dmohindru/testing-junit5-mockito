package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    @Mock
    private OwnerService ownerService;

    @Mock
    private Model model;

    @InjectMocks
    private OwnerController ownerController;

    @Mock
    BindingResult bindingResult;

   // Alternative way to declare an argument captor
    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {

        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(
                invocation -> {
                    List<Owner> owners = new ArrayList<>();

                    String name = invocation.getArgument(0);

                    if (name.equals("%Buck%")) {
                        owners.add(new Owner(1L, "Joe", "Buck"));
                        return owners;
                    } else if (name.equals("%DontFindMe%")) {
                        return owners;
                    }
                    else if (name.equals("%FindMe%")) {
                        owners.add(new Owner(1L, "Joe", "Buck"));
                        owners.add(new Owner(2L, "Joe2", "Buck2"));
                        return owners;
                    }

                    throw new RuntimeException("Invalid Argument");
                }
        );
    }

    /*@Test
    void processFIndFormWildcardString() {
        // given
        Owner owner = new Owner(1L, "Joe", "Buck");

        List<Owner> ownerList = new ArrayList<>();
        // ArgumentCaptor is used to capture all the arguments going into a particular method
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(ownerList);


        // when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        // then
        assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
    }*/

    @Test
    void processFIndFormWildcardStringAnnotation() {
        // given
        Owner owner = new Owner(1L, "Joe", "Buck");

        // when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        // then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFormWildcardNotFound() {
        // given
        Owner owner = new Owner(1L, "Joe", "DontFindMe");

        // when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);
        verifyNoMoreInteractions(ownerService);

        // then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

    @Test
    void processFindFormWildcardFound() {
        // given
        Owner owner = new Owner(1L, "Joe", "FindMe");
        InOrder inOrder = inOrder(ownerService, model);

        // when
        String viewName = ownerController.processFindForm(owner, bindingResult, model);

        // then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

        // inorder assertion
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);

    }

    @Test
    void processCreationFormReturnError() {

        // Given
        Owner owner = new Owner(5L, "Dhruv", "Mohindru");
        given(bindingResult.hasErrors()).willReturn(true);


        // When
        String error = ownerController.processCreationForm(owner, bindingResult);

        //Then
        assertThat(error).isEqualTo(VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
        then(ownerService).should(never()).save(any(Owner.class));

    }

    @Test
    void processCreationFormReturnSuccess() {
        Owner owner = new Owner(5L, "Dhruv", "Mohindru");

        // Given
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any(Owner.class))).willReturn(owner);

        // When
        String message = ownerController.processCreationForm(owner, bindingResult);

        //Then
        assertThat(message).isEqualTo("redirect:/owners/5");
        then(ownerService).should(atLeastOnce()).save(any(Owner.class));

    }
}
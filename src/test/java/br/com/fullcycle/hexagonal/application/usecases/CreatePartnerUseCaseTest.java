package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CreatePartnerUseCaseTest {


    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreate() {

        // given
        final String expectedCNPJ = "12345678901";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        Mockito.when(partnerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.empty());
        Mockito.when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        Mockito.when(partnerService.save(any())).thenAnswer(a -> {
            var partner = a.getArgument(0, Partner.class);
            partner.setId(1L);
            return partner;
        });

        final CreatePartnerUseCase useCase = new CreatePartnerUseCase(partnerService);
        final CreatePartnerUseCase.Output output = useCase.execute(createInput);

        // then
        assertNotNull(output.id());
        assertEquals(expectedCNPJ, output.cnpj());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não dece cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

        // given
        final String expectedCNPJ = "12345678901";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        final var aPartner = new Partner();
        aPartner.setId(1L);
        aPartner.setCnpj(expectedCNPJ);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        Mockito.when(partnerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.of(aPartner));

        final CreatePartnerUseCase useCase = new CreatePartnerUseCase(partnerService);

        // then
        assertThrows(ValidationException.class, () -> useCase.execute(input));
    }

    @Test
    @DisplayName("Não dece cadastrar um parceiro com email duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

        // given
        final String expectedCNPJ = "12345678901";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        final var aPartner = new Partner();
        aPartner.setId(1L);
        aPartner.setCnpj(expectedCNPJ);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);
        Mockito.when(partnerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.of(aPartner));

        final CreatePartnerUseCase useCase = new CreatePartnerUseCase(partnerService);

        // then
        assertThrows(ValidationException.class, () -> useCase.execute(input));
    }
}
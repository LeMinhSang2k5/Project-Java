package com.java.backend.service;

import com.java.backend.entity.MedicalSupply;
import com.java.backend.repository.MedicalSupplyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalSupplyServiceTest {

    @Mock
    private MedicalSupplyRepository medicalSupplyRepository;

    @InjectMocks
    private MedicalSupplyService medicalSupplyService;

    @Test
    void createMedicalSupply_shouldSaveValidSupply() {
        MedicalSupply supply = sampleSupply();
        when(medicalSupplyRepository.findByName("Paracetamol")).thenReturn(Optional.empty());
        when(medicalSupplyRepository.save(any(MedicalSupply.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalSupply result = medicalSupplyService.createMedicalSupply(supply);

        assertSame(supply, result);
        verify(medicalSupplyRepository).save(supply);
    }

    @Test
    void createMedicalSupply_shouldRejectDuplicateName() {
        MedicalSupply supply = sampleSupply();
        MedicalSupply existing = sampleSupply();
        existing.setId(2L);
        when(medicalSupplyRepository.findByName("Paracetamol")).thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicalSupplyService.createMedicalSupply(supply));

        assertTrue(exception.getMessage().contains("đã tồn tại"));
        verify(medicalSupplyRepository, never()).save(any());
    }

    @Test
    void createMedicalSupply_shouldRejectEmptyName() {
        MedicalSupply supply = sampleSupply();
        supply.setName(" ");
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
        supply.setName(null);
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
    }

    @Test
    void createMedicalSupply_shouldRejectInvalidQuantity() {
        MedicalSupply supply = sampleSupply();
        supply.setQuantity(0);
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
        supply.setQuantity(null);
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
    }

    @Test
    void createMedicalSupply_shouldRejectEmptyCategory() {
        MedicalSupply supply = sampleSupply();
        supply.setCategory(" ");
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
        supply.setCategory(null);
        assertThrows(RuntimeException.class, () -> medicalSupplyService.createMedicalSupply(supply));
    }

    @Test
    void getAllMedicalSupplies_shouldReturnList() {
        when(medicalSupplyRepository.findAll()).thenReturn(List.of(sampleSupply()));
        assertFalse(medicalSupplyService.getAllMedicalSupplies().isEmpty());
    }

    @Test
    void getMedicalSupplyById_shouldReturnOptional() {
        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.of(sampleSupply()));
        assertTrue(medicalSupplyService.getMedicalSupplyById(1L).isPresent());
    }

    @Test
    void updateMedicalSupply_shouldUpdateExistingSupply() {
        MedicalSupply existing = sampleSupply();
        existing.setId(1L);
        MedicalSupply update = new MedicalSupply();
        update.setName("New Name");
        update.setQuantity(50);
        update.setCategory("New Category");
        update.setExpiryDate(LocalDate.now());

        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(medicalSupplyRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(medicalSupplyRepository.save(any(MedicalSupply.class))).thenAnswer(i -> i.getArgument(0));

        MedicalSupply result = medicalSupplyService.updateMedicalSupply(1L, update);
        assertEquals("New Name", result.getName());
        assertEquals(50, result.getQuantity());
        assertEquals("New Category", result.getCategory());
        assertNotNull(result.getExpiryDate());
    }

    @Test
    void updateMedicalSupply_shouldRejectDuplicateNameOnOtherEntity() {
        MedicalSupply existing = sampleSupply();
        existing.setId(1L);
        MedicalSupply update = sampleSupply();
        update.setName("Ibuprofen");
        MedicalSupply another = sampleSupply();
        another.setId(5L);
        another.setName("Ibuprofen");

        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(medicalSupplyRepository.findByName("Ibuprofen")).thenReturn(Optional.of(another));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicalSupplyService.updateMedicalSupply(1L, update));

        assertTrue(exception.getMessage().contains("đã tồn tại"));
        verify(medicalSupplyRepository, never()).save(any());
    }
    
    @Test
    void updateMedicalSupply_shouldAllowDuplicateNameOnSameEntity() {
        MedicalSupply existing = sampleSupply();
        existing.setId(1L);
        existing.setName("Ibuprofen");
        MedicalSupply update = sampleSupply();
        update.setName("Ibuprofen");

        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(medicalSupplyRepository.findByName("Ibuprofen")).thenReturn(Optional.of(existing));
        when(medicalSupplyRepository.save(any())).thenReturn(existing);

        MedicalSupply result = medicalSupplyService.updateMedicalSupply(1L, update);
        assertNotNull(result);
    }

    @Test
    void updateMedicalSupply_shouldThrowWhenMissing() {
        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> medicalSupplyService.updateMedicalSupply(1L, new MedicalSupply()));
    }

    @Test
    void updateMedicalSupply_shouldNotUpdateNullValues() {
        MedicalSupply existing = sampleSupply();
        existing.setId(1L);
        MedicalSupply update = new MedicalSupply(); // all fields null

        when(medicalSupplyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(medicalSupplyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MedicalSupply result = medicalSupplyService.updateMedicalSupply(1L, update);
        assertEquals("Paracetamol", result.getName());
    }

    @Test
    void deleteMedicalSupply_shouldDelete() {
        when(medicalSupplyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalSupplyRepository).deleteById(1L);
        medicalSupplyService.deleteMedicalSupply(1L);
        verify(medicalSupplyRepository).deleteById(1L);
    }

    @Test
    void deleteMedicalSupply_shouldThrowWhenMissing() {
        when(medicalSupplyRepository.existsById(9L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> medicalSupplyService.deleteMedicalSupply(9L));

        assertTrue(exception.getMessage().contains("không tồn tại"));
    }

    @Test
    void searchMedicalSupplies_shouldReturnList() {
        when(medicalSupplyRepository.findByNameContainingIgnoreCase("Para")).thenReturn(List.of(sampleSupply()));
        assertFalse(medicalSupplyService.searchMedicalSupplies("Para").isEmpty());
    }

    @Test
    void getMedicalSuppliesByCategory_shouldReturnList() {
        when(medicalSupplyRepository.findByCategory("Thuốc")).thenReturn(List.of(sampleSupply()));
        assertFalse(medicalSupplyService.getMedicalSuppliesByCategory("Thuốc").isEmpty());
    }

    @Test
    void getLowStockSupplies_shouldReturnList() {
        when(medicalSupplyRepository.findByQuantityLessThan(10)).thenReturn(List.of(sampleSupply()));
        assertFalse(medicalSupplyService.getLowStockSupplies(10).isEmpty());
    }

    @Test
    void getLowStockSuppliesByCategory_shouldDelegateToRepository() {
        List<MedicalSupply> expected = List.of(sampleSupply());
        when(medicalSupplyRepository.findByCategoryAndQuantityLessThan("Thuốc", 10)).thenReturn(expected);

        List<MedicalSupply> result = medicalSupplyService.getLowStockSuppliesByCategory("Thuốc", 10);

        assertSame(expected, result);
        verify(medicalSupplyRepository).findByCategoryAndQuantityLessThan("Thuốc", 10);
    }

    private MedicalSupply sampleSupply() {
        MedicalSupply supply = new MedicalSupply();
        supply.setName("Paracetamol");
        supply.setQuantity(20);
        supply.setCategory("Thuốc");
        supply.setExpiryDate(LocalDate.of(2026, 12, 31));
        return supply;
    }
}

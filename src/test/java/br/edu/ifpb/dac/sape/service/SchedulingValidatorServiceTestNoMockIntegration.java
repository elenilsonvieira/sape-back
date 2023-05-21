package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import br.edu.ifpb.dac.sape.business.service.SchedulingValidatorService;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;

public class SchedulingValidatorServiceTestNoMockIntegration {

		private SchedulingValidatorService validatorService;

        @BeforeEach
        public void setUpBeforeEach() {
            validatorService = new SchedulingValidatorService();

        }

        @Test
        public void testValidateEntityAndDtoOk() {
            Scheduling entity = new Scheduling();
            try {
                boolean isEntityValid = validatorService.validateScheduling(entity);
                assertTrue(isEntityValid);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        public void testValidateSchedulingDtoOk() {
            SchedulingDTO dto = new SchedulingDTO();
            try {
                boolean isValid = validatorService.validateSchedulingDTO(dto);
                assertTrue(isValid);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
  }


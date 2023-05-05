package br.edu.ifpb.dac.sape;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
@SelectClasses({br.edu.ifpb.dac.sape.controller.SportControllerTest.class, br.edu.ifpb.dac.sape.service.SportServiceTest.class,
	br.edu.ifpb.dac.sape.service.LoginConverterServiceTest.class,br.edu.ifpb.dac.sape.service.LoginServiceTest.class,br.edu.ifpb.dac.sape.model.dto.LoginDTOTest.class,
br.edu.ifpb.dac.sape.service.RoleServiceImplementationTest.class,br.edu.ifpb.dac.sape.service.ComparatorSchedulingDateTest.class})
@SelectPackages({"br.edu.ifpb.dac.sape.controller.sportControllerTest", "br.edu.ifpb.dac.sape.service.RoleServiceImplementationTest"
	, "br.edu.ifpb.dac.sape.model.user"})
@Suite
@SuiteDisplayName("Tests for functionalities implemented in Sprint 1")
class SchedulingSportPracticesApplicationTests {
	
}

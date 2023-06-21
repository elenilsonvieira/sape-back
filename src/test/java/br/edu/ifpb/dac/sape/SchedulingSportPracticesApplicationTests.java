package br.edu.ifpb.dac.sape;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectClasses({ br.edu.ifpb.dac.sape.system.OpenSiteTest.class,				 
				 br.edu.ifpb.dac.sape.system.PlaceCRUDSystemTest.class,
				 br.edu.ifpb.dac.sape.system.SportCRUDSystemTest.class,
				 br.edu.ifpb.dac.sape.system.SchedulingCRUDSystemTest.class,
				 br.edu.ifpb.dac.sape.system.ViewUserProfileSystemTest.class,
				 br.edu.ifpb.dac.sape.system.LoginSystemTest.class})
@SelectPackages({"br.edu.ifpb.dac.sape.system"})
@Suite
@SuiteDisplayName("Tests for functionalities implemented in Sprint 1")
class SchedulingSportPracticesApplicationTests {
	
}

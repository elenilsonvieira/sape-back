package br.edu.ifpb.dac.sape;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({"br.edu.ifpb.dac.sape.controller", "br.edu.ifpb.dac.sape.service"
	, "br.edu.ifpb.dac.sape.model"})
@Suite
@SuiteDisplayName("Tests for functionalities implemented in Sprint 1")
class SchedulingSportPracticesApplicationTests {
	
}

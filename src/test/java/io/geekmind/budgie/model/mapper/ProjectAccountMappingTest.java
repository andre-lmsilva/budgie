package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.project_account.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NewProjectAccountToProjectAccountMappingTest.class,
        ProjectAccountToNewProjectAccountMappingTest.class,
        ExistingStandardAccountToProjectAccountMappingTest.class,
        ProjectAccountToExistingStandardAccountMappingTest.class,
        ProjectAccountToEditProjectAccountMappingTest.class,
        EditProjectAccountToProjectAccountMappingTest.class,
        ProjectAccountToExistingProjectAccountMappingTest.class,
        ExistingProjectAccountToProjectAccountMappingTest.class
})
public class ProjectAccountMappingTest {
}

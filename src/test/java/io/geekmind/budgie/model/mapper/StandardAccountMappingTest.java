package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.standard_account.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NewStandardAccountToStandardAccountMappingTest.class,
        StandardAccountToNewStandardAccountMappingTest.class,
        StandardAccountToEditStandardAccountMappingTest.class,
        EditStandardAccountToStandardAccountMergingTest.class,
        StandardAccountToExistingStandardAccountMappingTest.class,
        ExistingStandardAccountToStandardAccountMappingTest.class
})
public class StandardAccountMappingTest {

}

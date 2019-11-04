package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.account.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NewAccountToAccountMappingTest.class,
        AccountToNewAccountMappingTest.class,
        AccountToEditAccountMappingTest.class,
        EditAccountToAccountMergingTest.class,
        AccountToExistingAccountMappingTest.class,
        ExistingAccountToAccountMappingTest.class
})
public class AccountMappingTest {

}

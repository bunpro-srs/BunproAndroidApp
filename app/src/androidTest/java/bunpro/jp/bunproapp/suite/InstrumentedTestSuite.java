package bunpro.jp.bunproapp.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({InstrumentedLoginTestSuite.class, InstrumentedLoggedInTestSuite.class, InstrumentedLogoutTestSuite.class})
public class InstrumentedTestSuite {
}

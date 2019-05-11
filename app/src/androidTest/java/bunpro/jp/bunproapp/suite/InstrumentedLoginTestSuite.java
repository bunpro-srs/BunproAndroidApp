package bunpro.jp.bunproapp.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bunpro.jp.bunproapp.LoginActivityTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({LoginActivityTest.class})
public class InstrumentedLoginTestSuite {
}

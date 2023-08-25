package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test325 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestAddPattern/Trace-1651091685089.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_111_220
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule4 = null;
        filterrule4 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule4.getPatternKeys();
        filterrule4.getPatternKeys();
        filterrule4.getName();
        filterrule4.setName("dummy");
        filterrule4.getDescription();
    }
}

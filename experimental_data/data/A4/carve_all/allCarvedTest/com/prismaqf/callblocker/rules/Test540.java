package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test540 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_174_346
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule3 = null;
        filterrule3 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule3.getPatternKeys();
        filterrule3.getPatternKeys();
        filterrule3.getName();
    }
}

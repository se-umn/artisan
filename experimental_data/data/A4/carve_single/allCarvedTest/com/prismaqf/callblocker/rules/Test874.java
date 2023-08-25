package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test874 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestChangeActionMissingOnCreate/Trace-1651091716735.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_194_383
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_001() throws Exception {
        java.lang.Object object0 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule6 = null;
        filterrule6 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule6.getPatternKeys();
        filterrule6.getPatternKeys();
        filterrule6.getName();
        filterrule6.setName("dummy");
        filterrule6.getDescription();
        filterrule6.setDescription("change me");
        filterrule6.equals(object0);
    }
}

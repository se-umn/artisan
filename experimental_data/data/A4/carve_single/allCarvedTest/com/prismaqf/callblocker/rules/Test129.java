package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test129 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_548_1091
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule6 = null;
        java.lang.Object object0 = null;
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

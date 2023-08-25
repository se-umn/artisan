package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test567 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#SmokeTest/Trace-1651091656890.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_118_232
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule5 = null;
        filterrule5 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule5.getPatternKeys();
        filterrule5.getPatternKeys();
        filterrule5.getName();
        filterrule5.setName("dummy");
        filterrule5.getDescription();
        filterrule5.setDescription("change me");
    }
}

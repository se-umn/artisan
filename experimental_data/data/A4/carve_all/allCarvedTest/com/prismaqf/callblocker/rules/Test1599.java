package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1599 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestRotationWithEmptyText/Trace-1651091704579.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_304_605
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule40 = null;
        filterrule40 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule40.getPatternKeys();
        filterrule40.getPatternKeys();
        filterrule40.setDescription("change me");
    }
}

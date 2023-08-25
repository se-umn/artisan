package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test597 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_679_1355
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule181 = null;
        filterrule181 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule181.getPatternKeys();
        filterrule181.getPatternKeys();
        filterrule181.setDescription("Description");
    }
}

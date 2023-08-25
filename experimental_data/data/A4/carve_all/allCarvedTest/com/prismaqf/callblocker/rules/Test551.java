package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test551 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_237_472
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_003() throws Exception {
        java.lang.Object object6 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule13 = null;
        filterrule13 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule13.getPatternKeys();
        filterrule13.getPatternKeys();
        filterrule13.getName();
        filterrule13.setName("dummy");
        filterrule13.getDescription();
        filterrule13.setDescription("change me");
        filterrule13.equals(object6);
        filterrule13.getPatternKeys();
        filterrule13.getPatternKeys();
        filterrule13.equals(object6);
        filterrule13.setName("Name");
        filterrule13.setDescription("Description");
        filterrule13.equals(object6);
    }
}

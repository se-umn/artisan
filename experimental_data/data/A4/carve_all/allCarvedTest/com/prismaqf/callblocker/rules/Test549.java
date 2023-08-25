package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test549 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_234_467
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_002() throws Exception {
        java.lang.Object object5 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule12 = null;
        filterrule12 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.getName();
        filterrule12.setName("dummy");
        filterrule12.getDescription();
        filterrule12.setDescription("change me");
        filterrule12.equals(object5);
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.equals(object5);
        filterrule12.setName("Name");
        filterrule12.setDescription("Description");
    }
}

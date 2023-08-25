package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1602 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestRotationWithEmptyText/Trace-1651091704579.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_356_710
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_002() throws Exception {
        java.lang.Object object27 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule43 = null;
        filterrule43 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule43.getPatternKeys();
        filterrule43.getPatternKeys();
        filterrule43.getName();
        filterrule43.setName("dummy");
        filterrule43.getDescription();
        filterrule43.setDescription("change me");
        filterrule43.equals(object27);
        filterrule43.getPatternKeys();
        filterrule43.getPatternKeys();
        filterrule43.equals(object27);
        filterrule43.setName("");
        filterrule43.getName();
        filterrule43.setName("");
        filterrule43.getDescription();
    }
}

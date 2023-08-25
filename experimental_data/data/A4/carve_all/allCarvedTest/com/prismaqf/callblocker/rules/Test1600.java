package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1600 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestRotationWithEmptyText/Trace-1651091704579.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_348_694
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_002() throws Exception {
        java.lang.Object object25 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule41 = null;
        filterrule41 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule41.getPatternKeys();
        filterrule41.getPatternKeys();
        filterrule41.getName();
        filterrule41.setName("dummy");
        filterrule41.getDescription();
        filterrule41.setDescription("change me");
        filterrule41.equals(object25);
        filterrule41.getPatternKeys();
        filterrule41.getPatternKeys();
        filterrule41.equals(object25);
        filterrule41.setName("");
        filterrule41.getName();
    }
}

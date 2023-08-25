package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test216 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_599_1196
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule12 = null;
        java.lang.Object object5 = null;
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
        filterrule12.setName("New rule");
        filterrule12.getName();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test502 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestSaveAction/Trace-1651091701954.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_194_383
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_001() throws Exception {
        java.lang.Object object0 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule7 = null;
        filterrule7 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule7.getPatternKeys();
        filterrule7.getPatternKeys();
        filterrule7.getName();
        filterrule7.setName("dummy");
        filterrule7.getDescription();
        filterrule7.setDescription("change me");
        filterrule7.equals(object0);
    }
}

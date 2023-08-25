package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1114 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLog/Trace-1651091669275.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_142_280
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_002() throws Exception {
        java.lang.Object object3 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule10 = null;
        filterrule10 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule10.getPatternKeys();
        filterrule10.getPatternKeys();
        filterrule10.getName();
        filterrule10.setName("dummy");
        filterrule10.getDescription();
        filterrule10.setDescription("change me");
        filterrule10.equals(object3);
        filterrule10.getPatternKeys();
        filterrule10.getPatternKeys();
        filterrule10.equals(object3);
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1110 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLog/Trace-1651091669275.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_118_232
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule6 = null;
        filterrule6 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule6.getPatternKeys();
        filterrule6.getPatternKeys();
        filterrule6.getName();
        filterrule6.setName("dummy");
        filterrule6.getDescription();
        filterrule6.setDescription("change me");
    }
}

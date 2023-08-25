package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test820 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdateActionValidation/Trace-1651091659983.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_111_220
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule5 = null;
        filterrule5 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule5.getPatternKeys();
        filterrule5.getPatternKeys();
        filterrule5.getName();
        filterrule5.setName("dummy");
        filterrule5.getDescription();
    }
}

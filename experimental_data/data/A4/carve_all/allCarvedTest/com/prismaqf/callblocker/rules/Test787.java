package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test787 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestEmptyNameShouldFlag/Trace-1651091718040.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_132_262
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule2 = null;
        filterrule2 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule2.getPatternKeys();
        filterrule2.getPatternKeys();
    }
}

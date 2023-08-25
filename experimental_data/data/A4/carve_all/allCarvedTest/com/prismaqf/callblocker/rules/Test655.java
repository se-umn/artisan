package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test655 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestUsedNameShouldFlag/Trace-1651091703166.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_222_443
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_002() throws Exception {
        java.lang.Object object4 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule11 = null;
        filterrule11 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule11.getPatternKeys();
        filterrule11.getPatternKeys();
        filterrule11.getName();
        filterrule11.setName("dummy");
        filterrule11.getDescription();
        filterrule11.setDescription("change me");
        filterrule11.equals(object4);
        filterrule11.getPatternKeys();
        filterrule11.getPatternKeys();
        filterrule11.equals(object4);
        filterrule11.setName("first");
    }
}

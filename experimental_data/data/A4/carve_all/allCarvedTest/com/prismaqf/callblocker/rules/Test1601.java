package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1601 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestRotationWithEmptyText/Trace-1651091704579.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_351_698
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_003() throws Exception {
        java.lang.Object object26 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule42 = null;
        filterrule42 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule42.getPatternKeys();
        filterrule42.getPatternKeys();
        filterrule42.getName();
        filterrule42.setName("dummy");
        filterrule42.getDescription();
        filterrule42.setDescription("change me");
        filterrule42.equals(object26);
        filterrule42.getPatternKeys();
        filterrule42.getPatternKeys();
        filterrule42.equals(object26);
        filterrule42.setName("");
        filterrule42.getName();
        filterrule42.setName("");
    }
}

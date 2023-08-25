package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1603 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestRotationWithEmptyText/Trace-1651091704579.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_363_722
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_003() throws Exception {
        java.lang.Object object28 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule44 = null;
        filterrule44 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule44.getPatternKeys();
        filterrule44.getPatternKeys();
        filterrule44.getName();
        filterrule44.setName("dummy");
        filterrule44.getDescription();
        filterrule44.setDescription("change me");
        filterrule44.equals(object28);
        filterrule44.getPatternKeys();
        filterrule44.getPatternKeys();
        filterrule44.equals(object28);
        filterrule44.setName("");
        filterrule44.getName();
        filterrule44.setName("");
        filterrule44.getDescription();
        filterrule44.setDescription("change me");
    }
}

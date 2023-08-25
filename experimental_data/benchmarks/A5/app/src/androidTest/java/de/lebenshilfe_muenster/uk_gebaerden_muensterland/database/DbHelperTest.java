package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.util.Log;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.FOOTBALL_SIGN;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.INITIAL_NUMBER_OF_SIGNS;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAMA_SIGN;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.PAPA_SIGN;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.TEST_SIGN;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@SuppressWarnings("unused")
@RunWith(AndroidJUnit4.class)
public class DbHelperTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private final SignDAO signDAO = SignDAO.getInstance(mainActivityActivityTestRule.launchActivity(null));

    @Before
    public void openDatabase() {
        signDAO.open();
    }

    @After
    public void closeDatabase() {
        signDAO.close();
    }

    @Test
    public void testCreatedSignEqualsInsertedSign() {
        final Sign sign = signDAO.create(TEST_SIGN);
        assertThat(sign, is(equalTo(TEST_SIGN)));
        signDAO.delete(sign);
    }

    @Test
    public void testDuplicateSignViolatesTableConstraint() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(allOf(startsWith("Inserting sign"), endsWith("a database error!")));
        signDAO.create(FOOTBALL_SIGN);
    }

    // @Test - disabled because the app is shipped with an existing database and this test is very slow.
    public void testCreatingManySigns() {
        final List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            final String name = "sign_" + i;
            signs.add(new Sign.Builder().setId(0).setName(name).setNameLocaleDe(name + "_de")
                    .setMnemonic(name + "_mnemonic").setStarred(false).setLearningProgress(0).create());

        }
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        signDAO.create(signs);
        stopWatch.stop();
        Log.d(DbHelperTest.class.getSimpleName(), "Creating the signs took " + stopWatch.getTime() + " milliseconds");
        stopWatch.reset();
        stopWatch.start();
        final List<Sign> signsFromDb = signDAO.read();
        stopWatch.stop();
        Log.d(DbHelperTest.class.getSimpleName(), "Reading the signs took " + stopWatch.getTime() + " milliseconds");
        signsFromDb.remove(FOOTBALL_SIGN);
        signsFromDb.remove(MAMA_SIGN);
        signsFromDb.remove(PAPA_SIGN);
        assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[0])));
        signDAO.delete(signs);
    }

    @Test
    public void testReadReturnsList() {
        List<Sign> signsFromDb = signDAO.read();
        assertThat(signsFromDb.size(), greaterThanOrEqualTo(INITIAL_NUMBER_OF_SIGNS));
        assertThat(MAMA_SIGN, isIn(signsFromDb));
        assertThat(PAPA_SIGN, isIn(signsFromDb));
        assertThat(FOOTBALL_SIGN, isIn(signsFromDb));
    }

    @Test
    public void testReadWithNameLocaleDeReturnsSingleResult() {
        List<Sign> signs = new ArrayList<>();
        signs.add(MAMA_SIGN);
        List<Sign> signsFromDb = signDAO.read("mam");
        assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[0])));
    }

    @Test
    public void testReadWithTagLocalDeReturnsResults() {
        // when: a tag is searched
        List<Sign> signsFromDb = signDAO.read(TestConstants.MAMA_TAG_PERSON);

        // then: is the expected sign among the list of returned signs
        assertThat(signsFromDb, hasItem(MAMA_SIGN));
    }

    @Test
    public void testReadWithIllegalNameLocaleDeReturnsNoResult() {
        List<Sign> signsFromDb = signDAO.read("foobar");
        assertThat(signsFromDb, is(empty()));
    }

    @Test
    public void testReadWithStarredOnlyReturnsCorrectResult() {
        Sign createdSign = null;
        try {
            createdSign = signDAO.create(TEST_SIGN);
            createdSign.setStarred(true);
            final Sign updatedSign = signDAO.update(createdSign);
            List<Sign> signs = new ArrayList<>();
            signs.add(updatedSign);
            List<Sign> signsFromDb = signDAO.readStarredSignsOnly();
            assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[0])));
        } finally {
            if (null != createdSign) {
                signDAO.delete(createdSign);
            }
        }
    }

    @Test
    public void testReadRandomSignReturnsNonNullValue() {
        final Sign randomSign = signDAO.readRandomSign(null);
        assertThat(randomSign, is(notNullValue()));
    }

    @Test
    public void testReadRandomSignReturnsNotTheCurrentSign() {
        for (int i = 0; i < 100; i++) {
            final Sign randomSign = signDAO.readRandomSign(FOOTBALL_SIGN);
            assertThat(randomSign, not(is(equalTo(FOOTBALL_SIGN))));
        }
    }

    @Test
    public void testReadRandomSignReturnsSignsOrderedByLearningProgress() {
        // setup
        final List<Sign> signs = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = -5; j < 6; j++) {
                final String name = "Test_Sign_" + k;
                signs.add(new Sign.Builder().setId(0).setName(name).setNameLocaleDe(name + "_de")
                        .setMnemonic(name + "_mnemonic").setTags(name + "_tags").setStarred(false).setLearningProgress(j).create());
                k++;
            }
        }
        try {
            signDAO.create(signs);
            final List<Sign> signsFromDbBeforeTest = getTestSigns();
            assertThat(signsFromDbBeforeTest, containsInAnyOrder(signs.toArray(new Sign[0])));
            // do the test
            final Sign firstSign = signDAO.readRandomSign(null);
            assertThat(firstSign.getLearningProgress(), is(equalTo(-5)));

            final Sign secondSign = signDAO.readRandomSign(firstSign);
            assertSignIsNotEqualToPreviousSignButHasSameLearningProgress(firstSign, secondSign);

            Sign thirdSign = signDAO.readRandomSign(secondSign);
            assertSignIsNotEqualToPreviousSignButHasSameLearningProgress(secondSign, thirdSign);
            thirdSign.increaseLearningProgress();
            thirdSign = signDAO.update(thirdSign);

            Sign fourthSign = signDAO.readRandomSign(thirdSign);
            assertSignIsNotEqualToPreviousSignButHasSameLearningProgress(thirdSign, fourthSign);
            fourthSign.increaseLearningProgress();
            fourthSign = signDAO.update(fourthSign);

            Sign fifthSign = signDAO.readRandomSign(fourthSign);
            assertSignIsNotEqualToPreviousSignAndHasLessLearningProgress(fourthSign, fifthSign);
            fifthSign.increaseLearningProgress();
            fifthSign = signDAO.update(fifthSign);

            Sign sixthSign = signDAO.readRandomSign(fifthSign);
            assertSignIsNotEqualToPreviousSignAndHasLessLearningProgress(fifthSign, sixthSign);

            Sign seventhSign = signDAO.readRandomSign(sixthSign);
            assertThat(seventhSign, (is(equalTo(firstSign))));

        } finally {
            signDAO.delete(signs);
        }

    }

    @NonNull
    private List<Sign> getTestSigns() {
        final List<Sign> signsFromDb = signDAO.read();
        final List<Sign> nonTestSigns = new ArrayList<>();
        for (Sign sign : signsFromDb) {
            if (!sign.getName().startsWith("Test_Sign_")) {
                nonTestSigns.add(sign);
            }
        }
        signsFromDb.removeAll(nonTestSigns);
        return signsFromDb;
    }

    private void assertSignIsNotEqualToPreviousSignButHasSameLearningProgress(Sign previousSign, Sign currentSign) {
        assertThat(currentSign, (not(is(equalTo(previousSign)))));
        assertThat(currentSign.getLearningProgress(), is(equalTo(previousSign.getLearningProgress())));
    }

    private void assertSignIsNotEqualToPreviousSignAndHasLessLearningProgress(Sign previousSign, Sign currentSign) {
        assertThat(currentSign, (not(is(equalTo(previousSign)))));
        assertThat(currentSign.getLearningProgress(), is(lessThan(previousSign.getLearningProgress())));
    }

    @Test
    @Ignore
    public void testGetRandomSignDoesNotRetrieveAnyOfTheLastFiveSigns() {
        final String name = "Test_Sign_" + UUID.randomUUID();
        final Sign testSignA = new Sign.Builder().setId(0).setName("name").setNameLocaleDe(name + "_de")
                .setMnemonic(name + "_mnemonic").setTags(name + "_tags").setStarred(false).setLearningProgress(-5).create();
        try {
            signDAO.create(testSignA);
            Sign randomSign = signDAO.readRandomSign(testSignA);
            assertThat(randomSign, not(is(equalTo(testSignA))));
            for (int i = 0; i < 4; i++) {
                randomSign = signDAO.readRandomSign(randomSign);
                assertThat(String.format("Random sign on iteration %s equals created test sign.", i), randomSign, not(is(equalTo(testSignA))));
            }
            final Sign signShouldBeEqualToTestSignA = signDAO.readRandomSign(randomSign);
            assertThat(signShouldBeEqualToTestSignA, (is(equalTo(testSignA))));
        } finally {
            signDAO.delete(testSignA);
        }

    }


    @Test
    public void testDelete() {
        final Sign sign = signDAO.create(TEST_SIGN);
        final int numberOfSignsBefore = signDAO.read().size();
        signDAO.delete(sign);
        final int numberOfSignsAfter = signDAO.read().size();
        if (numberOfSignsAfter != (numberOfSignsBefore - 1)) {
            fail("Deleted sign, but reading returns a list with the same size");
        }
    }

    @Test
    public void testUpdateChangesLearningProgress() {
        final Sign createdSign = signDAO.create(TEST_SIGN);
        createdSign.increaseLearningProgress();
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.getLearningProgress(), is(equalTo(createdSign.getLearningProgress())));
        signDAO.delete(createdSign);
    }

    @Test
    public void testUpdateChangesStarred() {
        final Sign createdSign = signDAO.create(TEST_SIGN);
        createdSign.setStarred(true);
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.isStarred(), is(equalTo(true)));
        signDAO.delete(createdSign);
    }

    @Test
    public void testUpdateThrowsExceptionOnIllegalId() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(allOf(startsWith("Updating sign"), endsWith("no rows!")));
        final Sign illegalSign = new Sign.Builder().setId(Integer.MAX_VALUE).setName("football").setNameLocaleDe("Fußball")
                .setMnemonic("Kick a ball").setTags("tags").setStarred(false).setLearningProgress(0).create();
        signDAO.update(illegalSign);
    }

}

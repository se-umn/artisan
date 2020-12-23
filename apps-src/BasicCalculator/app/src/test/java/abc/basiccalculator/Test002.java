package abc.basiccalculator;

import android.text.Editable;
import android.widget.EditText;

import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowTextView;

import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {EditTextShadow.class})
public class Test002 {

    @Test(timeout = 4000)
    public void test1() throws Exception {
        try {
            // Setup the activity as expected by robolectric
            org.robolectric.android.controller.ActivityController<abc.basiccalculator.MainActivity> mainActivityController2 = null;
            abc.basiccalculator.MainActivity mainactivity2 = null;
            // Create the activity and start it. This ensures that GUI elements and the like are prepared
            android.os.Bundle bundle2 = null;
            mainActivityController2 = org.robolectric.Robolectric.buildActivity(abc.basiccalculator.MainActivity.class);
            mainactivity2 = mainActivityController2.get();
            mainActivityController2.create(bundle2);
            
            /**
             * Setup the mocks as usual. Note that we might need to transitively mocked complex objects if they are 
             * returned by any of our mocks.
             */

            /**
             * First: mock the Editable which is returned by inputField.getText() inside MainActivity.
             * Make sure this mocks produces the string "13" (observed from the trace) by invoking toString()
             */
            Editable mockedEditable = Mockito.mock(Editable.class);
            doReturn("13").when(mockedEditable).toString();

            /**
             * Second: mock the EditText which is returned by findViewById() inside MainActivity.
             * Configure the mock to return the previously mocked Editable.
             */
            EditText mockedEditText = Mockito.mock( EditText.class );
            doReturn(mockedEditable).when(mockedEditText).getText();

            /**
             * Third: access the "real" EditText using the same used by findViewById() inside MainActivity.
             * Extract this element as Shadow (EditTextShadow), which enables to "attach" a mock to some method
             * call. In this case, we attach the mock to the getText method.
             * 
             * This is basically how we hook our mock to the instance of android specific elements, like Views.
             * 
             * TODO: This is just a reference solution, there might be better strategies here...
             */
            android.widget.EditText editText = mainactivity2.findViewById(2131165326);
            EditTextShadow editTextShadow = Shadow.extract( editText );
            editTextShadow.setMockFor("getText", mockedEditText );

            // Here we can use regular mocks or simply get the element by ID (which is more clear IMHO)
            android.widget.Button button0 = mainactivity2.findViewById(2131165267);

            // Finally, invoke the method under test
            mainactivity2.sendResult(button0);

            org.junit.Assert.fail("Expected exception was not thrown!");
        } catch (java.lang.IllegalArgumentException expected) {
            expected.printStackTrace();
        } catch (java.lang.Throwable unexpected) {
            unexpected.printStackTrace();
            org.junit.Assert.fail("A unexpected exception was thrown!");

        }
    }
}

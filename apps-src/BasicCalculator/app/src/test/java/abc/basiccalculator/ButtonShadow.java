package abc.basiccalculator;

import android.widget.Button;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import org.robolectric.shadows.ShadowTextView;

import java.util.HashMap;
import java.util.Map;

@Implements(Button.class)
public class ButtonShadow extends ShadowTextView {

    private Map<String, Button> mockedCalls = new HashMap<>();

    public void setMockFor(String methodName, Button mockedInstance){
        mockedCalls.put(methodName, mockedInstance);
    }
}

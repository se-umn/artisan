package abc.basiccalculator;

import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Stores programmatically created UI elements. Thus, they can be seamlessly used by multiple
 * activities.
 */
public class UiStorage {
    private static List<View> viewList = new ArrayList<>();

    public static void addElement(View uiElement) {
        viewList.add(uiElement);
    }

    public static void addAllElements(Collection<View> uiElements) {
        viewList.addAll(uiElements);
    }

    public static List<View> getElements() {
        return viewList;
    }

    public static void clear() {
        viewList.clear();
    }
}

package fr.android.foottracker.utils;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public final class TagUtil {

    private TagUtil() {
    }

    public static <T extends View> List<T> getTaggedElements(ViewGroup rootElement, String discriminatingTag) {
        final List<T> foundTaggedElements = new ArrayList<>();
        final int childCount = rootElement.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View currentChild = rootElement.getChildAt(i);
            traverseLayout(discriminatingTag, foundTaggedElements, currentChild);
            checkViewTag(discriminatingTag, foundTaggedElements, currentChild);
        }
        return foundTaggedElements;
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> void checkViewTag(String discriminatingTag, List<T> foundTaggedElements, View currentChild) {
        final Object childTag = currentChild.getTag();
        if (childTag != null && childTag.equals(discriminatingTag))
            foundTaggedElements.add((T) currentChild);
    }

    private static <T extends View> void traverseLayout(String discriminatingTag, List<T> foundTaggedElements, View currentChild) {
        if (currentChild instanceof ViewGroup) {
            foundTaggedElements.addAll(getTaggedElements((ViewGroup) currentChild, discriminatingTag));
        }
    }

}

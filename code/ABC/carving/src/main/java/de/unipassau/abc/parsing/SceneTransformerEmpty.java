package de.unipassau.abc.parsing;

import soot.Scene;
import soot.SceneTransformer;

import java.util.Map;

public class SceneTransformerEmpty extends SceneTransformer {

    @Override
    protected void internalTransform(String s, Map<String, String> map) {
        System.out.println("SceneTransformerEmpty");
        Scene.v().loadNecessaryClasses();
    }
}

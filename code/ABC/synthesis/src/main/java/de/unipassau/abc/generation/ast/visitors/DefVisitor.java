package de.unipassau.abc.generation.ast.visitors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class DefVisitor extends VoidVisitorAdapter<Object> {

    private Set<String> declaredVars = new HashSet<>();

    @Override
    public void visit(VariableDeclarationExpr expr, Object arg){
        NodeList<VariableDeclarator> vds = expr.getVariables();
        for(VariableDeclarator vd:vds){
            declaredVars.add(vd.getNameAsString());
        }
    }

    public Set<String> getDeclaredVars(){
        return declaredVars;
    }
}

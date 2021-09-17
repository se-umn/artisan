package de.unipassau.abc.generation.ast.visitors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class MethodCallExprVisitor extends VoidVisitorAdapter<Object> {

    private MethodCallExpr mce;

    @Override
    public void visit(MethodCallExpr expr, Object arg){
       mce = expr;
    }

    public MethodCallExpr getMethodCallExpr(){
        return mce;
    }
}

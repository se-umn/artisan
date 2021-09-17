package de.unipassau.abc.generation.ast.visitors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class UseVisitor extends VoidVisitorAdapter<String> {

    private boolean used = false;

    @Override
    public void visit(NameExpr expr, String varName){
        if(expr.getNameAsString().equals(varName)){
            used = true;
        }
    }

    public boolean isUsed(){
        return used;
    }
}

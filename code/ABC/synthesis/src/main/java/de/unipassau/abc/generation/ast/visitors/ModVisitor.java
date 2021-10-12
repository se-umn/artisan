package de.unipassau.abc.generation.ast.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ModVisitor extends ModifierVisitor<String> {

    private static final Logger logger = LoggerFactory.getLogger(ModVisitor.class);

    @Override
    public Node visit(VariableDeclarationExpr expr, String varName){
        boolean declaresRelevantVar = false;
        boolean nullAssignment = false;
        MethodCallExpr mce = null;
        NodeList<VariableDeclarator> vds = expr.getVariables();
        for(VariableDeclarator vd:vds){
            if(vd.getNameAsString().equals(varName)){
                declaresRelevantVar = true;
                Optional<Expression> initExprOpt = vd.getInitializer();
                if(initExprOpt.isPresent()){
                    Expression initExpr = initExprOpt.get();
                    if(initExpr.isNullLiteralExpr()){
                        nullAssignment = true;
                    }
                    else {
                        MethodCallExprVisitor mcev = new MethodCallExprVisitor();
                        initExpr.accept(mcev, null);
                        mce = mcev.getMethodCallExpr();
                    }
                }
            }
        }
        if(declaresRelevantVar && nullAssignment){
            logger.debug("Removed:"+expr);
            return null;
        }
        else if(declaresRelevantVar && mce!=null){
            logger.info("Changed:"+expr+" to:"+mce);
            return mce;
        }
        return expr;
    }
}

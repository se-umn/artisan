package de.unipassau.abc.generation;

import static com.github.javaparser.JavaParser.parseVariableDeclarationExpr;

import java.io.File;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jboss.util.NotImplementedException;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.ClassConstant;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;

public class TestCaseFactory {

	private final static Logger logger = LoggerFactory.getLogger(TestCaseFactory.class);

	public static NodeList<Expression> convertSootInvocationArguments(List<Value> args) {
		NodeList<Expression> arguments = new NodeList<>();
		for (Value argument : args) {
			// Values can be constant, locals, fields?
			// Maybe it would be enough String.valueOf( arg ) ?
			// TODO Char?
			if (argument instanceof Constant) {
				if (argument instanceof ClassConstant) {
					arguments.add(new NameExpr(argument.toString()));
				} else if (argument instanceof NullConstant) {
					arguments.add(new NullLiteralExpr());
				} else if (argument instanceof IntConstant) {
					arguments.add(new IntegerLiteralExpr(argument.toString()));
				} else if (argument instanceof LongConstant) {
					arguments.add(new LongLiteralExpr(argument.toString()));
				} else if (argument instanceof DoubleConstant) {
					arguments.add(new DoubleLiteralExpr(argument.toString()));
				} else if (argument instanceof FloatConstant) {
					arguments.add(new DoubleLiteralExpr(argument.toString()));
				} else if (argument instanceof StringConstant) {
					arguments.add(new StringLiteralExpr(argument.toString()));
				} else {
					System.out.println("WARNING CANNOT handle Constant " + argument);
					arguments.add(new NameExpr(argument.toString()));
				}
			} else if (argument instanceof Local) {
				arguments.add(new NameExpr(((Local) argument).getName()));
			} else if (argument instanceof ConcreteRef) {
				if (argument instanceof ArrayRef) {
					ArrayRef arrayRef = (ArrayRef) argument;
					arguments.add(new ArrayAccessExpr(new NameExpr(arrayRef.getBase().toString()),
							new IntegerLiteralExpr(arrayRef.getIndex().toString())));
				} else if (argument instanceof FieldRef) {
					FieldRef fieldRef = (FieldRef) argument;

					System.out.println(
							"TestCaseFactory.convertSootInvocationArguments() CANNOT HANDLE FIELD REF " + fieldRef);
					// How do I access the ref to instance ?!
					// arguments.add( new FieldAccessExpr(new NameExpr(
					// fieldRef.getFieldRef().
					// , new NameExpr( fieldRef.getField().getName())));

				} else {
					System.out.println("Cannot handle argument " + argument);
				}
			} else {
				System.out.println("Cannot handle argument " + argument);
			}
		}
		return arguments;
	}

	public static Expression convertSootInvocationExpr(InvokeExpr invokeExpr, Body body) {
		NodeList<Expression> arguments = convertSootInvocationArguments(invokeExpr.getArgs());
		//
		Expression scope = null;
		//
		String name = invokeExpr.getMethodRef().name();
		if (invokeExpr instanceof VirtualInvokeExpr || invokeExpr instanceof InterfaceInvokeExpr) {
			// We cannot use the super type "InstanceInvokeExpr"
			scope = new NameExpr(((InstanceInvokeExpr) invokeExpr).getBase().toString());
		} else if (invokeExpr instanceof StaticInvokeExpr) {
			// Not sure this is correct
			scope = new NameExpr(invokeExpr.getMethodRef().declaringClass().getName());
		} else if (invokeExpr instanceof DynamicInvokeExpr) {
			System.out.println("NOT SURE WHAT TO DO WITH A DynamicInvokeExpr");
			return null;
		}

		if (invokeExpr instanceof SpecialInvokeExpr) {

			if (((InstanceInvokeExpr) invokeExpr).getBase().equals(body.getThisLocal())) {
				// Object.INIT is a call to super. Superflous in our case
				if (invokeExpr.getMethodRef().declaringClass().equals(Scene.v().getSootClass("java.lang.Object"))) {
					return null;
				}

			}
			// INIIT 0 TODO
			// scope = new NameExpr(((InstanceInvokeExpr)
			// invokeExpr).getBase().toString());
			ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType();
			classOrInterfaceType.setName(((InstanceInvokeExpr) invokeExpr).getBase().getType().toString());
			return new ObjectCreationExpr(scope, classOrInterfaceType, arguments);
		} else {
			return new MethodCallExpr(scope, name, arguments);
		}
	}

	public static void generateTestFiles(File outputDir, Collection<SootClass> testClasses) {

		Options.v().set_output_dir(outputDir.getAbsolutePath());

		logger.debug("Output directory is " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		for (SootClass testClass : testClasses) {
			converSootClassToJavaClass(testClass);
		}
	}

	private final static EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);

	public static void converSootClassToJavaClass(SootClass sootClass) {
		// https://github.com/javaparser/javaparser/wiki/Manual
		logger.info("TestCaseFactory.generateTestFiles() " + sootClass.getName());

		CompilationUnit cu = new CompilationUnit();
		cu.setPackageDeclaration(sootClass.getPackageName());

		ClassOrInterfaceDeclaration myClass = cu.addClass(sootClass.getShortName());
		// myClass.setComment(new LineComment("Class generated with ABC from
		// system test XXX!"));
		myClass.setModifiers(modifiers);

		for (SootField field : sootClass.getFields()) {
			// Ideally we should keep the modifiers as they are, but ATM they
			// are just public
			// field.getModifiers()

			FieldDeclaration fieldDec = myClass.addField(field.getType().toString(), field.getName());
			fieldDec.setModifiers(modifiers);

			// The Jimple parsed cannot handle annotations so ATM we simply
			// impose them
			fieldDec.addAnnotation(Rule.class);

		}

		for (SootMethod method : sootClass.getMethods()) {

			CallableDeclaration genericMethodDeclaration = null;
			if (method.getName().equals("<init>")) {
				genericMethodDeclaration = new ConstructorDeclaration(modifiers, sootClass.getShortName());
				myClass.addMember(genericMethodDeclaration);
			} else {
				genericMethodDeclaration = new MethodDeclaration(modifiers, new VoidType(), method.getName());
				myClass.addMember(genericMethodDeclaration);

				genericMethodDeclaration.addAnnotation(Test.class);
				genericMethodDeclaration.addThrownException(Exception.class);
			}

			// https://www.javaadvent.com/2017/12/javaparser-generate-analyze-modify-java-code.html

			BlockStmt methodBody = new BlockStmt();
			// Insert local variables
			for (Local local : method.getActiveBody().getLocals()) {

				if (local.equals(method.getActiveBody().getThisLocal())) {
					continue;
				}

				// That's naive but might work ...
				VariableDeclarationExpr variableDeclaration = parseVariableDeclarationExpr(
						local.getType().toString() + " " + local.getName());
				methodBody.addStatement(variableDeclaration);
			}

			final Body body = method.getActiveBody();
			// Insert statement. Thos are ONLY assignments or invocations
			for (final Unit unit : body.getUnits()) {
				System.out.println("TestCaseFactory.generateTestFiles() Processing unit " + unit);
				unit.apply(new AbstractStmtSwitch() {
					@Override
					public void caseBreakpointStmt(BreakpointStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseGotoStmt(GotoStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseIdentityStmt(IdentityStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseIfStmt(IfStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseNopStmt(NopStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseRetStmt(RetStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseReturnStmt(ReturnStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseTableSwitchStmt(TableSwitchStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseThrowStmt(ThrowStmt stmt) {
						System.out.println("\t Skip");
					}

					@Override
					public void caseAssignStmt(AssignStmt stmt) {
						Expression leftExpr = null;
						Expression rightExpr = null;

						// This requires for sure to differentiate among
						// plain, field, and array.
						if (stmt.getLeftOp() instanceof FieldRef) {
							FieldRef fieldRef = (FieldRef) stmt.getLeftOp();
							leftExpr = new FieldAccessExpr(
									new NameExpr(((InstanceFieldRef) stmt.getLeftOp()).getBase().toString()),
									fieldRef.getFieldRef().name());
						} else if (stmt.getLeftOp() instanceof StaticFieldRef) {
							throw new NotImplementedException();
							// System.out.println(
							// "TestCaseFactory.generateTestFiles() ACCESS
							// TO STATIC FIELDS NOT IMPLEMENTED ");
						} else if (stmt.getLeftOp() instanceof ArrayRef) {
							ArrayRef arrayRef = (ArrayRef) stmt.getLeftOp();
							leftExpr = new ArrayAccessExpr(new NameExpr(arrayRef.getBase().toString()),
									new IntegerLiteralExpr(arrayRef.getIndex().toString()));
						} else {
							// Local
							leftExpr = new NameExpr(stmt.getLeftOp().toString());
						}

						/// Right side
						if (stmt.getRightOp() instanceof InstanceFieldRef) {
							// throw new NotImplementedException();
							FieldRef fieldRef = (FieldRef) stmt.getRightOp();

							rightExpr = new FieldAccessExpr(
									new NameExpr(((InstanceFieldRef) stmt.getRightOp()).getBase().toString()),
									fieldRef.getFieldRef().name());
						} else if (stmt.getRightOp() instanceof StaticFieldRef) {
							StaticFieldRef fieldRef = (StaticFieldRef) stmt.getRightOp();

							rightExpr = new NameExpr(fieldRef.getFieldRef().declaringClass()+"."+fieldRef.getFieldRef().name());
							
						} else if (stmt.getRightOp() instanceof ArrayRef) {
							ArrayRef arrayRef = (ArrayRef) stmt.getRightOp();

							rightExpr = new ArrayAccessExpr(new NameExpr(arrayRef.getBase().toString()),
									new IntegerLiteralExpr(arrayRef.getIndex().toString()));
						} else if (stmt.getRightOp() instanceof InvokeExpr) {
							rightExpr = convertSootInvocationExpr((InvokeExpr) stmt.getRightOp(), body);
						} else if (stmt.getRightOp() instanceof NewExpr) {
							// Do not process soot's "new"
							return;
						} else if (stmt.getRightOp() instanceof NewArrayExpr) {
							NewArrayExpr newArrayExpr = (NewArrayExpr) stmt.getRightOp();
							// TODO Convert type by type !
							// Primitive type, ClassOrInterfaceType type,
							// ArrayType
							// PrimitiveType.booleanType();
							ArrayCreationLevel it = new ArrayCreationLevel(
									new IntegerLiteralExpr(newArrayExpr.getSize().toString()));
							Type elementType = new ClassOrInterfaceType(null, newArrayExpr.getBaseType().toString());
							rightExpr = new ArrayCreationExpr(elementType)
									.setLevels(new NodeList<ArrayCreationLevel>(it)).setInitializer(null);

						} else {
							// NOT SURE... constants ?
							rightExpr = new NameExpr(stmt.getRightOp().toString());
						}

						if (rightExpr == null || leftExpr == null) {
							System.out.println("null expression. Skip " + leftExpr + " == " + rightExpr);
							return;
						}

						ExpressionStmt e = new ExpressionStmt(
								new AssignExpr(leftExpr, rightExpr, AssignExpr.Operator.ASSIGN));

						System.out.println("caseAssignStmt()" + stmt + " --> " + e);

						methodBody.addStatement(e);
					}

					@Override
					public void caseInvokeStmt(InvokeStmt stmt) {
						InvokeExpr invokeExpr = (InvokeExpr) stmt.getInvokeExpr();
						Expression e = convertSootInvocationExpr(invokeExpr, body);

						if (e == null) {
							// this might happen for Object<init>
							System.out.println("\t Skip");
							return;
						}
						if (invokeExpr instanceof SpecialInvokeExpr) {
							// This must become an Assign
							// TODO Check this is actually an <init> and not
							// a <clinit>
							methodBody.addStatement(new ExpressionStmt(
									new AssignExpr(new NameExpr(((SpecialInvokeExpr) invokeExpr).getBase().toString()),
											e, AssignExpr.Operator.ASSIGN)));
						} else {
							methodBody.addStatement(e);
						}
						System.out.println("caseInvokeStmt() " + stmt + " --> " + e);
					}
				});
			}

			if (genericMethodDeclaration instanceof ConstructorDeclaration) {
				((ConstructorDeclaration) genericMethodDeclaration).setBody(methodBody);
			} else if (genericMethodDeclaration instanceof MethodDeclaration) {
				((MethodDeclaration) genericMethodDeclaration).setBody(methodBody);
			}

		}
		System.out.println(cu);

	}
}

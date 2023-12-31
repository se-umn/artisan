package de.unipassau.abc.generation.impl;

import static com.github.javaparser.JavaParser.parseVariableDeclarationExpr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import de.unipassau.abc.data.JimpleUtils;
import soot.Body;
import soot.BooleanType;
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

public class TestCaseFactory {

	private final static Logger logger = LoggerFactory.getLogger(TestCaseFactory.class);

	private final static Map<Expression, ResolvedType> typeCache = new HashMap<>();

	public static NodeList<Expression> convertSootInvocationArguments(InvokeExpr invokeExpr) {
		//
		// List<Value> args
		NodeList<Expression> arguments = new NodeList<>();

		for (int i = 0; i < invokeExpr.getArgCount(); i++) {
			Value argument = invokeExpr.getArg(i);
			soot.Type type = invokeExpr.getMethodRef().parameterType(i);

			if (type.equals(BooleanType.v())) {
				if ("0".equals(argument.toString())) {
					arguments.add(new BooleanLiteralExpr(false));
				} else {
					arguments.add(new BooleanLiteralExpr(true));
				}
				continue;
			}

			// Values can be constant, locals, fields?
			// Maybe it would be enough String.valueOf( arg ) ?
			// TODO Char?
			// Pay Attention to the booleans

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
					arguments.add(new NameExpr(argument.toString())
					// new StringLiteralExpr(argument.toString())// This
					// generates "" and ""
					);
				} else {
					logger.warn("CANNOT handle Constant " + argument);
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
					// This should not be necessary since tests use only locals
					logger.warn("TestCaseFactory.convertSootInvocationArguments() CANNOT HANDLE FIELD REF " + fieldRef);
					// How do I access the ref to instance ?!
					// arguments.add( new FieldAccessExpr(new NameExpr(
					// fieldRef.getFieldRef().
					// , new NameExpr( fieldRef.getField().getName())));

				} else {
					logger.info("Cannot handle argument " + argument);
				}
			} else {
				logger.info("Cannot handle argument " + argument);
			}
		}
		return arguments;
	}

	public static Expression convertSootInvocationExpr(InvokeExpr invokeExpr, Body body) {

		NodeList<Expression> arguments = convertSootInvocationArguments(invokeExpr);
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
			logger.info("NOT SURE WHAT TO DO WITH A DynamicInvokeExpr");
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

	/**
	 * This might take ages because of type solver ?
	 * 
	 * @param projectJars
	 * @param testClasses
	 * @param resolveTypes
	 * @return
	 * @throws IOException
	 */
	public static Set<CompilationUnit> generateTestFiles(List<File> projectJars, //
			Collection<SootClass> testClasses, //
			boolean resolveTypes) throws IOException {

		Set<CompilationUnit> generatedTestClasses = new HashSet<>();

		// Resolve the missing generics
		final CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}

		// Sort by id/name ?
		List<SootClass> sortedClasses = new ArrayList<>(testClasses);
		Collections.sort(sortedClasses, new Comparator<SootClass>() {

			@Override
			public int compare(SootClass o1, SootClass o2) {
				return Integer.parseInt(o1.getName().split("_")[1]) - Integer.parseInt(o2.getName().split("_")[1]);
			}
		});

		for (SootClass testClass : testClasses) {

			logger.info("Generate source code for " + testClass.getName());

			long start = System.currentTimeMillis();
			CompilationUnit javaCode = converSootClassToJavaClass(testClass);
			long end = System.currentTimeMillis();
			logger.info("Conversion from soot took " + (end - start) + " mses ");

			// Forcefully initialize all the Objects to null if they are not
			// initialized !
			start = System.currentTimeMillis();
			forceObjectInitialization(javaCode, combinedTypeSolver);
			end = System.currentTimeMillis();
			logger.info("Forcing variable initialization took " + (end - start) + " mses ");

			// During delta debugging there's no need to resolve types
			if (resolveTypes) {
				// This updates the code
				start = System.currentTimeMillis();
				resolveMissingGenerics(javaCode, combinedTypeSolver);
				end = System.currentTimeMillis();
				logger.info("Resolving types  took " + (end - start) + " mses ");
			}

			generatedTestClasses.add(javaCode);
		}

		return generatedTestClasses;

	}

	public static void forceObjectInitialization(CompilationUnit cu, TypeSolver typeSolver) {
		// cu.accept(new ModifierVisitor<JavaParserFacade>() {
		//
		// public VariableDeclarator visit(final VariableDeclarator n,
		// JavaParserFacade javaParserFacade) {
		// super.visit(n, javaParserFacade);
		//
		// try {
		//
		// if (!javaParserFacade.getType(n).isPrimitive() &&
		// n.getInitializer().equals(Optional.empty())) {
		// // System.out.println("Focing null init for " + n);
		// n.setInitializer(new NullLiteralExpr());
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// return n;
		// }
		// }, JavaParserFacade.get(typeSolver));
		cu.accept(new ModifierVisitor<Void>() {
			public Visitable visit(VariableDeclarator n, Void arg) {
				if (n.getInitializer().equals(Optional.empty())) {
					if (!JimpleUtils.isPrimitiveDeclaration(n.getNameAsString())) {
						n.setInitializer(new NullLiteralExpr());
					}
				}
				// Rerutn the original
				return super.visit(n, arg);
			}
		}, null);

	}

	// Look for "collection" types and resolve missing generics ...
	public static void resolveMissingGenerics(CompilationUnit cu, TypeSolver typeSolver) {

		final Map<String, ResolvedType> missingTypes = new HashMap<>();

		/**
		 * Check for known collection types if the Actual type is specified or not.
		 */
		// This is wrong ! it parses the class-level initializations only !!
		cu.getType(0).accept(new VoidVisitorAdapter<JavaParserFacade>() {
			@Override
			public void visit(MethodDeclaration md, JavaParserFacade arg) {

				// Check if this type implements collections

				if (md.isAnnotationPresent(Test.class)) {
					// System.out.println("Visiting test method " + md);
					//
					md.accept(new ModifierVisitor<JavaParserFacade>() {

						@Override
						public Visitable visit(VariableDeclarationExpr n, JavaParserFacade javaParserFacade) {
							VariableDeclarator vd = n.getVariables().get(0);
							// Resolve the type of the first variable. This
							// might take some time...
							ResolvedType valueType = javaParserFacade.getType(vd);

							// FIXME: this resolve the type declaration,
							// ResolvedReferenceTypeDeclaration objectType =
							// typeSolver.solveType(Collection.class.getCanonicalName());
							// But cannot match it to: valueType.asReferenceType().getAllAncestors()

							if (!valueType.isReferenceType()) {
								return super.visit(n, javaParserFacade);
							}
							// Probably there's a faster way using contains, but
							// I cannot get a ResolvedRefernceType !
							for (ResolvedReferenceType rrt : valueType.asReferenceType().getAllAncestors()) {
								if (rrt.getQualifiedName().equals(Collection.class.getCanonicalName())) {
									if (((ResolvedReferenceType) valueType).typeParametersMap().isEmpty()) {
										resolveMissingGeneric(vd, javaParserFacade);
									}
								}
							}
							return super.visit(n, arg);

						}

						private void resolveMissingGeneric(final VariableDeclarator vd,
								JavaParserFacade javaParserFacade) {
							// look for the actual type on the value part
							final Set<ResolvedType> missingGeneric = new HashSet<>();
							vd.getInitializer().get().accept(new VoidVisitorAdapter<JavaParserFacade>() {

								@Override
								public void visit(MethodCallExpr n, JavaParserFacade arg1) {
									try {
										missingGeneric.add(arg1.getType(n));
									} catch (Exception e) {
										logger.error(">>> ERROR " + n, e);
									}
									super.visit(n, arg);

								}
							}, javaParserFacade);

							if (!missingGeneric.isEmpty()) {
								// Update the existing one or replace it?
								//
								ResolvedType rt = missingGeneric.iterator().next();
								// System.out.println("Found missing generic " +
								// rt.describe());
								// Update the definition of the variable !
								// JavaParser.parseClassOrInterfaceType(n.getElementType().asString()+"<"+rt+">");
								// n.getElementType().asTypeParameter()
								//
								ClassOrInterfaceType c = JavaParser.parseClassOrInterfaceType(rt.describe());
								// System.out.println(" ClassOrInterfaceType c "
								// + c);
								// n.setVariable(i, variableDeclarator)
								vd.setType(c.getElementType());
							}
						}
					}, arg);

				} else {
					super.visit(md, arg);
				}
			}
		}, JavaParserFacade.get(typeSolver));

		cu.accept(new ModifierVisitor<Void>() {

			public VariableDeclarator visit(final VariableDeclarator n, Void arg) {
				super.visit(n, arg);
				if (missingTypes.containsKey(n.getName().asString())) {
					logger.info("Updating Type Def for " + n.getName());
					n.setType(missingTypes.get(n.getName().asString()).describe());
				}
				return n;
			}
		}, null);

		// This breaks somehow... java parser fails to find the types of some
		// objects...
		// Force Explicit cast if the collections/generic returns an object

		// Cache repetitive assign expression to avoid calling type solver
		// which is somehow broken..

		// For some weird reason this keep failing...
		cu.accept(new ModifierVisitor<JavaParserFacade>() {

			public AssignExpr visit(final AssignExpr n, JavaParserFacade javaParserFacade) {

				// Skip expensive computations for known elements, such as
				// Strings -> javalangstring*
				// if( n.getTarget().toString().startsWith("javalangstring")){
				// return n;
				// }

				// Left
				ResolvedType targetType = null;
				if (!typeCache.containsKey(n.getTarget())) {
					try {
						typeCache.put(n.getTarget(), javaParserFacade.getType(n.getTarget()));
					} catch (Exception e) {
						// Avoid this over and over.
						logger.info("Cannot resolve targetType in " + n.toString() + " reason. " + e);
						// e.printStackTrace();
						typeCache.put(n.getTarget(), null);
						//
						return n;
					}
				}

				targetType = typeCache.get(n.getTarget());

				// Right
				ResolvedType valueType = null;

				if (!typeCache.containsKey(n.getValue())) {
					try {
						typeCache.put(n.getValue(), javaParserFacade.getType(n.getValue()));
					} catch (Exception e) {
						logger.info("Cannot resolve valueType in " + n.toString() + " reason. " + e);
						// e.printStackTrace();
						typeCache.put(n.getValue(), null);
						//
						return n;
					}
				}

				valueType = typeCache.get(n.getValue());

				// Since we store null values we must check the following
				if (targetType == null || valueType == null) {
					logger.info("Type info is missing for assignment n " + targetType);
					return n;
				}

				// This changes the ast and all the calls to symbol solver might
				// be broken !
				if (!targetType.isAssignableBy(valueType)) {
					CastExpr c = new CastExpr();
					c.setType(targetType.describe());
					c.setExpression(n.getValue());
					n.setValue(c);
				}

				return n;
			}
		}, JavaParserFacade.get(typeSolver));

	}

	private final static EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);

	public static CompilationUnit converSootClassToJavaClass(SootClass sootClass) {
		// https://github.com/javaparser/javaparser/wiki/Manual
		logger.debug("TestCaseFactory.generateTestFiles() " + sootClass.getName());

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

				NormalAnnotationExpr annotation = genericMethodDeclaration.addAndGetAnnotation(Test.class);
				MemberValuePair timeout = new MemberValuePair("timeout", new NameExpr("4000"));
				NodeList<MemberValuePair> attrs = new NodeList<>();
				annotation.getPairs().add(timeout);
				//
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
				logger.trace("TestCaseFactory.generateTestFiles() Processing unit " + unit);
				unit.apply(new AbstractStmtSwitch() {
					@Override
					public void caseBreakpointStmt(BreakpointStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseGotoStmt(GotoStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseIdentityStmt(IdentityStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseIfStmt(IfStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseNopStmt(NopStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseRetStmt(RetStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseReturnStmt(ReturnStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseTableSwitchStmt(TableSwitchStmt stmt) {
						logger.trace("\t Skip");
					}

					@Override
					public void caseThrowStmt(ThrowStmt stmt) {
						logger.trace("\t Skip");
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
							StaticFieldRef fieldRef = (StaticFieldRef) stmt.getLeftOp();
							leftExpr = new NameExpr(
									fieldRef.getFieldRef().declaringClass() + "." + fieldRef.getFieldRef().name());
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

							rightExpr = new NameExpr(
									fieldRef.getFieldRef().declaringClass() + "." + fieldRef.getFieldRef().name());

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

						} else if (stmt.getRightOp() instanceof ClassConstant) {
							ClassConstant clazz = (ClassConstant) stmt.getRightOp();
							rightExpr = new NameExpr(clazz.getValue().replaceAll("\\/", ".") + ".class");
						} else {
							// NOT SURE... constants ?
							rightExpr = new NameExpr(stmt.getRightOp().toString());
						}

						if (rightExpr == null || leftExpr == null) {
							logger.trace("null expression. Skip " + leftExpr + " == " + rightExpr);
							return;
						}

						ExpressionStmt e = new ExpressionStmt(
								new AssignExpr(leftExpr, rightExpr, AssignExpr.Operator.ASSIGN));

						logger.debug("caseAssignStmt()" + stmt + " --> " + e);

						methodBody.addStatement(e);
					}

					@Override
					public void caseInvokeStmt(InvokeStmt stmt) {
						InvokeExpr invokeExpr = (InvokeExpr) stmt.getInvokeExpr();

						Expression e = convertSootInvocationExpr(invokeExpr, body);

						if (e == null) {
							// this might happen for Object<init>
							logger.trace("\t Skip");
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
						logger.debug("caseInvokeStmt() " + stmt + " --> " + e);
					}
				});
			}

			if (genericMethodDeclaration instanceof ConstructorDeclaration) {
				((ConstructorDeclaration) genericMethodDeclaration).setBody(methodBody);
			} else if (genericMethodDeclaration instanceof MethodDeclaration) {
				((MethodDeclaration) genericMethodDeclaration).setBody(methodBody);
			}

		}
		//
		return cu;
	}

	public static void checkAndSetRetunValue(CompilationUnit javaCode, final String nameOfTheReturnType) {
		javaCode.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(Test.class)) {
					Statement mut = n.getBody().get().getStatements().removeLast();
					if (!mut.toString().contains("returnValue")) {
						String assignStatement = nameOfTheReturnType + " returnValue = " + mut.toString();
						n.getBody().get().addStatement(assignStatement);
					} else {
						n.getBody().get().addStatement(mut);
					}
				}
				//
				return super.visit(n, arg);
			}
		}, null);

	}
}

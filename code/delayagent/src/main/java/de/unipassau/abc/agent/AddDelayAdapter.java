package de.unipassau.abc.agent;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class AddDelayAdapter extends ClassVisitor {

	private String owner;
	private boolean isInterface;

	public AddDelayAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(version, access, name, signature, superName, interfaces);
		isInterface = (access & ACC_INTERFACE) != 0;
		owner = name;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (!isInterface && mv != null && !name.equals("<init>")) {
			mv = new AddDelayMethodAdapter(mv);
		}
		return mv;
	}

	@Override
	public void visitEnd() {
		// if (!isInterface) {
		// FieldVisitor fv = cv.visitField(ACC_PUBLIC + ACC_STATIC, "timer",
		// "J", null, null);
		// if (fv != null) {
		// fv.visitEnd();
		// }
		// }
		cv.visitEnd();
	}

	public class AddDelayMethodAdapter extends MethodVisitor {

		public AddDelayMethodAdapter(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			// Add a random delay before calling executing any query on the
			// database
			if (name.contains("execute") && owner != null && owner.startsWith("java/sql")) {
				// System.out.println("---> Instrumenting " + name + " " + owner
				// + " " + desc);
				mv.visitMethodInsn(INVOKESTATIC, "de/unipassau/abc/agent/Delayer", "delay", "()V", false);

			}

			/* do call */
			mv.visitMethodInsn(opcode, owner, name, desc, itf);

			/* TODO: System.err.println("RETURN" + name); */
		}

		// @Override
		// public void visitCode() {
		// mv.visitCode();
		// mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
		// mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
		// "currentTimeMillis", "()J");
		// mv.visitInsn(LSUB);
		// mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
		// }
		//
		// @Override
		// public void visitInsn(int opcode) {
		// if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
		// mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
		// mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
		// "currentTimeMillis", "()J");
		// mv.visitInsn(LADD);
		// mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
		// }
		// mv.visitInsn(opcode);
		// }
		//
		// @Override
		// public void visitMaxs(int maxStack, int maxLocals) {
		// mv.visitMaxs(maxStack + 4, maxLocals);
		// }
	}
}
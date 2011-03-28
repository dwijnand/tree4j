package com.dwijnand.tree4j;

/**
 * <em>This is <b>not</b> a real interface, it is a placeholder for documenting what
 * is intended by compliance and immutability guarantees.</em>
 * <p>
 * Guaranteeing compliance is very hard on the JVM. Due to the issues described
 * below, the tree structure classes defined in this library utilise package
 * private constructors and the final modifier on the final concrete
 * implementations. This is a best-effort guarantee and will be utilised until
 * the state-of-affairs improves.
 * <p>
 * There are few issues that create cause for concern, and they are regarding:
 * <ul>
 * <li>creating a class hierarchy of compliant implementations</li>
 * <li>creating immutable stateful implementations</li>
 * </ul>
 * <p>
 * When creating a class hierarchy of compliant implementations, the
 * <em>only</em> way to completely deny the ability to subclass any class within
 * that hierarchy is to define each class as a nested inner class of the class
 * it extends, and only define private constructors for that class. This,
 * however, would create a very ugly user experience as it would force the user
 * to import 3 times nested classes as well as cause the library to be very
 * difficult to maintain.
 * <p>
 * It is also not possible on the JVM to guarantee stateful immutability. This
 * is because it <em>is</em> possible to gain access and modify hidden and/or
 * final fields (the state of the object) via reflection or byte-code
 * manipulation. The only way to deny this is to use a security manager when
 * running the code, but the problem with that is it is not possible to enforce
 * the use of a security manager and for the most part one is never used.
 */
public interface GuaranteedCompliance {

}

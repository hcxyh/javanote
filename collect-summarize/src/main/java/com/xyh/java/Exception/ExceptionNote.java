package com.xyh.java.Exception;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public class ExceptionNote {
	
	/**
	 * Throwable： 有两个重要的子类：Exception（异常）和 Error（错误），二者都是 Java 异常处理的重要子类，各自都包含大量子类。
	 * 
	 * Error（错误）:是程序无法处理的错误，表示运行应用程序中较严重问题。
	 * 大多数错误与代码编写者执行的操作无关，而表示代码运行时 JVM（Java 虚拟机）出现的问题。
	 * 例如，Java虚拟机运行错误（Virtual MachineError），
	 * 当 JVM 不再有继续执行操作所需的内存资源时，将出现 OutOfMemoryError。
	 * 这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。
	 * 这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时，
	 * 如Java虚拟机运行错误（Virtual MachineError）、类定义错误（NoClassDefFoundError）等。
	 * 这些错误是不可查的，因为它们在应用程序的控制和处理能力之 外，
	 * 而且绝大多数是程序运行时不允许出现的状况。对于设计合理的应用程序来说，
	 * 即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。
	 * 在 Java中，错误通过Error的子类描述。
	 * 
	 * Exception（异常）:是程序本身可以处理的异常。
	 * RuntimeException。RuntimeException 类及其子类表示“JVM 常用操作”
	 * 引发的错误。例如，若试图使用空值对象引用、除数为零或数组越界，
	 * 则分别引发运行时异常（NullPointerException、ArithmeticException）和 ArrayIndexOutOfBoundException。
	 * 注意：异常和错误的区别：异常能被程序本身可以处理，错误是无法处理。
	 * 
	 * 可查的异常（checked exceptions）和不可查的异常（unchecked exceptions）。
	 * 可查异常（编译器要求必须处置的异常）：
	 * 正确的程序在运行中，很容易出现的、情理可容的异常状况。可查异常虽然是异常状况，
	 * 但在一定程度上它的发生是可以预计的，而且一旦发生这种异常状况，就必须采取某种方式进行处理。
	 * 除了RuntimeException及其子类以外，其他的Exception类及其子类都属于可查异常。
	 * 这种异常的特点是Java编译器会检查它，也就是说，当程序中可能出现这类异常，
	 * 要么用try-catch语句捕获它，
	 * 要么用throws子句声明抛出它，否则编译不会通过。
	 * 
	 * 不可查异常(编译器不要求强制处置的异常):包括运行时异常（RuntimeException与其子类）和错误（Error）。
	 * Exception 这种异常分两大类运行时异常和非运行时异常(编译异常)。程序中应当尽可能去处理这些异常。
	 *  
	 */
	
}

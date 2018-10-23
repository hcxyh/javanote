package com.xyh.java.jdk8.book;

/**
 * 【读书笔记】《写给大忙人看的Java SE 8》——Java8新特性总结 (没有包含concurrent包的变化 --completeFuture)
 * @author xyh
 * 	
 * 	1.Stream 
 * 	2.option
 *  3.接口的default默认方法,静态方法
 *  4.functionInterface,函数式接口. lamdba表达式 和方法引用 .
 *
 */
public class Java8BookNote {
	
	/**
	 * 
		1.接口中的默认方法和静态方法
		先考虑一个问题，如何向Java中的集合库中增加方法？例如在Java 8中向Collection接口中添加了一个forEach方法。
		如果在Java 8之前，对于接口来说，其中的方法必须都为抽象方法，也就是说接口中不允许有接口的实现，
		那么就需要对每个实现Collection接口的类都需要实现一个forEach方法。
		但这就会造成在给接口添加新方法的同时影响了已有的实现，所以Java设计人员引入了接口默认方法，
		其目的是为了解决接口的修改与已有的实现不兼容的问题，接口默认方法可以作为库、框架向前兼容的一种手段。
		默认方法就像一个普通Java方法，只是方法用default关键字修饰。
	 * 
	 */
	public interface Person {
	    //默认方法
	    default String getName(String name) {
	        return name;
	    }
	}
	public class Student implements Person {

	}
	public  class Test {
	    public  void test(String[] args) {
	        Person p = new Student();
	        String name = p.getName("小李");
	        System.out.println(name);
	    }
	}
	/**
	 我们定义了一个Person接口，其中getName是一个默认方法。接着编写一个实现类，可以从结果中看到，虽然Student是空的，
	 但是仍然可以实现getName方法。
	显然默认接口的出现打破了之前的一些基本规则，使用时要注意几个问题。
		考虑如果接口中定义了一个默认方法，而另外一个父类或者接口中又定义了一个同名的方法，该选择哪个？
	1. 选择父类中的接口。如果一个父类提供了具体的实现方法，那么接口中具有相同名称和参数的默认方法会被忽略。
	2. 接口冲突。如果一个父接口提供了一个默认方法，而另一个接口也提供了具有相同名称和参数类型的方法（不管该方法是否是默认方法），
	那么必须通过覆盖方法来解决。
		记住一个原则，就是“类优先”，即当类和接口都有一个同名方法时，只有父类中的方法会起作用。
	“类优先”原则可以保证与Java 7的兼容性。如果你再接口中添加了一个默认方法，它对Java 8以前编写的代码不会产生任何影响。
	
	下面来说说静态方法。
	静态方法就像一个普通Java静态方法，但方法的权限修饰只能是public或者不写。
	默认方法和静态方法使Java的功能更加丰富。
	在Java 8中Collection接口中就添加了四个默认方法，
	stream()、parallelStream()、forEach()和removeIf()。Comparator接口也增加了许多默认方法和静态方法。
	 */
	
	
	/**
	 2.函数式接口和Lambda表达式
	 	函数式接口（Functional Interface）是只包含一个方法的抽象接口。
		比如Java标准库中的java.lang.Runnable，java.util.concurrent.Callable就是典型的函数式接口。
		在Java 8中通过@FunctionalInterface注解，将一个接口标注为函数式接口，该接口只能包含一个抽象方法。
		@FunctionalInterface注解不是必须的，只要接口只包含一个抽象方法，虚拟机会自动判断该接口为函数式接口。
		一般建议在接口上使用@FunctionalInterface注解进行声明，以免他人错误地往接口中添加新方法，
		如果在你的接口中定义了第二个抽象方法的话，编译器会报错。
	函数式接口是为Java 8中的lambda而设计的，lambda表达式的方法体其实就是函数接口的实现。
	为什么要使用lambda表达式？
	“lambda表达式”是一段可以传递的代码，因为他可以被执行一次或多次。我们先回顾一下之前在Java中一直使用的相似的代码块。
	当我们在一个线程中执行一些逻辑时，通常会将代码放在一个实现Runnable接口的类的run方法中.
	new Thread(new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < 10; i++)
                    System.out.println("Without Lambda Expression");
            }}).start();
      如果想利用字符串长度排序而不是默认的字典顺序排序，就需要自定义一个实现Comparator接口的类，然后将对象传递给sort方法。
     class LengthComparator implements Comparator<String> {
		    @Override
		    public int compare(String s1, String s2) {
		        return Integer.compare(s1.length(), s2.length());
		    }
		}
		Arrays.sort(strings, new LengthComparator());
	按钮回调是另一个例子。将回调操作放在了一个实现了监听器接口的类的一个方法中。
	JButton button = new JButton("click");
	button.addActionListener(new ActionListener() {    
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        System.out.println("Without Lambda Expression");
	    }
	});
	这三个例子中，出现了相同方式，一段代码被传递给其他调用者——一个新线程、是一个排序方法或者是一个按钮。这段代码会在稍后被调用。
	在Java中传递代码并不是很容易，不可能将代码块到处传递。你不得不构建一个类的对象，由他的某个方法来包含所需的代码。
	而lambda表达式实际上就是代码块的传递的实现。其语法结构如下：
	
	(parameters) -> expression 或者 (parameters) -> {statements;}
	括号里的参数可以省略其类型，编译器会根据上下文来推导参数的类型，你也可以显式地指定参数类型，如果没有参数，括号内可以为空。
	方法体，如果有多行功能语句用大括号括起来，如果只有一行功能语句则可以省略大括号。
	
	new Thread(() -> {
            for (int i = 0; i < 100; i++)
                System.out.println("Lambda Expression");
        }).start();
	
	Comparator<String> c = (s1, s2) -> Integer.compare(s1.length(), s2.length());

	button.addActionListener(e -> System.out.println("Lambda Expression"));
	
	可以看到lambda表达式使代码变得简单，代替了匿名内部类。
	下面来说一下方法引用，方法引用是lambda表达式的一种简写形式。 如果lambda表达式只是调用一个特定的已经存在的方法，则可以使用方法引用。
	使用“::”操作符将方法名和对象或类的名字分隔开来。以下是四种使用情况：
		对象::实例方法
		类::静态方法
		类::实例方法
		类::new
	Arrays.sort(strings, String::compareToIgnoreCase);
	// 等价于
	Arrays.sort(strings, (s1, s2) -> s1.compareToIgnoreCase(s2));
	
	 */
	
	
	
	/**
	 * 3.Stream API
	 	当处理集合时，通常会迭代所有元素并对其中的每一个进行处理。例如，我们希望统计一个字符串类型数组中，所有长度大于3的元素。
	 	String[] strArr = { "Java8", "new", "feature", "Stream", "API" };
        int count = 0;
        for (String s : strArr) {
            if (s.length() > 3)
                count++;
        }
        通常我们都会使用这段代码来统计，并没有什么错误，只是它很难被并行计算。这也是Java8引入大量操作符的原因，在Java8中，实现相同功能的操作符如下所示：
	long count = Stream.of(strArr).filter(w -> w.length() > 3).count();
	stream方法会为字符串列表生成一个Stream。filter方法会返回只包含字符串长度大于3的一个Stream，然后通过count方法计数。
	
	一个Stream表面上与一个集合很类似，允许你改变和获取数据，但实际上却有很大区别：
		1.Stream自己不会存储元素。元素可能被存储在底层的集合中，或者根据需要产生出来。
		2.Stream操作符不会改变源对象。相反，他们返回一个持有新结果的Stream。
		3.Stream操作符可能是延迟执行的。意思是它们会等到需要结果的时候才执行。
	Stream相对于循环操作有更好的可读性。并且可以并行计算：
	 	long count = Arrays.asList(strArr).parallelStream().filter(w -> w.length() > 3).count();
	 	只需要把stream方法改成parallelStream，就可以让Stream去并行执行过滤和统计操作。
		Stream遵循“做什么，而不是怎么去做”的原则。只需要描述需要做什么，而不用考虑程序是怎样实现的。
		Stream很像Iterator，单向，只能遍历一遍。但是Stream可以只通过一行代码就实现多线程的并行计算。
	当使用Stream时，会有三个阶段：
		1.创建一个Stream。
			// 1. Individual values
			Stream stream = Stream.of("a", "b", "c");
			// 2. Arrays
			String [] strArray = new String[] {"a", "b", "c"};
			stream = Stream.of(strArray);
			stream = Arrays.stream(strArray);
			// 3. Collections
			List<String> list = Arrays.asList(strArray);
			stream = list.stream();
		2.在一个或多个步骤中，将初始Stream转化到另一个Stream的中间操作。
			中间操作包括：map (mapToInt, flatMap 等)、 filter、distinct、
			sorted、peek、limit、skip、parallel、sequential、unordered。
 		3.使用一个终止操作来产生一个结果。该操作会强制他之前的延迟操作立即执行。在这之后，该Stream就不会在被使用了。
 			终止操作包括：forEach、forEachOrdered、toArray、reduce、collect、min、max、count、
 			anyMatch、allMatch、noneMatch、findFirst、findAny、iterator。
 		
 		关于Stream的每个方法如何使用就不展开了，更详尽的介绍看这篇文章：
 		https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
	 */
	
	/**
	 4.新的日期和时间 API
	 	Java8 引入了一个新的日期和时间API，位于java.time包下。
		新的日期和时间API借鉴了Joda Time库，其作者也为同一人，但它们并不是完全一样的，做了很多改进。
		下面来说一下几个常用的类。首先是Instant，一个Instant对象表示时间轴上的一个点。
		Instant.now()会返回当前的瞬时点（格林威治时间）。Instant.MIN和Instant.MAX分别为十亿年前和十亿年后。
	 	
	 	Instant start = Instant.now();
		runAlgorithm();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		long millis = timeElapsed.toMillis();
	  
	 */
	
}

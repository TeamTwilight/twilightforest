Contributing
============

* No AI Slop
  * This is not a blanket ban on LLMs as a whole. They are a tool, and we won't force developers to not use a tool. That's like saying you're not allowed to use Eclipse. But please, be reasonable and know what you are doing. If the LLM is doing all the work for you then you're not the one contributing and the PR will be closed.
* Scope your PR. If the PR is doing too many different things, it needs to be split up. One feature or one bug fix at a time, please.
* Avoid Statics
  * This is not a hard rule, but we have a [DI](https://github.com/Tamaized/Beanification) library that can be used.
  * Statics lead to more difficult tests. Statics are hard to mock.
  * Again, it's fine if statics end up being used. We don't expect developers to understand Dependency Injection. 99% of modders don't know what that is.
* Always use `@Override`
  * Do NOT add `@Nullable` to an overridden method if the parent is expected to be non-null.
* New packages must contain a `package-info.java`
  * Use existing ones as an example.
* Avoid `@SuppressWarnings` where possible
  * Sometimes it's just not possible to do so, and that's okay, but be reasonable.
* Avoid the `this.memberName` syntax where possible
  * It's unnecessary bloat and does not add any value to readability.
  * There are only a handful of valid use cases in Java. Examples should not be needed for a competent Java developer.
* Avoid the `var` keyword.
  * `var` hurts readability. The only valid use case is when there's a monster of a nested generic where it would actually help.
* Class formatting
  * Blank lines between the class declaration and members
  * Fields are always above all methods and the constructor
  * Blank lines between fields and methods
    * Grouping fields together (when there's more than one group) can avoid the newline
    ```java
    class SomeClass {

    	int x;
    	int y;

    	String a;
    	String b;

    	SomeClass() {}

    	void someMethod() {}

    	void anotherMethod() {}

    }
    ```
  * Field ordering
    * Static Injected
    * Static
    * Instance Injected
    * Instance `final` keyword
    * Mutable Instance
    ```java
    class SomeClass {

    	@Autowired
    	private static SomeBean someStaticBean;

    	public static final String SOME_CONST = "xyz";

    	@Autowired
    	private SomeBean someBean;

    	private final String someFinalValue = "abc";

    	private String someMutableValue;

    }
    ```
  * Method ordering
    * Constructor(s)
    * `@PostConstruct`
    * Every other method
    * There's no set order for static vs non-static
* General Formatting
  * Braceless if/else places the statement on the following line, indented one level deeper.
    * Avoid `if (x) return;`
  * Never newline after a `=`
  * Wrapped call chains
    * Break before the dot. The line ends at `member()` and the following line starts with `.nestedMember()` one indentation deeper.
    * If the chain will reasonably fit on one line then it stays on one line.
    * If the line is too long because of a singular member call then break the calls arguments one per line instead of the method chain.
    ```java
    class SomeClass {

    	public void method() {
    		a().b().c(); // Reasonable

    		someVeryVeryVeryLongName()
    			.someExtraLongNestedName()
    			.doubleNestedWithALongName();

    		a().b(
    			veryLongParamNameOne(),
    			veryLongParamNameTwo(),
    			veryLongParamNameThree()
    		).c();
    	}

    }
    ```
* No single use consts.
  * If you don't know what a const is, it's a `static final` field.
  * If these are only used in a single place, just inline it.
  * It makes review difficult by having to scroll all the way back up to the top of the class to see what the hell the value is.
  * Magic numbers are an exception. However, you can also declare final variables on the method level, unless performance becomes a concern. In that case they can go into a static final. Use your own judgment.
* Unused lambda params get named as `_`
* No unused fields / params / imports
  * Unless the param is forced by the supertype

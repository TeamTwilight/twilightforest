Contributing
============

* No AI Slop
  * This is not a blanket ban on LLMs as a whole, they are a tool, and it's wrong to force developers to not use a tool, that's like saying you're not allowed to use Eclipse. But please be reasonable and know what you are doing, if the LLM is doing all the work then you're not the one contributing and the PR will be closed.
* Scope your PR. If the PR is doing way too many things, it needs to be split up. One feature or one bug fix at a time please.
* Avoid Statics
  * This is not a hard rule, but we have a [DI](https://github.com/Tamaized/Beanification) library that can be used.
  * Statics lead to more difficult tests. Statics are hard to mock.
  * Again, it's fine if statics end up being used, we don't expect devs to understand Dependency Injection, 99% of modders don't know what that is.
* Always use `@Override`
  * Do NOT add `@Nullable` to an overridden method if the parent is expected to be non-null.
* New packages must contain a `package-info.java`
  * Use existing ones as an example.
* Avoid `@SuppressWarnings` where possible
  * Sometimes it's just not possible and that's okay! Be reasonable.
* Avoid the `this.memberName` syntax where possible
  * It's unnecessary bloat and does not add any value to readability.
  * There are only a handful of valid use cases in Java. Examples should not be needed for a competent Java developer.
* Avoid the `var` keyword.
  * `var` hurts readability. The only valid use case is when there's a monster of a nested generic where it would actually help.
* Class formatting
  * Blank lines between the class declaration and members
  * Fields are always above all methods and the constructor.
  * Blank lines between fields and methods
    * Grouping fields together (when there's more than one group) can avoid the newline.
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
  * Braceless if/else places the statement on the following line, indented one level deeper
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
  * It makes review difficult having to scroll all the way back up to the top of the class to see wtf the value is.
  * Magic numbers are an exception however you can also declare final variables on the method level, unless performance becomes a concern then they can go into a static final. Use your judgment.
* Unused lambda params get named as `_`
* No unused fields / params / imports
  * Unless the param is forced by the supertype
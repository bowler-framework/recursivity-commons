
# Recursivity-commons utilities
Various Java- and Scala utilities with no outside library dependencies.
Built with SBT (Simple-Build-Tool)

Java classes: not written by me, but stuff I often use and compiled from various books and open source sources.

MVN/SBT repo: 
	
	https://oss.sonatype.org/content/repositories/releases (built against 2.9.1, compatible with Scala 2.8.x, but requires you to build yourself)

Dependency definition (sbt): 
	
	"com.recursivity" %% "recursivity-commons" % "0.6"

## Scala validator (com.recursivity.commons.validator-package)
Simple and extensible Scala validation framework that makes use of Scala function passing to provide a more powerful validation framework.
Supports:

* Custom validators
* Localised error messages
* hooking in at various level of granularity, depending on your needs.

### Example usage:

 	// setup a bean to validate
 	val bean = new MyBean("hello", 5, new Date, None)

	// new ValidationGroup with a ClassPathMessageResolver
	val group = ValidationGroup(new ClasspathMessageResolver(this.getClass))

	// add the validators to the ValidationGroup, note how we pass an anonymous function that will get the value/variable we want to validate.
	group.add(MinLength("hello", 8, {bean.text}))
	group.add(MaxInt("max", 3, {bean.number}))
	group.add(MinInt("min", 6, {bean.number}))
	group.add(NotNullOrNone("null", {bean.value}))

	// validate and return error messages, a List of Tuple2's with (key, errorMessage) format.
	val failures = group.validateAndReturnErrorMessages

Assumes property-files in the same package with the same name as "this"-class, for instance com.mypackage.MyClass would be:

	/com/mypackage/MyClass.properties // for the default messages
	/com/mypackage/MyClass_se.properties // for messages localized in Swedish

Contents of property-file might be:

	#Validator messages
	MinLengthValidator={key} must be at least {min} characters long
	# {key} is replaced for either property value of key, for instance "hello", or simply the raw key.
	# {min} in this example would be replaced by 8 from the code above.

	# property key-values
	hello='your text'
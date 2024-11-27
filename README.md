# Swingy

Create an RPG game using java and the swing GUI library.

## Game:

Create a game where different types of heros beat different kinds of villains in a N * N map.

Heros will be persisted, using file or database.

## Need to know:

### Maven:

Maven is a build automation tool used primarily for Java projects. Maven can also be used to build and manage projects written in C#, Ruby, Scala, and other languages. 

pom.xml: dependency and version control file, like package.json

### Hibernate Validator:

Hibernate Validator is the reference implementation of the *Java Bean Validation* (JSR 380) specification. It provides a way to define constraints for fields and properties in Java classes, ensuring that the objects meet specific validation rules before being processed or persisted. This is particularly useful for validating data before saving it to a database, accepting user input, or performing any business logic.

### Jackson:

**Jackson** is a high-performance JSON processor used for Java. It is the most popular library used for serializing Java objects or Map to JSON and vice-versa. It is completely based on Java. Jackson tutorial provides all the basic and advanced concepts of the Jackson library.

## Swing:

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7b1649d2-91cd-4c10-a57a-3871aea1fcc7/daac9ef9-3028-4955-95fa-6772fd3fa5e4/image.png)

JFrame: like Window.

JPanel: Container for JComponent

JComponent: Basic components like label and button

### Example

The app lifecycle 

```java
public class AppLifecycle implements Lifecycle {
		
	public void run(WindowController windowController, JFrame frame) {		
		windowController.show(frame, BoundsPolicy.MAXIMIZE);
	}

	public void configure(String[] args) {
		// whatever you want to configure
		System.out.println(Arrays.toString(args));
	}

	public boolean confirmExit() {
		//some logic to confirm the exit
		return true;
	}

	public void exit() {
		//save your stuff
		System.out.println("goodbye!");
		System.exit(0);
	}
    
}
```
Launcher 

```java
public class AppTest {
	
	public static void main(String [] args){
		Launcher.launch(args, new AppLifecycle());
	}
	
}
```
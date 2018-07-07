# Salesforce Automation Testing Library ![Build Status](https://travis-ci.org/zwb1988/salesforce-sat.svg?branch=master)

A set of wrappers classes for automation testing in Salesforce's Lightning by using Selenium.
The library mainly wraps the common components on the Salesforce standard lightning interface.

The project at the current state is merely a initial version. There is only few component wrappers available.
More commonly used component wrapper will be used in the future.

## Example
The code sample below demonstrate a Salesforce login with a change to the filter on the global search.
```java
public class TestRun {
    public static void main(String[] args) {
        AliasManager.getInstance().register(Alias.class);

        WebDriverSalesforce webInstance = new WebDriverSalesforce( "https://test.salesforce.com");
        try {
            webInstance.login("myusername@example.com", "mypassword");
            GlobalSearch search = new GlobalSearch(webInstance.getWebDriver());
            search.changeType("Accounts");
            search.search("My Test Account");
            // ... more operations follows ...
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## Getting Started

#### Prerequisists
1. Maven 3
2. Java SE 8 and above
2. Python 3.x

Before we can compile or add the project as a dependency, an Alias file need to be generated. This is done by the maven build script, but it is important to understand how the Alias works, as you will need it if you want to build your own component wrapper. Please read the **Extension** section for more details.

##### Two ways to setup your project
- Simply compile the project into a jar file and add it as a dependency to your automation testing project.
- For maven projects, you can also add this project as a decadency to yours.


## Extension
You can create your own wrappers by extending the base wrapper **Element** class. In most of the cases, you will need your own properties file once you extended.

#### Extend the base wrapper
Here is an example of how you can extend the base wrapper and associate a property file to it:

```java
public class GlobalSearch extends Element {

    public GlobalSearch(WebDriver webDriver) {
        super(webDriver, null, GlobalSearch.class);
    }
}
```

We create a GlobalSearch class that extend the Element wrapper class. 
Notice in the constructor, we are passing the class type of the current class as a parameter to the super constructor.
This will associate the GlobalSearch class to a GlobalSearch.properties file if it finds one.

#### Provide property mapper file (optional)
Here is a sample of what's in the GlobalSearch.properties file
```
#GlobalSearch.properties
control=xpath|//div[@class="slds-combobox-group forceSearchDesktopHeader"]
dropdown=xpath|div[contains(@class, 'slds-combobox_object-switcher')]
```

#### Custom Alias generation and registration
Once you've created extension classes or modified properties files, your custom Alias needs to be generated.

The generated alias file serves as a reference point for all the key value pairs defined in *.properties files. It contains a set of Enums that maps to the key values in every property files within Java.

The generator is written in Python 3 and it is located under the src\main\resources\tools\AliasClassGenerator folder. Whenever a property file is changed, created or removed, we need to run this generator script, or let the maven build to do it.

The base wrapper class: **Element** has a mechanism that will load properties files dynamically based on the name of the sub-class that extends it.
```commandline
python AliasClassGenerator.py
```

To generate the Alias file for your project, you need to:
1. **Copy** the Python scripts for generating the Alias file into your project's resource folder, and open the _AliasClassGenerator.py_ in a text editor. 

2. **Update the setting variables** on the top of the _AliasClassGenerator.py_:


| Variables | Description |
| --- | --- |
| propertyFolder | Folder name of the property folder under the resources directory |
| resourcePath | The relative path of the resource folder to the python script |
| aliasFilePath | The path for the generated Alias file |
| propfileExts | Property file extension list |
| aliasFilename | The file name for the generated Alias file |
| package | The package for the the generated Alias file |

```python
propertyFolder = "properties"
resourcePath = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../"))
propertiesPath = os.path.join(resourcePath, propertyFolder)
aliasFilePath = os.path.abspath(os.path.join(resourcePath, "../java/sfdctest/ui/element"))
propfileExts = ["*.txt", "*.properties"]
aliasFilename = "Alias.java"
package = "sfdctest.ui.element"
```

In the property files, you can defined your key value pairs. The key will present in the Alias class and capitalized during the Alias generation. Then you can reference it in your code as follows:

```java
public class GlobalSearch extends Element {

    public GlobalSearch(WebDriver webDriver) {
        super(webDriver, null, GlobalSearch.class);
    }

    @Override
    public void initialize() {
        super.currentElement = this.getElement(Alias.RefGlobalSearch.CONTROL.toString());
    }
}
```
Note that as a standard practice, you should also override the _initialize()_ method if you have a target DOM element to be wrapped by the current custom wrapper class.

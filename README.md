# Salesforce Automation Testing Library 

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
Before we can compile or add the project as a dependency, an Alias file need to be generated.

The generated alias file serves as a reference point for all the key value pairs defined in *.properties files.

The generator is located under the resources\tools folder, and it written in Python 3. Whenever a property file changed, created or removed, we need to run this generator script.

The base wrapper class: **Element** has a mechanism that will load properties files dynamically based on the name of the sub-class that extends it.
```commandline
python AliasClassGenerator.py
```
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

We create a GlobalSearch class that extending the Element wrapper class. 
Notice in the constructor, we are passing the class type of the current class as a parameter to the super constructor.
This will associate the GlobalSearch class to a GlobalSearch.properties file if it can find one.

#### Provide property mapper file (optional)
Here is a sample of what's in the GlobalSearch.properties file
```
#GlobalSearch.properties
control=xpath|//div[@class="slds-combobox-group forceSearchDesktopHeader"]
dropdown=xpath|div[contains(@class, 'slds-combobox_object-switcher')]
```

#### Custom Alias generation and registration
Once you've created extension classes and properties files, your custom Alias needs to be generated.
**Copy** the Python scripts for generating Alias file into your project, and open the _AliasClassGenerator.py_ in a text editor.

**Update the setting variables** on the top of the _AliasClassGenerator.py_:
| Variables | Description |
| --- | --- |
| propertyFolder | Folder name of the property folder |
| resourcePath | The relative path of the property resource folder |
| aliasFilePath | The path for the generated Alias file |
| propfileExts | Property file extension list |
| aliasFilename | The file name for the generated Alias file |
| package | The package for the the generated Alias file |

```python
propertyFolder = "properties"
resourcePath = os.path.abspath(os.path.join(
    os.path.dirname(__file__), "../" + propertyFolder))
aliasFilePath = os.path.abspath(os.path.join(
    os.path.dirname(__file__), "../../java/sfdctest/ui/element"))
propfileExts = ["*.txt", "*.properties"]
aliasFilename = "Alias.java"
package = "sfdctest.ui.element"
```

In the property files, you can defined your key value pairs. The key will present in the Alias class and capitalized during the Alias generation.
Then you can reference it in your code as follows:

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
Note that as a standard practice, you should also override the _initialize()_ method if you have a target DOM element to be wrapped by the current sub-class.

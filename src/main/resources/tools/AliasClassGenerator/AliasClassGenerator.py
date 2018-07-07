"""
    Alias Class Generator

    This is a python script that will read the property files and create Java Enum in a Alias
    class file based on the file name. And for every identified property in the property files, 
    it will generate a enum value
"""

__author__ = "Wenbo Zhou"
__credits__ = ["Wenbo Zhou"]
__license__ = "MIT"
__version__ = "1.2"
__maintainer__ = "Wenbo Zhou"
__status__ = "Production"

import os
from glob import glob

from AliasClassTemplates import classTemplate, propClassTemplate, enumClassTemplate
from PropertyWrapper import PropertyWrapper

propertyFolder = "properties"
resourcePath = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../"))
propertiesPath = os.path.join(resourcePath, propertyFolder)
aliasFilePath = os.path.abspath(os.path.join(resourcePath, "../java/sfdctest/ui/element"))
propfileExts = ["*.txt", "*.properties"]
aliasFilename = "Alias.java"
package = "sfdctest.ui.element"


imports = ["java.util.Collections", "java.util.HashMap", "java.util.Map", "sfdctest.common.AliasInterface"]
propClassTemplate_fileMapVar = "fileMap"


def getEnumClass(propertyWrapper):
    identifiers = propertyWrapper.getAllPropertyNames()
    enums = ',\n        '.join(identifiers)
    return enumClassTemplate.format(filename=propertyWrapper.name, enums=enums)


def getPropClass(propertyWrappers):
    if propertyWrappers is list and len(propertyWrappers) == 0:
        return ""

    filename_items = []
    file_map_items = []
    for wrapper in propertyWrappers:
        const_var_name = wrapper.name.upper()
        filename = wrapper.name
        file_rel_path = os.path.join(propertyFolder, wrapper.getRelativePath(propertiesPath)).replace("\\", "\\\\")
        filename_items.append(
            f"public static final String {const_var_name} = \"{filename}\";\n")
        file_map_items.append(
            f"{propClassTemplate_fileMapVar}.put({const_var_name}, \"{file_rel_path}\");\n"
        )
    return propClassTemplate.format(propNames='        '.join(filename_items),
                                    fileMapVar=propClassTemplate_fileMapVar,
                                    propFilesMap='            '.join(file_map_items))


def parse(targetDir, filename):
    target_file = os.path.join(targetDir, filename)
    print(f"Parsing file: {target_file}")

    wrapper = None
    with open(target_file, 'r', encoding='utf-8') as file:
        class_name = filename
        if '.' in filename:
            separator_index = filename.index('.')
            class_name = filename[:separator_index]

        wrapper = PropertyWrapper(class_name, filename, target_file)
        for line in file:
            if line.startswith('#') or '=' not in line:
                continue
            tokens = line.split('=', 1)
            key = tokens[0].strip().replace(' ', '_').replace('-', '_').upper()
            value = tokens[1]
            wrapper.addProperty(key, value)
    return wrapper


def parsePath(filepath):
    return parse(os.path.dirname(filepath), os.path.basename(filepath))


def writeToFile(propertyWrappers, outPath, outFilename):
    outfile = os.path.join(outPath, outFilename)
    print(f"Generating file: {outfile}")
    enum_classes = []
    for wrapper in propertyWrappers:
        enum_classes.append(getEnumClass(wrapper))
    all_enum_classes = ''.join(enum_classes)

    import_statements = []
    for import_statement in imports:
        import_statements.append(f"import {import_statement};\n")

    file_content = classTemplate.format(
        package=package, imports="".join(import_statements), enumClasses=all_enum_classes,
        propClass=getPropClass(propertyWrappers))

    with open(outfile, 'w') as aliasFile:
        aliasFile.writelines(file_content)


def main():
    filepath_list = []
    for folderPath, _, _ in os.walk(propertiesPath):
        for ext in propfileExts:
            filepath_list += glob(os.path.join(folderPath, ext))
    print("Start parsing files...")
    wrappers = [parsePath(filepath) for filepath in filepath_list]
    writeToFile(wrappers, aliasFilePath, aliasFilename)


if __name__ == '__main__':
    print(f"Running {os.path.basename(__file__)} @{__version__}")
    main()
    print("Program execution completed")

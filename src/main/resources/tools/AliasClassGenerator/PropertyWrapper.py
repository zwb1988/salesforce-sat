import os


class PropertyWrapper:

    def __init__(self, name, filename, filepath):
        self.name = name
        self.filename = filename
        self.filepath = filepath
        self.properties = {}

    def addProperty(self, key, value):
        self.properties[key] = value

    def getAllPropertyNames(self):
        return self.properties.keys()

    def getRelativePath(self, resourcePath):
        return os.path.relpath(self.filepath, resourcePath)

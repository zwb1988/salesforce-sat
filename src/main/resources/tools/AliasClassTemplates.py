classTemplate = \
    """/*
 * This file is generated. Please do not modify it manually.
 * If you want to update this file, run the generator again.
 * 
 */

package {package};

{imports}

public class Alias implements AliasInterface {{

    @Override
    public Map<String, String> getFileMap() {{
        return File.FILE_PATH_MAP;
    }}
    
    {propClass}
    {enumClasses}
}}

"""

propClassTemplate = \
    """
    public static class File {{
        {propNames}
        
        public static final Map<String, String> FILE_PATH_MAP;
        static {{
            Map<String, String> {fileMapVar} = new HashMap<>();
            {propFilesMap}
            FILE_PATH_MAP = Collections.unmodifiableMap({fileMapVar});
        }}
    }}
"""

enumClassTemplate = \
    """
    public enum Ref{filename} {{
        {enums}
    }}
"""

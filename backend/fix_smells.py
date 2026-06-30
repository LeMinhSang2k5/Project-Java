import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original = content

    # 1. Remove public from @Test methods and classes
    if 'src/test' in filepath.replace('\\', '/'):
        content = re.sub(r'public class (\w+Test)', r'class \1', content)
        content = re.sub(r'public void (\w+)\(', r'void \1(', content)

    # 2. Replace ResponseEntity<?> with ResponseEntity<Object>
    content = content.replace('ResponseEntity<?>', 'ResponseEntity<Object>')

    # 3. Format specifiers for logger (very basic heuristics)
    # logger.info("Message: " + var) -> logger.info("Message: {}", var)
    # This regex is simple and works for one concatenation at the end
    content = re.sub(r'(logger\.(?:info|error|warn|debug))\("([^"]+)" \+ ([a-zA-Z0-9_.()]+)\)', r'\1("\2{}", \3)', content)
    # logger.info("Message: " + var + " suffix") -> logger.info("Message: {} suffix", var)
    content = re.sub(r'(logger\.(?:info|error|warn|debug))\("([^"]+)" \+ ([a-zA-Z0-9_.()]+) \+ "([^"]+)"\)', r'\1("\2{}\4", \3)', content)

    # 4. System.err.println to logger.error
    # Only if logger is defined in the file
    if 'logger.' in content or 'LoggerFactory' in content:
        content = re.sub(r'System\.err\.println\("([^"]+)" \+ ([a-zA-Z0-9_.()]+)\);', r'logger.error("\1{}", \2);', content)
        content = re.sub(r'System\.out\.println\("([^"]+)" \+ ([a-zA-Z0-9_.()]+)\);', r'logger.info("\1{}", \2);', content)
        content = re.sub(r'System\.err\.println\(([^)]+)\);', r'logger.error(\1);', content)
        content = re.sub(r'System\.out\.println\(([^)]+)\);', r'logger.info(\1);', content)

    # 5. Remove useless eq(...)
    # when(mock.method(eq(val), any())) -> when(mock.method(val, any()))
    # this is a bit risky with regex, but we can target eq(1L) or eq("str")
    content = re.sub(r'eq\(([0-9]+L?)\)', r'\1', content)
    content = re.sub(r'eq\("([^"]+)"\)', r'"\1"', content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

if __name__ == "__main__":
    count = 0
    for root, dirs, files in os.walk('src'):
        for file in files:
            if file.endswith('.java'):
                if process_file(os.path.join(root, file)):
                    count += 1
    print(f"Modified {count} files.")

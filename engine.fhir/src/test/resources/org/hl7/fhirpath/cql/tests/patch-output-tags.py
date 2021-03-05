import sys, os

# Place script in same directory as xml test files. It iterates over them and properly types the <output> tags.
# Note that we're waiting for the js engine folks to get back to us about proper primitive types etc.

CQL_FUNCS = [
    'interval', 'power', 'concept',
]

def infer_type(value):
    for char in value:
        num_cnt = 0
        if char.isnumeric:
            num_cnt = num_cnt + 1
        if num_cnt > 0 and char == 'L':
            print(f'Returning quantity for value {value}')
            return 'quantity'
    
    # if value[0] == '{' and value[-1:] == '}':
    #     return 'code'
    if 'cm' in value or '/cm' in value:
        print(f'Returning quantity for value {value}')
        return 'quantity'
    if value.isnumeric() and '.' not in value:
        print(f'Returning integer for value {value}')
        return 'integer'
    if value[0] == '-' and value[1:].replace('.','').isnumeric():
        print(f'Returning integer for value {value}')
        return 'integer'
    if value.replace('.','',1).isdigit():
        print(f'Returning decimal for value {value}')
        return 'decimal'
    if 'true' in value.lower() or 'false' in value.lower():
        print(f'Returning boolean for value {value}')
        return 'boolean'
    if '@' in value or 'time' in value.lower():
        print(f'Returning dateTime for value {value}')
        return 'dateTime'
    if '\'' in value:
        print(f'Returning string for value {value}')
        return 'string'


def parse_tag(line, tag, value):
    tmp = ''
    
    if not tag in line:
       print(f'This line does not contain {tag}') 
       sys.exit()
    
    tmp = line.split(f'<{tag}')[0]
    return f'{tmp}<{tag} type="{infer_type(value)}">{line.split("<" + tag + ">")[1]}'

def get_value(line):
    return line.split('>')[1].split('<')[0]

def main():
    dir_list = next(os.walk('.'))


    for test in dir_list[2]:
        reconstructed_test = ''
        print(test)
        if test[-3:] != 'xml':
            continue

        # if len(sys.argv) < 2:
        #     print('Proper usage -> python patch-output-tags.py BotchedXmlTest.xml')
        #     sys.exit()
        
        with open(test, 'r') as input_file:
            for line in input_file.readlines():
                if '<output>' in line:
                    print()
                    reconstructed_test = reconstructed_test + parse_tag(line, 'output', get_value(line))
                else:
                    reconstructed_test = reconstructed_test + line
        
        with open(test, 'w') as testWrite:
            testWrite.write(reconstructed_test)


if __name__ == '__main__':
    main()
from util import *
from message import * 

def main():
    payload = [{'firstName': 'John', 'lastName': 'Boozman'}, 
               {'firstName': 'John', 'lastName': 'Boozman'}, 
               {'firstName': 'Nancy', 'lastName': 'Pelosi'}]
    send_data_message(count_name_occurrences(payload))

if __name__ == "__main__":
    main()
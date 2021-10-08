import datetime
import json
import os
import random


def greet_customer():
    time = datetime.datetime.now()
    time = time.hour
    if time < 12:
        print("Good morning.")
    elif 12 <= time and time < 18:
        print("Good afternoon.")
    else:
        print("Good evening.")


def get_customer_name():
    customername = raw_input("Please enter your name: ")
    print(customername + " , I hope you're doing well")
    return customername


def get_customer_concern():
    print("\n 1.I have a ticket. \n 2.I want to raise a concern.")
    customerconcern = raw_input("Please select one of the above option: ")
    select_concern_type(int(customerconcern))


def select_concern_type(customerconcern):
    if customerconcern == 1:
        existing_concern()
    elif customerconcern == 2:
        new_concern()
    else:
        print("please select a valid option.")
        get_customer_concern()


def existing_concern():
    ticketnumber = raw_input("Please enter your ticket number: ")
    filename = ticketnumber+".json"
    file_list = os.listdir("./tickets")
    if (filename in file_list):
        with open("./tickets/"+filename) as f:
            data = json.loads(f.read())
            ticketStatus = data["status"]
            print("Your Ticket status : " + ticketStatus)
            if ticketStatus.lower() == "closed":
                print("Your issue has been resolved.")
            else:
                print("We are sorry to inform you that your issue has not resold yet.")
                print("We assure you to get your issue resolved as soon as possible.")
            f.close()
    else:
        print("Invalid ticket number.")
        existing_concern()
    print("Thank you for using our services.\nWe are happy to help you.")


def take_customer_option():
    print("Please select your concern regarding the product.")
    customerOptions = ["Delayed Shipping",
                       "Damage Product Delievered", "Return/Replacement"]
    i = 1
    for item in customerOptions:
        print(str(i)+"."+item)
        i = i+1
    customer_issue = raw_input("Please select one of the above option: ")
    customer_issue = int(customer_issue)
    if (customer_issue in [1, 2, 3]):
        return customerOptions[customer_issue-1]
    else:
        print("Invalid input.")
    take_customer_option()


def new_concern():
    print("Please enter the details of your product.")
    ticketNumber = str(random.randint(10000, 99999))
    product_name = raw_input("Please enter your product name: ")
    product_issue = take_customer_option()
    ticket_description = raw_input(
        "Please provide brief description of your issue: ")
    data = {
        "ticketNumber": ticketNumber,
        "product_name": product_name,
        "status": "open",
        "product_issue": product_issue,
        "ticket_description": ticket_description
    }
    filename = ticketNumber+".json"
    with open("./tickets/"+filename, 'w') as f:
        json.dump(data, f)
    print("I am sorry you are going through this.I've passed this on to our team.\nThank you so much for notifying us about the issue.")
    print("Your ticket has been raised. Your ticket number is "+ticketNumber)
    print("We assure you to get your issue resolved as soon as possible.")


def greet_customer_goodbye(customername):
    print("Hope you liked our chatbot service.\nGood Bye, " +
          customername+". Thank you for being our customer.")


greet_customer()
customername = get_customer_name()
get_customer_concern()
greet_customer_goodbye(customername)

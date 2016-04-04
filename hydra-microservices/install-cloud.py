import UAAC_WRAPPER
import subprocess
import sys


class CloudFoundryService:
    def __init__(self, SERVICE, INSTANCE, PLAN, CONFIG_FILE):
        self.service = SERVICE
        self.instance = INSTANCE
        self.plan = PLAN
        self.config_file = CONFIG_FILE

    def create(self):
        statement = 'cf create-service ' + self.service + ' ' + self.plan + ' ' + self.instance + ' -c ' + self.config_file
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error creating service.")

    def delete(self):
        statement = 'cf delete-service ' + self.service
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error deleting service.")


class CloudFoundryComponent:
    def __init__(self, APPLICATION_NAME, PROJECT_DIRECTORY):
        self.application = APPLICATION_NAME
        self.directory = PROJECT_DIRECTORY

    def initialize(self, uaac):
        print ("Initialize microservice " + self.application)

    def restage(self):
        statement_status = subprocess.call("cf restage " + self.application, shell=True)
        if statement_status == 1:
            sys.exit("Error restaging a uaa service instance to boot")

    def push(self):
        statement = 'cd ' + self.directory
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error pushing application " + self.application)
        statement = 'cf push'
        if statement_status == 1:
            sys.exit("Error pushing application " + self.application)


print ("Deploying microservices ...")

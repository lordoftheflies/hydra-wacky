import sys
import subprocess


class UAAC_WRAPPER:
    prefix = ''
    uaa_instance_url = '${' + prefix + 'UAA_INSTANCE_URL}'
    authorities = '${' + prefix + 'AUTHORITIES}'
    scope = '${' + prefix + 'SCOPE}'
    autoapprove = '${' + prefix + 'AUTO_APPROVE}'
    authorized_grant_types = '${' + prefix + 'AUTHORIZED_GRANT_TYPES}'

    def __init__(self, PREFIX, UAA_INSTANCE_URL):
        self.uaa_instance_url = UAA_INSTANCE_URL
        self.prefix = PREFIX
        self.target()

    def target(self):
        statement = 'uaac target ' + self.uaa_instance_url
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error connecting UAAC target.")

    def addGroup(self, GROUP_NAME):
        statement = 'uaac group add ' + GROUP_NAME
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error creating group.")

    def addUser(self, USER_NAME, EMAILS, PASSWORD):
        statement = 'uaac user add ' + USER_NAME + ' --emails ' + EMAILS + ' --password ' + PASSWORD
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error creating user.")

    def addMember(self, GROUP_NAME, MEMBER_NAME):
        statement = 'uaac member add ' + GROUP_NAME + ' ' + MEMBER_NAME
        statement_status = subprocess.call(statement, shell=True)
        if statement_status == 1:
            sys.exit("Error adding member.")
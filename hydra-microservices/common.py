import sys
import subprocess
import os
import time
import json
import base64


def getVcapJsonForPredixBoot(bootAppName):
    predixBootEnv = subprocess.check_output(["cf", "env", bootAppName])
    systemProvidedVars = resultJson = \
        predixBootEnv.split('System-Provided:')[1].split('No user-defined env variables have been set')[0]
    resultJson = "[" + systemProvidedVars.replace("\n", "").replace("'", "").replace("}{", "},{") + "]"
    print ("formattedJson=" + resultJson)
    return resultJson


def callCommand(statement, fail_message):
    statementStatus = subprocess.call(statement, shell=True)
    if statementStatus == 1:
        sys.exit(fail_message)
    return statementStatus


def callCurl(statement, other_predicate, success_message, fail_message):
    clientResponse = subprocess.check_output(statement, shell=True)
    statementStatusJson = json.loads(clientResponse)
    # print("print the json response is "+ clientResponse)
    if statementStatusJson.get('error'):
        statementStatus = statementStatusJson['error']
        statementStatusDesc = statementStatusJson['error_description']
    else:
        statementStatus = 'success'
        statementStatusDesc = statementStatusJson['id']

    if statementStatus == 'success' or other_predicate not in statementStatusDesc:
        print(success_message)
    else:
        sys.exit(fail_message + statementStatusDesc)


def indicator(value, initial):
    if type(value) == str:
        if value in ('y', 'Y'):
            return True
        else:
            return False
    if type(value) == type(True):
        return value
    else:
        return initial


def callSubprocess(statement, success_message, fail_message):
    statementStatus = subprocess.call(statement, shell=True)
    if statementStatus == 1:
        sys.exit(fail_message)


class UaacWrapper:
    prefix = ''
    uaa_instance_url = '${' + prefix + 'UAA_INSTANCE_URL}'
    authorities = '${' + prefix + 'AUTHORITIES}'
    scope = '${' + prefix + 'SCOPE}'
    autoapprove = '${' + prefix + 'AUTO_APPROVE}'
    authorized_grant_types = '${' + prefix + 'AUTHORIZED_GRANT_TYPES}'

    def __init__(self, PREFIX, UAA_INSTANCE_URL, ADMIN_SECRET):
        self.uaa_instance_url = UAA_INSTANCE_URL
        self.prefix = PREFIX
        self.admin_secret = ADMIN_SECRET
        self.target()

    def target(self):
        return callCommand('uaac target ' + self.uaa_instance_url, 'Error connecting UAAC target.')

    def addGroup(self, GROUP_NAME):
        return callCommand('uaac group add ' + GROUP_NAME, 'Error creating group.')

    def addUser(self, USER_NAME, EMAILS, PASSWORD):
        return callCommand('uaac user add ' + USER_NAME + ' --emails ' + EMAILS + ' --password ' + PASSWORD,
                           'Error creating user.')

    def addMember(self, GROUP_NAME, MEMBER_NAME):
        return callCommand('uaac member add ' + GROUP_NAME + ' ' + MEMBER_NAME, 'Error adding member.')

    def processUAAClientId (self,app,uuaClientURL,method):
        adminToken = self.getUAAAdminToken(app)
        if not adminToken :
            sys.exit("Error getting admin token from the UAA instance ")

        # create a client id
        print("****************** Creating client id ******************")
        print(app.clientScope)
        print(app.clientScopeList)

        createClientIdBody = {"client_id":"","client_secret":"","scope":[],"authorized_grant_types":[],"authorities":[],"autoapprove":["openid"]}
        createClientIdBody["client_id"] = config.rmdAppClientId
        createClientIdBody["client_secret"] = config.rmdAppSecret
        createClientIdBody["scope"] = config.clientScopeList
        createClientIdBody["authorized_grant_types"] = config.clientGrantType
        createClientIdBody["authorities"] = config.clientAuthoritiesList

        createClientIdBodyStr = json.dumps(createClientIdBody)

        headers = ' -H "Authorization:'+adminToken+'\" -H \"Content-Type: application/json\" '
        #uaaCreateClientCurl = 'curl -X '+method+' "'+uuaClientURL+'" -d "'+createClientIdBodyStr+'"'+headers
        uaaCreateClientCurl = "curl -X "+method+" '"+uuaClientURL+"' -d '"+createClientIdBodyStr+"'"+headers
        print ("*****************")
        print (" UAA Client Command"+uaaCreateClientCurl)
        print ("*****************")

        clientResponse  = subprocess. check_output(uaaCreateClientCurl, shell=True)
        statementStatusJson = json.loads(clientResponse)


        if statementStatusJson.get('error'):
            statementStatus = statementStatusJson['error']
            statementStatusDesc = statementStatusJson['error_description']
        else :
            statementStatus = 'success'
            statementStatusDesc = 'success'

        if statementStatus == 'success' or  'Client already exists' in statementStatusDesc :
            print("Success creating or reusing the Client Id")
        else :
            sys.exit("Error Processing ClientId on UAA "+statementStatusDesc )

        return adminToken

    def getUAAAdminToken(self, app):
        adminRealm = "admin:" + self.admin_secret
        adminRelmKey = base64.b64encode(adminRealm)
        headers = ' -H "Authorization: Basic ' + adminRelmKey + '\" -H \"Content-Type: application/x-www-form-urlencoded\" '
        queryClientCreds = "grant_type=client_credentials"
        uaaAdminClientTokenCurl = "curl -X GET '" + app.uaaIssuerId + "?" + queryClientCreds + "'" + headers

        print ("*****************")
        print (" UAA Client GET client ADMIN token " + uaaAdminClientTokenCurl)
        print ("*****************")
        getAdminClientTokenResponse = subprocess.check_output(uaaAdminClientTokenCurl, shell=True)
        getAdminClientTokenResponseJson = json.loads(getAdminClientTokenResponse)

        print("Admin Token is " + getAdminClientTokenResponseJson['token_type'] + " " + getAdminClientTokenResponseJson[
            'access_token'])
        return (getAdminClientTokenResponseJson['token_type'] + " " + getAdminClientTokenResponseJson['access_token'])
    def createClientIdAndAddUser(config):
        # setup the UAA login
        adminToken = processUAAClientId(config,config.UAA_URI+"/oauth/clients","POST")

        # Add users
        print("****************** Adding users ******************")
        addUAAUser(config, config.rmdUser1 , config.rmdUser1Pass, config.rmdUser1 + "@gegrctest.ge.com",adminToken)
        addUAAUser(config, config.rmdAdmin1 , config.rmdAdmin1Pass, config.rmdAdmin1 + "@gegrctest.com",adminToken)

class GitWrapper:
    def checkoutSubmodules(self):
        print("Pulling Submodules ")
        callCommand('git submodule init', 'Error when init submodule ')
        return callCommand('git submodule update --init --remote', 'Error when updating submodules')


class MavenWrapper:
    def __init__(self, SETTINGS, REPOSITORY):
        self.settings = SETTINGS

    def initialize(self):
        print('Maven settings: ' + self.settings)
        print('Maven repository: ' + self.repository)

    def install(self, project_directory, maven_args):
        callCommand('cd ' + project_directory)
        return callCommand('mvn install ' + maven_args, 'Error building the project ' + project_directory)

    def fastInstall(self, project_directory):
        print("Copying artifacts..if mvn dependency get doesn't work, use mvn copy command(slow)")
        if self.settings == '':
            statement = 'cd ' + project_directory + '; mkdir -p target; mvn dependency:get -DgroupId=\${project.groupId} -DartifactId=\${project.artifactId} -Dversion=\${project.version} -Ddest=./target -Dtransitive=false -Dmaven.repo.local=' + self.repository + '; cd ..'
        else:
            statement = 'cd ' + project_directory + '; mkdir -p target; mvn dependency:get -DgroupId=\${project.groupId} -DartifactId=\${project.artifactId} -Dversion=\${project.version} -Ddest=./target -Dtransitive=false -s ../' + self.settings + ' -Dmaven.repo.local=' + self.repository + '; cd ..'
        return callCommand(statement, 'Error copying artifacts from artifactory ' + project_directory)

    def clean(self, project_directory, maven_args):
        callCommand('cd ' + project_directory)
        return callCommand('mvn clean ' + maven_args, 'Error building the project ' + project_directory)

    def package(self, project_directory, maven_args):
        callCommand('cd ' + project_directory)
        return callCommand('mvn package ' + maven_args, 'Error building the project ' + project_directory)


class CloudFoundryWrapper:
    def __init__(self, API, LOGIN, PASSWORD):
        self.api = API
        self.login = LOGIN
        self.password = PASSWORD

    def login(self):
        statement = 'cf login -a https://api.system.aws-usw02-pr.ice.predix.io -u laszlo.hegeds@ge.com -p Apocalypse00A'


class CloudFoundryService:
    def __init__(self, SERVICE, INSTANCE, PLAN, CONFIG_FILE):
        self.service = SERVICE
        self.instance = INSTANCE
        self.plan = PLAN
        self.config_file = CONFIG_FILE

    def create(self):
        return callCommand(
            'cf create-service ' + self.service + ' ' + self.plan + ' ' + self.instance + ' -c ' + os.getcwd() + '/' + self.config_file,
            "Error creating service.")

    def delete(self):
        delete_request = "cf delete-service -f "
        statement_status = callCommand(delete_request, "Error deleting an service instance: " + self.instance)
        time.sleep(10)
        return statement_status


class PredixUaaService(CloudFoundryService):
    # def __init__(self):

    def externalize(self, uaaAdminSecret):
        data = {}
        data['adminClientSecret'] = uaaAdminSecret

        with open(self.config_file, 'w') as outfile:
            json.dump(data, outfile)
            outfile.close()


class CloudFoundryComponent:
    def __init__(self, APPLICATION_NAME, PROJECT_DIRECTORY, REPOSITORY):
        self.application = APPLICATION_NAME
        self.directory = PROJECT_DIRECTORY
        self.repository = REPOSITORY

    def initialize(self, uaac):
        print ("Initialize microservice " + self.application)

    def delete(self):
        delete_request = "cf delete -f -r " + self.application
        statement_status = callCommand(delete_request, "Error deleting an application: " + self.application)
        time.sleep(5)  # Delay for 5 seconds
        return statement_status

    def restage(self):
        return callCommand("cf restage " + self.application, "Error restaging a uaa service instance to boot")

    def push(self):
        statement = 'cd ' + self.directory
        statement_status = callCommand(statement, "Error pushing application " + self.application)
        statement = 'cf push'
        return callCommand(statement, "Error pushing application " + self.application)

    def deploy(self, cfCommand):
        print("****************** Running deployProject for ******************" + self.directory)
        if self.fastinstall:
            maven.fastInstall(self.project_directory)
        print("Deploying project as  " + self.application + " projectDir=" + self.directory)
        return self.push()

    def getVcapJson(self):
        predixBootEnv = subprocess.check_output(["cf", "env", self.application])
        systemProvidedVars = \
        predixBootEnv.split('System-Provided:')[1].split('No user-defined env variables have been set')[0]
        return "[" + systemProvidedVars.replace("\n", "").replace("'", "").replace("}{", "},{") + "]"

    def getUaaConfig(self, uaaService):
        if not hasattr(self, 'uaaIssuerId'):
            d = json.loads(self.getVcapJson())
            self.uaaIssuerId = d[0]['VCAP_SERVICES'][uaaService.service][0]['credentials']['issuerId']
            self.UAA_URI = d[0]['VCAP_SERVICES'][uaaService.service][0]['credentials']['uri']
            uaaZoneHttpHeaderName = d[0]['VCAP_SERVICES'][uaaService.service][0]['credentials']['zone'][
                'http-header-name']
            uaaZoneHttpHeaderValue = d[0]['VCAP_SERVICES'][uaaService.service][0]['credentials']['zone'][
                'http-header-value']
            print("****************** UAA configured As ******************")
            print (
            "\n uaaIssuerId = " + self.uaaIssuerId + "\n UAA_URI = " + self.UAA_URI + "\n " + uaaZoneHttpHeaderName + " = " + uaaZoneHttpHeaderValue + "\n")
            print("****************** ***************** ******************")


class PredixBootService(CloudFoundryComponent):


# def __init__(self):
#     print "Initialize PredixService"




class Project:
    micro_components = [
                           CloudFoundryComponent('ui', 'sdfsdf'),
                           CloudFoundryComponent('ui2', 'sdfsdf'),
                           CloudFoundryComponent('ui3', 'sdfsdf'),
                           CloudFoundryComponent('ui4', 'sdfsdf')
                       ],
    micro_services = [
        CloudFoundryService('s2', 'i2', 'p2', 'c2')
    ]

    uaaService = PredixUaaService('predix-uaa', 'uaa', 'Tiered', 'uaa_payload.json')
    uaaUri = ''
    bootApp = PredixBootService('boot', 'predix-microservice-cf-jsr')

    def __init__(self, PROJECT_DIRECTORY, FASTINSTALL, ALL_DEPLOY):
        self.project_directory = PROJECT_DIRECTORY
        self.fast_install = indicator(FASTINSTALL, True)
        self.all_deploy = indicator(ALL_DEPLOY, False)

    def initialize(self):

        print("Fast install set =" + self.fastinstall)
        print ("Current Directory = " + os.getcwd())

    def createPredixUAASecurityService(self):
        if self.allDeploy:
            # create UAA instance
            self.uaaService.externalize('ombre2383')
            self.uaaService.create()

    def addUAAUser(self, userId, password, email, adminToken):
        createUserBody = {"userName": "", "password": "", "emails": [{"value": ""}]}
        createUserBody["userName"] = userId
        createUserBody["password"] = password
        createUserBody["emails"][0]['value'] = email

        createUserBodyStr = json.dumps(createUserBody)
        print(createUserBodyStr)

        headers = ' -H "Authorization:' + adminToken + '\" -H \"Content-Type: application/json\" '
        createUserCurl = "curl -X POST '" + self.uaaUri + "/Users' " + "-d '" + createUserBodyStr + "'" + headers
        print ("*****************")
        print ("Creating User command : " + createUserCurl)
        print ("*****************")
        callCurl(createUserCurl, 'scim_resource_already_exists', "User is UAA ", "Error adding Users")

    def deleteExistingApplications(self):
        statement_status = 0
        for component in self.micro_components:
            statement_status = component.delete()

        return statement_status

    def deleteExistingServices(self):
        print "Delete Services>? : " + self.all_deploy
        if self.all_deploy:
            # delete UAA instance
            # statement_status = ''
            for service in Project.micro_services:
                statement_status = service.delete()
            return statement_status


uaac = UaacWrapper(ADMIN_SECRET='', PREFIX='', UAA_INSTANCE_URL='')
maven = MavenWrapper('', '')
maven.initialize()

project = Project('', 'y')
project.initialize()

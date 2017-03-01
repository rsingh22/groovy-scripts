import hudson.model.*
import groovy.json.*
  
// get current build
def build = Thread.currentThread()?.executable
  
def envVarsMap = build.properties.get("envVars")

def groupname = envVarsMap['GIT_REPO']
def groupID = ''
def projectname = envVarsMap['ProjectName']
def privateToken = "abcxyzdgggghhhkkkll"

def conn = "http://git.test.in/api/v3/projects/${groupname}%2F${projectname}".toURL().openConnection()
conn.setRequestProperty("PRIVATE-TOKEN", privateToken)

if( conn.responseCode == 200 ) {
    def searchResultTxt = conn.content.text
    def searchResults = new JsonSlurper().parseText(searchResultTxt)
    println """
--- Project: ${searchResults.name} already exists with valid contents in Git
    URL:  ${searchResults.http_url_to_repo}
--- Check within your team if the existing project is valid / old / a dummy project.
    - OR -
--- Work with CD team to delete this project or its existing contents if you need it created as a new project.
"""
    throw new Exception()
} else {
    def theUrl = "http://git.test.in/api/v3/groups/${groupname}"
    conn = theUrl.toURL().openConnection()
    conn.setRequestProperty("PRIVATE-TOKEN", privateToken)
    def content = conn.content.text
    def results = new JsonSlurper().parseText(content)
    groupID = results.id
    
    def query = /{ "name":"${projectname}", "namespace_id":"${groupID}" }/
    
    conn = "http://git.test.in/api/v3/projects".toURL().openConnection()
    conn.setRequestProperty("PRIVATE-TOKEN", privateToken)
    conn.setRequestProperty("Accept", "application/json")
    conn.setRequestProperty("Content-Type", "application/json")
    conn.setRequestMethod("POST")
    conn.setDoOutput(true)
    conn.outputStream.withWriter { Writer writer -> writer << query }
  
    println "\n\n${conn.responseCode}: ${conn.responseMessage}"
    println "--- Project ${projectname} was created.\n\n"
}

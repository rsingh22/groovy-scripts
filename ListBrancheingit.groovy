/**
 * This script will search git for a list of branches for the indicated group name and project name
 * and return them in an array for use in a dropdown in jenkins.
 */

import groovy.json.*

def privateToken = "YOUR PRIVATE API KEY"
def groupname = "ABCD"
def projectname = "createnow"

def theUrl = "http://git.server.name/api/v3/projects/${groupname}%2F${projectname}/repository/branches?private_token=${privateToken}"
//println "searching gitlab with: ${theUrl}"
def conn = theUrl.toURL().openConnection()
def searchResults = []
if( conn.responseCode == 200 ) {
    def content = conn.content.text
//    println content
    def results = new JsonSlurper().parseText(content)
    searchResults = results.collect { it.name }.sort()
} else {
  searchResults = ["Failed to query gitlab: ${conn.responseCode}: ${conn.responseMessage}"]
}
searchResults
# groovy-utils

##How to use the createprojectsingit.groovy through jenkins####
createprojectsingit.groovy

You can pass any git repo in GIT_REPO variable(You can use jenkins Choice Parameter plugin to define multiple GIT REPOS).

Need to pass the ProjectName(which new project you want to create) as a variable(for e.g. from Jenkins you can chosse String Parameter plugin).

Get the private token from the gitlab/git for the user who is having an access to create the project in gitlab.

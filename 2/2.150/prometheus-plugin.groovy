import jenkins.model.*

import hudson.model.*
import hudson.security.*

import jenkins.install.InstallState

def env = System.getenv()

def install_prometheus = env['INSTALL_PROMETHEUS']
if (!install_prometheus) {
  println "### INSTALL_PROMETHEUS environment variable is not set."
  println "### Prometheus plugin is not going to be installed."
  return
}

def jenkins = Jenkins.getInstanceOrNull()

// Jenkins installation and configuration already completed
if (jenkins.installState.isSetupComplete()) {
  println "### Prometheus plugin installation already completed"
  return
}

sleep 6400

def uc = jenkins.model.Jenkins.instance.getUpdateCenter()
def pl = uc.getPlugin('prometheus')

def installationStatus = pl.deploy(true)

// Waiting for plugin to install...
while(!installationStatus.isDone()) {
  sleep(1280)
}

println "### Prometheus plugin installation completed"

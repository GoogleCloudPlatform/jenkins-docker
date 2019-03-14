// Copyright (c) 2018 Google Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of
// this software and associated documentation files (the "Software"), to deal in
// the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
c = 3
while (pl == null && c != 0) {
  println "### Prometheus plugin retry..."
  sleep(1280)
  pl = uc.getPlugin('prometheus')
  c--
}

def installationStatus = pl.deploy(true)

// Waiting for plugin to install...
while(!installationStatus.isDone()) {
  sleep(1280)
}

println "### Prometheus plugin installation completed"

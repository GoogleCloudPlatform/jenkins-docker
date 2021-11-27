import hudson.model.*;
import jenkins.model.*;


Thread.start {
  def env = System.getenv()
  if (env['JENKINS_SLAVE_AGENT_PORT']) {
    sleep 10000

    def instance = Jenkins.getInstance()
    if (instance) {
      println "--> setting agent port for jnlp"

      int port = env['JENKINS_SLAVE_AGENT_PORT'].toInteger()
      instance.setSlaveAgentPort(port)

      println "--> setting agent port for jnlp... done"
    }
  } else {
    println "--> JENKINS_SLAVE_AGENT_PORT not set."
  }
}

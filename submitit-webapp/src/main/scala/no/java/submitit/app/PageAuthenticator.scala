package no.java.submitit.app

import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy
import java.lang.Class
import org.apache.wicket.Page
import pages.EmsIdPage

class PageAuthenticator extends AbstractPageAuthorizationStrategy {

  /**
   * Only page that we use coockies to authorize is the ems-id page for the program committee.
   */
  override def isPageAuthorized[T <: Page](pageClass: Class[T]) = {
    State().emsIdAuthorized || pageClass != classOf[EmsIdPage]
  }

}
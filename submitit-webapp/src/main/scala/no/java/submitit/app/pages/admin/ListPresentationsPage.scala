/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.list._
import org.apache.wicket.behavior.HeaderContributor
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.basic._
import model._
import common.Implicits._

class ListPresentationsPage extends LayoutPage {

  add(org.apache.wicket.behavior.HeaderContributor.forJavaScript("js/jquery-1.3.2.min.js"));
  add(org.apache.wicket.behavior.HeaderContributor.forJavaScript("js/jquery.tablesorter.min.js"));
  
  add(new Link("reload") {
  	def onClick {
  	  setResponsePage(new ListPresentationsPage)
  	}
  })
  
  add(new ListView("presentations", State().backendClient.getAllPresentations) {
    override def populateItem(item: ListItem) {
      val presentationInfo = item.getModelObject.asInstanceOf[PresentationInfo]
      
      item.add(new Link("titleLink") {
      	def onClick {
      	  val pres = State().backendClient.loadPresentation(presentationInfo.id).get
          State().currentPresentation = pres
      	  setResponsePage(new ReviewPage(pres, false))
      	}
      	add(new Label("title", presentationInfo.title))
      })
      item.add(new Label("speaker", presentationInfo.speakerNames.mkString(", ")))
      item.add(new Label("status", presentationInfo.status.toString))
    }
  })
  
}

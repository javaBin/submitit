package subliftit.model

import net.liftweb.mapper._

class Submission extends LongKeyedMapper[Submission] with IdPK {
  def getSingleton = Submission

  object title extends MappedString(this, 200){
    override def validations = valMinLen(3, "Must have atleast length 3") _ :: super.validations
  }

  object body extends MappedTextarea(this, 4096){
    override def validations = valMinLen(1, "Cannot be blank") _ :: super.validations
  }

  object speaker extends LongMappedMapper(this, User)
}

object Submission extends Submission with LongKeyedMetaMapper[Submission]
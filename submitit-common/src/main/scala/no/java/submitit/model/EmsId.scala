package no.java.submitit.model

trait EmsId {

  var originalId: String = _
  
  def canEqual(other: AnyRef): Boolean = {
    other.getClass isAssignableFrom getClass
  }
  
  override def equals(other: Any): Boolean = {
    other match {
      case that: EmsId if (that canEqual this) =>
          originalId == that.originalId
      case _ => false
    }
  }
  
    override def hashCode(): Int = originalId.hashCode + 41
  
}

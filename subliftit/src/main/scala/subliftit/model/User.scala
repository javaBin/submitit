package subliftit.model

import net.liftweb.mapper.{MegaProtoUser, MetaMegaProtoUser}
import net.liftweb.common.Full

class User extends MegaProtoUser[User]{
  def getSingleton = User
}

object User extends User with MetaMegaProtoUser[User] {
  override def skipEmailValidation = true
  override def signupFields = firstName :: lastName :: email :: password :: Nil
  override def screenWrap = Full(<lift:surround with="default" at="content"><lift:bind/></lift:surround>)
}
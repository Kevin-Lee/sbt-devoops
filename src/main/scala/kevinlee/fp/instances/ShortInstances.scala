package kevinlee.fp.instances

import kevinlee.fp.Equal

/**
  * @author Kevin Lee
  * @since 2019-07-28
  */
trait ShortEqualInstance {
  implicit val shortEqual: Equal[Short] = new Equal[Short] {
    @SuppressWarnings(Array("org.wartremover.warts.Equals"))
    override def equal(x: Short, y: Short): Boolean = x == y
  }
}

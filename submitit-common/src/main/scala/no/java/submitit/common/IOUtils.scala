package no.java.submitit.common

trait IOUtils {

  def using[T, C <: { def close() }]
           (closeable: C)
           (f: C => T): T = {
    try {
      f(closeable)
    } finally
      if (closeable != null)
        try {
          closeable.close()
        } catch {
          case e => Console.err.println("Exception on close " + e.toString)
        }
  }	
  
}

package com.recursivity.commons

import java.io.{BufferedReader, InputStreamReader, InputStream}
import io.Source

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 6, 2010
 * Time: 12:05:07 AM
 * To change this template use File | Settings | File Templates.
 */

trait StringInputStreamReader{


  def load(is: InputStream): String = {
    return Source.fromInputStream(is).getLines.reduceLeft(_ + _)
  }

}

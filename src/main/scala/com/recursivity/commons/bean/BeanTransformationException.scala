package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 20/12/2010
 * Time: 00:51
 * To change this template use File | Settings | File Templates.
 */

class BeanTransformationException(target: Class[_]) extends Exception("Cannot transform from class " + target.getName)
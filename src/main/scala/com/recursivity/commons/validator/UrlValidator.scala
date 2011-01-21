package com.recursivity.commons.validator

import java.util.regex.Pattern
import scala.collection.mutable.{HashSet, MutableList}

/*
 * The base code is from the Wicket UrlValidator.
 * https://fisheye6.atlassian.com/browse/~raw,r=1052932/wicket/trunk/
 * wicket-core/src/main/java/org/apache/wicket/validation/validator/UrlValidator.java
 */
/**
 * Checks if an Url is well formed.
 *
 * @author Georgios Zapounidis
 */
case class UrlValidator(key: String, value: () => String) extends Validator {
  import UrlValidator._

  /**
   * Holds the set of current validation options.
   */
  private var options: Long = 0

  /**
   * The set of schemes that are allowed to be in a URL.
   */
  private val allowedSchemes = HashSet.empty[String]

  options = ALLOW_ALL_SCHEMES

  def getKey = key

  def isValid: Boolean = {
    val url = value()

    if (null == url) return false

    val matchAsciiPat = Pattern.compile(LEGAL_ASCII_PATTERN).matcher(url);
		if (!matchAsciiPat.matches) return false

		// Check the whole url address structure
		val matchUrlPat = Pattern.compile(URL_PATTERN).matcher(url);
		if (!matchUrlPat.matches) return false
		if (!isValidScheme(matchUrlPat.group(PARSE_URL_SCHEME))) return false
		if (!isValidAuthority(matchUrlPat.group(PARSE_URL_AUTHORITY))) return false
		if (!isValidPath(matchUrlPat.group(PARSE_URL_PATH))) return false
		if (!isValidQuery(matchUrlPat.group(PARSE_URL_QUERY))) return false
		if (!isValidFragment(matchUrlPat.group(PARSE_URL_FRAGMENT))) return false

		true
  }

  /**
	 * Validates a scheme. If schemes[] was initialized to non-<code>null</code>, then only those
	 * schemes are allowed. Note that this is slightly different than for the constructor.
	 *
	 * @param scheme
	 *            The scheme to validate. A <code>null</code> value is considered invalid.
	 * @return <code>true</code> if the <code>URL</code> is valid
	 */
	protected def isValidScheme(scheme: String): Boolean = {
		if (null == scheme)	return false

		if (!Pattern.compile(SCHEME_PATTERN).matcher(scheme).matches)	return false
		if (isOff(ALLOW_ALL_SCHEMES)) {
			if (!allowedSchemes.contains(scheme)) {
				return false
			}
		}

		true
	}


	/**
	 * Returns <code>true</code> if the authority is properly formatted. An authority is the
	 * combination of host name and port. A <code>null</code> authority value is considered invalid.
	 *
	 * @param authority
	 *            an authority value to validate
	 * @return true if authority (host name and port) is valid.
	 */
	protected def isValidAuthority(authority: String): Boolean = {
		if (null == authority) return false

		val authorityMatcher = Pattern.compile(AUTHORITY_PATTERN).matcher(authority)
		if (!authorityMatcher.matches) return false

		var ipV4Address = false
		var hostname = false
		// check if authority is IP address or hostname
		var hostIP = authorityMatcher.group(PARSE_AUTHORITY_HOST_IP)
		val matchIPV4Pat = Pattern.compile(IP_V4_DOMAIN_PATTERN).matcher(hostIP)
		ipV4Address = matchIPV4Pat.matches

		if (ipV4Address) {
			// this is an IP address so check components
			for (i <- 1 to 4)	{
				val ipSegment = matchIPV4Pat.group(i)
				if (ipSegment == null || ipSegment.length <= 0)	return false

				try	{
					if (Integer.parseInt(ipSegment) > 255) return false
				}	catch {
          case e: NumberFormatException => return false
				}
			}
		}	else {
			// Domain is hostname name
			hostname = Pattern.compile(DOMAIN_PATTERN).matcher(hostIP).matches
		}

		// rightmost hostname will never start with a digit.
		if (hostname)	{
			// LOW-TECH FIX FOR VALIDATOR-202
			// TODO: Rewrite to use ArrayList and .add semantics: see
			// VALIDATOR-203
			val chars = hostIP.toArray
			var size = 1
			for (ch <- chars if ch == '.') size += 1

			val domainSegment = new Array[String](size)
			var matching = true
			var segmentCount = 0
			var segmentLength = 0

			while (matching) {
				val atomMatcher = Pattern.compile(ATOM_PATTERN).matcher(hostIP)
				matching = atomMatcher.find
				if (matching)	{
					domainSegment(segmentCount) = atomMatcher.group(1)
					segmentLength = domainSegment(segmentCount).length + 1
					hostIP = if (segmentLength >= hostIP.length) "" else hostIP.substring(segmentLength)
					segmentCount += 1
				}
			}

			if (segmentCount > 1)	{
				val topLevel = domainSegment(segmentCount - 1)
				if (topLevel.length < 2 || topLevel.length > 4)	return false

				// First letter of top level must be a alpha
				val alphaMatcher = Pattern.compile(ALPHA_PATTERN).matcher(topLevel.substring(0, 1))
				if (!alphaMatcher.matches) return false
			}
		}

		if (!hostname && !ipV4Address) return false

		val port = authorityMatcher.group(PARSE_AUTHORITY_PORT)
		if (port != null) {
			val portMatcher = Pattern.compile(PORT_PATTERN).matcher(port)
			if (!portMatcher.matches)	return false
		}

		val extra = authorityMatcher.group(PARSE_AUTHORITY_EXTRA)
		if (!isBlankOrNull(extra)) return false

    true
	}

  /**
	 * Returns <code>true</code> if the path is valid. A <code>null</code> value is considered
	 * invalid.
	 *
	 * @param path
	 *            a path value to validate.
	 * @return <code>true</code> if path is valid.
	 */
	protected def isValidPath(path: String): Boolean = {
		if (null == path) return false

		val pathMatcher = Pattern.compile(PATH_PATTERN).matcher(path)
		if (!pathMatcher.matches) return false

		val slash2Count = countToken("//", path)
		if (isOff(ALLOW_2_SLASHES) && (slash2Count > 0)) return false

		val slashCount = countToken("/", path)
		val dot2Count = countToken("/..", path)
		if (dot2Count > 0) {
			if ((slashCount - slash2Count - 1) <= dot2Count) return false
		}

		true
	}

  /**
	 * Returns <code>true</code> if the query is <code>null</code> or if it's a properly-formatted
	 * query string.
	 *
	 * @param query
	 *            a query value to validate
	 * @return <code>true</code> if the query is valid
	 */
	protected def isValidQuery(query: String): Boolean = {
		if (null == query) return true
		val queryMatcher = Pattern.compile(QUERY_PATTERN).matcher(query)
		queryMatcher.matches
	}

  /**
	 * Returns <code>true</code> if the given fragment is <code>null</code> or fragments are
	 * allowed.
	 *
	 * @param fragment
	 *            a fragment value to validate
	 * @return <code>true</code> if the fragment is valid
	 */
	protected def isValidFragment(fragment: String): Boolean = {
		if (null == fragment)	return true
		isOff(NO_FRAGMENTS)
	}

	/**
	 * Returns the number of times the token appears in the target.
	 *
	 * @param token
	 *            a token value to be counted
	 * @param target
	 *            a target <code>String</code> to count tokens in
	 * @return the number of tokens
	 */
	protected def countToken(token: String, target: String): Int = {
		var tokenIndex = 0
		var count = 0
		while (tokenIndex != -1) {
			tokenIndex = target.indexOf(token, tokenIndex)
			if (tokenIndex > -1) {
				tokenIndex += 1
				count += 1
			}
		}

    count
	}

	// Flag Management
	/**
	 * Tests whether the given flag is on. If the flag is not a power of 2 (ie. 3) this tests
	 * whether the combination of flags is on.
	 *
	 * @param flag
	 *            flag value to check
	 * @return whether the specified flag value is on
	 */
	def isOn(flag: Long): Boolean = (options & flag) > 0

	/**
	 * Tests whether the given flag is off. If the flag is not a power of 2 (ie. 3) this tests
	 * whether the combination of flags is off.
	 *
	 * @param flag
	 *            flag value to check.
	 * @return whether the specified flag value is off
	 */
	def isOff(flag: Long): Boolean = (options & flag) == 0

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}

object UrlValidator {

  /**
   * Allows all validly-formatted schemes to pass validation instead of supplying a set of valid
   * schemes.
   */
  val ALLOW_ALL_SCHEMES = 1 << 0

  /**
   * Allow two slashes in the path component of the <code>URL</code>.
   */
  val ALLOW_2_SLASHES = 1 << 1

  /**
   * Enabling this option disallows any <code>URL</code> fragments.
   */
  val NO_FRAGMENTS = 1 << 2
  val ALPHA_CHARS = "a-zA-Z"
  val ALPHA_NUMERIC_CHARS = ALPHA_CHARS + "\\d"
  val SPECIAL_CHARS = ";/@&=,.?:+$"
  val VALID_CHARS = "[^\\s" + SPECIAL_CHARS + "]"
  val SCHEME_CHARS = ALPHA_CHARS
  val AUTHORITY_CHARS = ALPHA_NUMERIC_CHARS + "\\-\\."
  val ATOM = VALID_CHARS + '+'

  /**
   * This expression derived/taken from the BNF for URI (RFC2396).
   */
  val URL_PATTERN = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?"

  /**
   * Schema / Protocol (<code>http:</code>, <code>ftp:</code>, <code>file:</code>, etc).
   */
  val PARSE_URL_SCHEME = 2
  val PARSE_URL_AUTHORITY = 4
  val PARSE_URL_PATH = 5
  val PARSE_URL_QUERY = 7
  val PARSE_URL_FRAGMENT = 9

  /**
   * Protocol (<code>http:</code>, <code>ftp:</code>, or <code>https:</code>).
   */
  val SCHEME_PATTERN = "^[" + SCHEME_CHARS + "].*$"
  val AUTHORITY_PATTERN = "^(.+(:.*)?@)?([" + AUTHORITY_CHARS + "]*)(:\\d*)?(.*)?"
  val PARSE_AUTHORITY_HOST_IP = 3
  val PARSE_AUTHORITY_PORT = 4
  val PARSE_AUTHORITY_EXTRA = 5
  val PATH_PATTERN = "^(/[-\\w:@&?=+,.!/~*'%$_;]*)?$"
  val QUERY_PATTERN = "^(.*)$"
  val LEGAL_ASCII_PATTERN = "^[\\x00-\\x7F]+$"
  val IP_V4_DOMAIN_PATTERN = "^(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})$"
  val DOMAIN_PATTERN = "^" + ATOM + "(\\." + ATOM + ")*$"
  val PORT_PATTERN = "^:(\\d{1,5})$"
  val ATOM_PATTERN = "(" + ATOM + ")"
  val ALPHA_PATTERN = "^[" + ALPHA_CHARS + "]"

  /**
   * If no schemes are provided, default to this set of protocols.
   */
  protected var defaultSchemes = Array("http", "https", "ftp")

  /**
   * Checks if the field isn't <code>null</code> and if length of the field is greater than zero,
   * not including whitespace.
   *
   * @param value
   *            the value validation is being performed on
   * @return <code>true</code> if blank or <code>null</code>
   */
  def isBlankOrNull(value: String): Boolean = ((value == null) || (value.trim.length == 0))

}

object Url {
  def apply(key: String, value: => String) = UrlValidator(key, () => value)
}

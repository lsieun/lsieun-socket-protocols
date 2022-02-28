/**
 * <p>
 * One simple bidirectional TCP protocol is dict, defined in RFC 2229. In this protocol,
 * the client opens a socket to port 2628 on the dict server and sends commands such as
 * “DEFINE fd-eng-lat gold”. This tells the server to send a definition of the word gold using
 * its English-to-Latin dictionary.
 * </p><br/>
 * <p>telnet dict.org 2628</p>
 * <p>DEFINE fd-eng-lat gold</p>
 *
 */
package lsieun.dict;
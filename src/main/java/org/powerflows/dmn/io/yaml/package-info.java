/**
 * Contains powerflows human friendly YAML decision table format reading and writing capabilities.
 *
 * Reading: <br/>
 * <pre>
 *     new YamlDecisionReader().readAll(inputStream)
 * </pre>
 * Writing:<br/>
 * <pre>
 *     new YamlDecisionWriter().write(decision, outputStream)
 * </pre>
 */
package org.powerflows.dmn.io.yaml;
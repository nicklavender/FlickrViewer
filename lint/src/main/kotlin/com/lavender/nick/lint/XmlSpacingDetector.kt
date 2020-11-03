package com.lavender.nick.lint

import com.android.ide.common.blame.SourcePosition
import com.android.tools.lint.detector.api.*

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Severity.WARNING
import org.w3c.dom.Document
import java.util.*

val ISSUE_XML_SPACING = Issue.create("XmlSpacing",
		"XML files should not contain any new lines.",
		"Having newlines in xml files just adds noise and should be avoided. The only exception is the new lint at the end of the file.",
		CORRECTNESS, PRIORITY, WARNING,
		Implementation(XmlSpacingDetector::class.java, EnumSet.of(Scope.ALL_RESOURCE_FILES)))

class XmlSpacingDetector : ResourceXmlDetector() {
	override fun visitDocument(context: XmlContext, document: Document) {
		val contents = context.client.readFile(context.file).toString().split("\n")

		contents
				.withIndex()
				.windowed(2)
				.filter { it[0].value.isBlank() && it.getOrNull(1)?.value?.trim()?.startsWith("<!--") == false }
				.map { it[0] }
				.filterNot { it.index == contents.size - 1 }
				.forEach {
					val location = Location.create(context.file, SourcePosition(it.index, 0, it.value.length))
					val fix = fix()
							.name("Remove new line")
							.replace()
							.range(location)
							.all()
							.autoFix(true, false)
							.build()

					context.report(ISSUE_XML_SPACING, location, "Unnecessary new line at line ${it.index + 1}.", fix)
				}
	}
}
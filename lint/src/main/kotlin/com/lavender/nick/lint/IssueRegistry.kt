package com.lavender.nick.lint

import com.android.tools.lint.detector.api.CURRENT_API

internal const val PRIORITY = 10 // Does not matter anyways within Lint.

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
	override val api = CURRENT_API

	override val issues get() = listOf(
			ISSUE_NAMING_PATTERN,
			ISSUE_INVALID_SINGLE_LINE_COMMENT,
			ISSUE_XML_SPACING
	)
}


package com.gowoon.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.common.bbsRule
import com.gowoon.ui.R

@Composable
fun BBSRuleBottomSheet(
    onDismissRequest: () -> Unit
) {
    BottomSheet(
        title = stringResource(R.string.bbs_rule_bottom_sheet_title),
        buttonType = BottomSheetButtonType.Single(stringResource(R.string.bbs_rule_bottom_sheet_done_btn)),
        content = { BBSRuleContent() },
        onDismissRequest = onDismissRequest,
        canDismiss = false
    )
}

@Composable
private fun BBSRuleContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        bbsRule.forEach { BBSRuleItem(it) }
    }
}

@Composable
private fun BBSRuleItem(rule: String) {
    Text(
        text = "• $rule",
        color = DonmaniTheme.colors.Gray95,
        style = DonmaniTheme.typography.Body1
    )
}
package com.mobile.framework.components;

import com.mobile.framework.core.BasePage;
import com.mobile.framework.core.View;

/**
 * Represents the confirmation dialog shown after submitting a product rating.
 */
public final class ReviewDialog extends BasePage {

    public ReviewDialog() {
        super(
                "//*[@content-desc='Closes review dialog']/..",
                ""
        );
    }

    public View message() {
        return view(
                "//*[contains(@resource-id, 'id/sortTV')]",
                ""
        );
    }

    public View btnContinue() {
        return view(
                "//*[contains(@resource-id, 'id/closeBt')]",
                ""
        );
    }

    public void clickButtonContinue() {
        btnContinue().tap();
    }
}

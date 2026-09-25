package com.mobile.framework.components;

import com.mobile.framework.core.View;
import com.mobile.framework.pages.ProductDetailsPage;

/**
 * Represents a product card in the catalog.
 * Provides access to its rating and opens the product by tapping its image.
 */
public final class ProductCard extends ProductItem {

    public ProductCard(View root) {
        super(root);
    }

    public void tap() {
        image().tap();
    }

    public ProductDetailsPage openDetails() {
        tap();
        ProductDetailsPage page = new ProductDetailsPage();
        page.title().waitUntilVisible();
        return page;
    }

    public ReviewDialog rateProduct(int rating) {
        ratingStar(rating).tap();
        return new ReviewDialog();
    }
}
    

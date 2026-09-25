package com.mobile.framework.pages;
import com.mobile.framework.components.ProductCard;
import com.mobile.framework.core.BasePage;
import com.mobile.framework.core.View;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends BasePage {

    public ProductsPage() {
        super(
                "//*[contains(@resource-id, 'id/productTV') " +
                        "and @text='Products']/..",
                ""
        );
    }

    public View title() {
        return view(
                "//*[contains(@resource-id, 'id/productTV')]",
                ""
        );
    }

    /**
     * Scrollable container that wraps the product catalog.
     */
    @Override
    protected View scrollView() {
        return view(
                "//*[contains(@resource-id, 'id/scrollView')]",
                ""
        );
    }

    private View firstProduct() {
        return view(
                "//*[contains(@resource-id, 'id/productRV')]/*[1]",
                ""
        );
    }

    /**
     * The catalog is loaded asynchronously, after the page title.
     */
    @Override
    protected View loadedIndicator() {
        return firstProduct();
    }

    private List<ProductCard> products() {
        waitUntilLoaded();

        List<View> cardRoots = views(
                "//*[contains(@resource-id, 'id/productRV')]/*",
                ""
        );

        List<ProductCard> products = new ArrayList<>();

        for (View cardRoot : cardRoots) {
            ProductCard product = new ProductCard(cardRoot);
            products.add(product);
        }

        return products;
    }

    public List<ProductCard> getProducts() {
        return products();
    }

    public List<String> getVisibleProductTitles() {
        List<String> titles = new ArrayList<>();

        for (ProductCard product : getProducts()) {
            if (product.title().isDisplayed()) {
                titles.add(product.title().text());
            }
        }

        return titles;
    }

    public ProductCard getFirstProduct() {
        return new ProductCard(firstProduct());
    }
}

package com.mobile.tests;

import com.mobile.framework.components.ProductCard;
import com.mobile.framework.components.ReviewDialog;
import com.mobile.framework.core.scroll.ScrollDirection;
import com.mobile.framework.pages.ProductDetailsPage;
import com.mobile.framework.pages.ProductsPage;
import com.mobile.tests.utils.TestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(TestNGListener.class)
public class ProductDetailsTest extends BaseTest {

    private ProductsPage productsPage;
    private SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        productsPage = new ProductsPage();
        softAssert = new SoftAssert();
    }

    @Test
    public void productDetailsPageContainsAllElements_Test() {
        ProductCard product = productsPage.getFirstProduct();
        ProductDetailsPage productDetailsPage = product.openDetails();

        softAssert.assertTrue(productDetailsPage.title().isDisplayed(),
                "Product title is not displayed");

        productDetailsPage.scroll(ScrollDirection.DOWN, 1.0);

        softAssert.assertTrue(productDetailsPage.price().isDisplayed(),
                "Product price is not displayed");

        softAssert.assertTrue(productDetailsPage.rating().isDisplayed(),
                "Product rating is not displayed");

        softAssert.assertFalse(productDetailsPage.colorOptions().isEmpty(),
                "No color options are displayed");

        softAssert.assertTrue(productDetailsPage.decreaseQuantityButton().isDisplayed(),
                "Decrease quantity button is not displayed");

        softAssert.assertTrue(productDetailsPage.quantity().isDisplayed(),
                "Quantity is not displayed");

        softAssert.assertTrue(productDetailsPage.increaseQuantityButton().isDisplayed(),
                "Increase quantity button is not displayed");

        softAssert.assertTrue(productDetailsPage.addToCartButton().isDisplayed(),
                "Add to cart button is not displayed");

        softAssert.assertTrue(productDetailsPage.productHighlights().isDisplayed(),
                "Product highlights title is not displayed");

        softAssert.assertTrue(productDetailsPage.productHighlightsText().isDisplayed(),
                "Product highlights text is not displayed");

        softAssert.assertAll();
    }

    @Test
    public void productColorCanBeChanged_Test() {
        ProductCard product = productsPage.getFirstProduct();
        ProductDetailsPage productDetailsPage = product.openDetails();
        productDetailsPage.scroll(ScrollDirection.DOWN, 1.0);

        int colorsCount = productDetailsPage.colorOptions().size();

        softAssert.assertTrue(colorsCount > 1,
                "Not enough color options to test switching");

        int otherColorIndex = 1;
        productDetailsPage.selectColor(otherColorIndex);

        softAssert.assertTrue(productDetailsPage.isColorSelected(otherColorIndex),
                "Selected color indicator is not shown for the chosen color");

        softAssert.assertAll();
    }

    @Test
    public void productQuantityCanBeChanged_Test() {
        ProductCard product = productsPage.getFirstProduct();
        ProductDetailsPage productDetailsPage = product.openDetails();
        productDetailsPage.scroll(ScrollDirection.DOWN, 1.0);

        int initialQuantity = productDetailsPage.getQuantity();

        productDetailsPage.increaseQuantity();
        int increasedQuantity = productDetailsPage.getQuantity();

        softAssert.assertEquals(increasedQuantity, initialQuantity + 1,
                "Quantity did not increase by 1");

        productDetailsPage.decreaseQuantity();
        int decreasedQuantity = productDetailsPage.getQuantity();

        softAssert.assertEquals(decreasedQuantity, initialQuantity,
                "Quantity did not decrease back to initial value");

        productDetailsPage.decreaseQuantity();
        int zeroQuantity = productDetailsPage.getQuantity();

        softAssert.assertEquals(zeroQuantity, 0,
                "Quantity did not decrease to 0");

        softAssert.assertFalse(productDetailsPage.addToCartButton().isEnabled(),
                "Add to cart button is enabled when quantity is 0");

        productDetailsPage.increaseQuantity();

        softAssert.assertTrue(productDetailsPage.addToCartButton().isEnabled(),
                "Add to cart button is not enabled when quantity is greater than 0");

        softAssert.assertAll();
    }

    @Test
    public void productRatingCanBeSubmitted_Test() {
        ProductCard product = productsPage.getFirstProduct();
        ProductDetailsPage productDetailsPage = product.openDetails();
        productDetailsPage.scroll(ScrollDirection.DOWN, 1.0);

        ReviewDialog reviewDialog = productDetailsPage.selectRating(5);

        String message = reviewDialog.message().text();
        reviewDialog.clickButtonContinue();

        Assert.assertEquals(
                message,
                "Thank you for submitting your review!",
                "Review confirmation message is incorrect"
        );
    }

    @Test
    public void productDetailsScreenCanBeScrolledDown_Test() {
        ProductCard product = productsPage.getFirstProduct();
        ProductDetailsPage productDetailsPage = product.openDetails();

        Assert.assertFalse(productDetailsPage.productHighlights().isDisplayed(),
                "Product highlights are already displayed before scrolling");

        productDetailsPage.scrollScreen(ScrollDirection.DOWN, 1.0);

        Assert.assertTrue(productDetailsPage.productHighlights().isDisplayed(),
                "Product highlights are not displayed after scrolling one screen down");
    }
}

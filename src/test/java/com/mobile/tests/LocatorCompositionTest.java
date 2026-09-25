package com.mobile.tests;

import com.mobile.framework.core.Locator;
import com.mobile.framework.core.Platform;
import com.mobile.framework.core.View;
import com.mobile.framework.pages.HomePage;
import com.mobile.framework.pages.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Framework self-test: checks how locators are composed, no device or app needed.
 */
@Test(groups = "framework")
public class LocatorCompositionTest {

    @Test
    public void locatorResolvesAndroidWhenPlatformIsAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        Locator loc = Locator.of("//*[@resource-id='btn']", "//*[@name='btn']");
        assertEquals(loc.xpath(), "//*[@resource-id='btn']");
    }

    @Test
    public void locatorResolvesIosWhenPlatformIsIos() {
        Platform.setCurrent(Platform.IOS);
        Locator loc = Locator.of("//*[@resource-id='btn']", "//*[@name='btn']");
        assertEquals(loc.xpath(), "//*[@name='btn']");
    }

    @Test
    public void locatorChildComposesXPaths() {
        Platform.setCurrent(Platform.ANDROID);
        Locator parent = Locator.of("//*[@resource-id='page']", "//*[@name='page']");
        Locator child = Locator.of("//*[@resource-id='field']", "//*[@name='field']");
        Locator composed = parent.child(child);

        assertEquals(composed.xpath(), "//*[@resource-id='page']//*[@resource-id='field']");
    }

    @Test
    public void locatorChildComposesIos() {
        Platform.setCurrent(Platform.IOS);
        Locator parent = Locator.of("//*[@resource-id='page']", "//*[@name='page']");
        Locator child = Locator.of("//*[@resource-id='field']", "//*[@name='field']");
        Locator composed = parent.child(child);

        assertEquals(composed.xpath(), "//*[@name='page']//*[@name='field']");
    }

    @Test
    public void locatorSameUsesSameForBothPlatforms() {
        Locator loc = Locator.same("//*[@text='OK']");
        Platform.setCurrent(Platform.ANDROID);
        assertEquals(loc.xpath(), "//*[@text='OK']");
        Platform.setCurrent(Platform.IOS);
        assertEquals(loc.xpath(), "//*[@text='OK']");
    }

    // ---- View locator = page + view ----

    @Test
    public void viewXPathIsPagePlusViewOnAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        Locator pageRoot = Locator.of("//*[@resource-id='login_screen']", "//*[@name='LoginVC']");
        Locator viewOwn = Locator.of("//*[@resource-id='email_input']", "//*[@name='email_tf']");
        View v = new View(pageRoot, viewOwn);

        assertEquals(v.xpath(), "//*[@resource-id='login_screen']//*[@resource-id='email_input']");
    }

    @Test
    public void viewXPathIsPagePlusViewOnIos() {
        Platform.setCurrent(Platform.IOS);
        Locator pageRoot = Locator.of("//*[@resource-id='login_screen']", "//*[@name='LoginVC']");
        Locator viewOwn = Locator.of("//*[@resource-id='email_input']", "//*[@name='email_tf']");
        View v = new View(pageRoot, viewOwn);

        assertEquals(v.xpath(), "//*[@name='LoginVC']//*[@name='email_tf']");
    }

    // ---- Nested views: parent view → child view ----

    @Test
    public void nestedViewComposes3Levels() {
        Platform.setCurrent(Platform.ANDROID);
        Locator page = Locator.of("//*[@resource-id='home']", "//*[@name='home']");
        Locator section = Locator.of("//*[@resource-id='profile']", "//*[@name='profile']");
        Locator avatar = Locator.of("//*[@resource-id='avatar']", "//*[@name='avatar']");

        View sectionView = new View(page, section);
        View avatarView = sectionView.child(avatar);

        assertEquals(avatarView.xpath(),
            "//*[@resource-id='home']//*[@resource-id='profile']//*[@resource-id='avatar']");
    }

    // ---- Page objects produce correct xpaths ----

    @Test
    public void loginPageEmailInputAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        LoginPage page = new LoginPage();
        assertEquals(page.emailInput().xpath(),
            "//*[@resource-id='login_screen']//*[@resource-id='email_input']");
    }

    @Test
    public void loginPageEmailInputIos() {
        Platform.setCurrent(Platform.IOS);
        LoginPage page = new LoginPage();
        assertEquals(page.emailInput().xpath(),
            "//*[@name='LoginViewController']//*[@name='email_textfield']");
    }

    @Test
    public void loginPagePasswordInputAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        LoginPage page = new LoginPage();
        assertEquals(page.passwordInput().xpath(),
            "//*[@resource-id='login_screen']//*[@resource-id='password_input']");
    }

    @Test
    public void loginPageLoginButtonIos() {
        Platform.setCurrent(Platform.IOS);
        LoginPage page = new LoginPage();
        assertEquals(page.loginButton().xpath(),
            "//*[@name='LoginViewController']//*[@name='login_button']");
    }

    @Test
    public void homePageNestedAvatarAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        HomePage page = new HomePage();
        // profileAvatar = home_screen / profile_section / avatar_image
        assertEquals(page.profileAvatar().xpath(),
            "//*[@resource-id='home_screen']//*[@resource-id='profile_section']//*[@resource-id='avatar_image']");
    }

    @Test
    public void homePageNestedAvatarIos() {
        Platform.setCurrent(Platform.IOS);
        HomePage page = new HomePage();
        assertEquals(page.profileAvatar().xpath(),
            "//*[@name='HomeViewController']//*[@name='profile_view']//*[@name='avatar_imageview']");
    }

    @Test
    public void homePageWelcomeLabelAndroid() {
        Platform.setCurrent(Platform.ANDROID);
        HomePage page = new HomePage();
        assertEquals(page.welcomeLabel().xpath(),
            "//*[@resource-id='home_screen']//*[@resource-id='welcome_text']");
    }
}

package com.sleepkqq.sololeveling.ui.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.Optional;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

  private final H1 viewTitle = new H1();

  public MainLayout() {
    setPrimarySection(Section.DRAWER);
    addDrawerContent();
    addHeaderContent();
  }

  private void addHeaderContent() {
    var toggle = new DrawerToggle();
    toggle.setAriaLabel("Menu toggle");

    viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

    addToNavbar(toggle, viewTitle);
  }

  private void addDrawerContent() {
    var appName = new Span("Solo leveling");
    appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
    var header = new Header(appName);

    var scroller = new Scroller(createNavigation());

    addToDrawer(header, scroller, createFooter());
  }

  private SideNav createNavigation() {
    var nav = new SideNav();

    var menuEntries = MenuConfiguration.getMenuEntries();
    menuEntries.forEach(entry ->
        Optional.ofNullable(entry.icon())
            .ifPresentOrElse(
                icon -> nav.addItem(
                    new SideNavItem(entry.title(), entry.path(), new SvgIcon(icon))),
                () -> nav.addItem(new SideNavItem(entry.title(), entry.path()))
            )
    );

    return nav;
  }

  private Footer createFooter() {
    return new Footer();
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    return MenuConfiguration.getPageHeader(getContent()).orElse("");
  }
}
